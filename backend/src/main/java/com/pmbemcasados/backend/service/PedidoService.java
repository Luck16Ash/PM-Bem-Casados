package com.pmbemcasados.backend.service;

import com.pmbemcasados.backend.domain.*;
import com.pmbemcasados.backend.domain.enums.StatusPedido;
import com.pmbemcasados.backend.dto.*;
import com.pmbemcasados.backend.exception.RecursoNaoEncontradoException;
import com.pmbemcasados.backend.exception.RegraNegocioException;
import com.pmbemcasados.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoStatusHistoricoRepository historicoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioAdminRepository usuarioAdminRepository;

    private static final Map<StatusPedido, Set<StatusPedido>> TRANSICOES = Map.of(

            StatusPedido.SOLICITACAO,
            Set.of(
                    StatusPedido.ANALISE_NEGOCIACAO,
                    StatusPedido.CANCELADO),

            StatusPedido.ANALISE_NEGOCIACAO,
            Set.of(
                    StatusPedido.CONFIRMACAO,
                    StatusPedido.CANCELADO),

            StatusPedido.CONFIRMACAO,
            Set.of(
                    StatusPedido.ANALISE_NEGOCIACAO,
                    StatusPedido.AGENDADO),

            StatusPedido.AGENDADO,
            Set.of(
                    StatusPedido.PRONTO),

            StatusPedido.PRONTO,
            Set.of(
                    StatusPedido.ENTREGUE));

    public List<PedidoResponse> listar() {

        return pedidoRepository.findAll()
                .stream()
                .map(this::converter)
                .toList();
    }

    public PedidoResponse buscar(UUID id) {
        return converter(buscarEntidade(id));
    }

    @Transactional
    public PedidoResponse criar(PedidoRequest request) {

        if (!Boolean.TRUE.equals(request.consentimentoLgpd())) {
            throw new RegraNegocioException(
                    "O consentimento LGPD é obrigatório.");
        }

        Cliente cliente = clienteRepository
                .findById(request.clienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Cliente não encontrado."));

        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .tipoEvento(request.tipoEvento())
                .dataEventoSolicitada(request.dataEventoSolicitada())
                .localCep(request.localCep())
                .localCidade(request.localCidade())
                .localBairro(request.localBairro())
                .localLogradouro(request.localLogradouro())
                .localNumero(request.localNumero())
                .localComplemento(request.localComplemento())
                .status(StatusPedido.SOLICITACAO)
                .consentimentoLgpd(true)
                .build();

        pedidoRepository.save(pedido);

        BigDecimal total = BigDecimal.ZERO;

        for (ItemPedidoRequest itemRequest : request.itens()) {

            Produto produto = produtoRepository
                    .findById(itemRequest.produtoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Produto não encontrado."));

            if (!Boolean.TRUE.equals(produto.getAtivo())) {
                throw new RegraNegocioException(
                        "O produto "
                                + produto.getNome()
                                + " está inativo.");
            }

            BigDecimal subtotal = produto.getPrecoUnitario()
                    .multiply(
                            BigDecimal.valueOf(
                                    itemRequest.quantidade()));

            total = total.add(subtotal);

            ItemPedido item = ItemPedido.builder()
                    .pedido(pedido)
                    .produto(produto)
                    .quantidade(itemRequest.quantidade())
                    .personalizacao(itemRequest.personalizacao())
                    .precoUnitarioRegistrado(
                            produto.getPrecoUnitario())
                    .build();

            itemPedidoRepository.save(item);
        }

        pedido.setValorEstimadoInicial(total);

        pedidoRepository.save(pedido);

        PedidoStatusHistorico historicoInicial = PedidoStatusHistorico.builder()
                .pedido(pedido)
                .status(StatusPedido.SOLICITACAO)
                .motivo("Pedido criado.")
                .build();

        historicoRepository.save(historicoInicial);

        return converter(pedido);
    }

    @Transactional
    public PedidoResponse alterarStatus(
            UUID pedidoId,
            PedidoStatusRequest request) {

        Pedido pedido = buscarEntidade(pedidoId);

        StatusPedido atual = pedido.getStatus();
        StatusPedido novo = request.novoStatus();

        validarTransicao(atual, novo);

        if (novo == StatusPedido.CANCELADO
                && (request.motivo() == null
                        || request.motivo().isBlank())) {

            throw new RegraNegocioException(
                    "Informe o motivo do cancelamento.");
        }

        if (novo == StatusPedido.AGENDADO) {

            if (request.dataEventoConfirmada() == null) {
                throw new RegraNegocioException(
                        "A data confirmada do evento é obrigatória.");
            }

            pedido.setDataEventoConfirmada(
                    request.dataEventoConfirmada());
        }

        UsuarioAdmin admin = null;

        if (request.adminId() != null) {

            admin = usuarioAdminRepository
                    .findById(request.adminId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Administrador não encontrado."));
        }

        pedido.setStatus(novo);

        pedidoRepository.save(pedido);

        PedidoStatusHistorico historico = PedidoStatusHistorico.builder()
                .pedido(pedido)
                .status(novo)
                .alteradoPor(admin)
                .motivo(request.motivo())
                .build();

        historicoRepository.save(historico);

        return converter(pedido);
    }

    private void validarTransicao(
            StatusPedido atual,
            StatusPedido novo) {

        Set<StatusPedido> permitidos = TRANSICOES.getOrDefault(
                atual,
                Collections.emptySet());

        if (!permitidos.contains(novo)) {

            throw new RegraNegocioException(
                    "Não é permitido alterar o pedido de "
                            + atual
                            + " para "
                            + novo
                            + ".");
        }
    }

    private Pedido buscarEntidade(UUID id) {

        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Pedido não encontrado."));
    }

    private PedidoResponse converter(Pedido pedido) {

        List<ItemPedidoResponse> itens = itemPedidoRepository
                .findByPedidoId(pedido.getId())
                .stream()
                .map(item -> new ItemPedidoResponse(
                        item.getId(),
                        item.getProduto().getId(),
                        item.getProduto().getNome(),
                        item.getQuantidade(),
                        item.getPersonalizacao(),
                        item.getPrecoUnitarioRegistrado()))
                .toList();

        return new PedidoResponse(
                pedido.getId(),
                pedido.getCliente().getId(),
                pedido.getCliente().getNome(),
                pedido.getTipoEvento(),
                pedido.getDataEventoSolicitada(),
                pedido.getDataEventoConfirmada(),
                pedido.getStatus(),
                pedido.getValorEstimadoInicial(),
                pedido.getValorConfirmado(),
                pedido.getLocalCep(),
                pedido.getLocalCidade(),
                pedido.getLocalBairro(),
                pedido.getLocalLogradouro(),
                pedido.getLocalNumero(),
                pedido.getLocalComplemento(),
                itens,
                pedido.getCriadoEm());
    }
}
package com.pmbemcasados.backend.service;

import com.pmbemcasados.backend.domain.*;
import com.pmbemcasados.backend.domain.enums.OrigemMovimentacaoEstoque;
import com.pmbemcasados.backend.domain.enums.TipoMovimentacaoEstoque;
import com.pmbemcasados.backend.dto.MovimentacaoEstoqueRequest;
import com.pmbemcasados.backend.dto.MovimentacaoEstoqueResponse;
import com.pmbemcasados.backend.exception.RecursoNaoEncontradoException;
import com.pmbemcasados.backend.exception.RegraNegocioException;
import com.pmbemcasados.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovimentacaoEstoqueService {

    private final MovimentacaoEstoqueRepository movimentacaoRepository;
    private final InsumoRepository insumoRepository;
    private final PedidoRepository pedidoRepository;
    private final FornecedorInsumoRepository fornecedorInsumoRepository;

    public List<MovimentacaoEstoqueResponse> listar() {

        return movimentacaoRepository.findAll()
                .stream()
                .map(this::converter)
                .toList();
    }

    public List<MovimentacaoEstoqueResponse> listarPorInsumo(
            UUID insumoId) {

        return movimentacaoRepository
                .findByInsumoIdOrderByCriadoEmDesc(insumoId)
                .stream()
                .map(this::converter)
                .toList();
    }

    @Transactional
    public MovimentacaoEstoqueResponse movimentar(
            MovimentacaoEstoqueRequest request) {

        validarRequest(request);

        Insumo insumo = insumoRepository.findById(request.insumoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Insumo não encontrado."));

        Pedido pedido = null;
        FornecedorInsumo fornecedorInsumo = null;

        if (request.pedidoId() != null) {

            pedido = pedidoRepository.findById(request.pedidoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Pedido não encontrado."));
        }

        if (request.fornecedorInsumoId() != null) {

            fornecedorInsumo = fornecedorInsumoRepository
                    .findById(request.fornecedorInsumoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Fornecedor/insumo não encontrado."));
        }

        atualizarSaldo(
                insumo,
                request.tipo(),
                request.quantidade());

        insumoRepository.save(insumo);

        MovimentacaoEstoque movimentacao = MovimentacaoEstoque.builder()
                .insumo(insumo)
                .tipo(request.tipo())
                .quantidade(request.quantidade())
                .origem(request.origem())
                .pedido(pedido)
                .fornecedorInsumo(fornecedorInsumo)
                .build();

        return converter(
                movimentacaoRepository.save(movimentacao));
    }

    private void atualizarSaldo(
            Insumo insumo,
            TipoMovimentacaoEstoque tipo,
            BigDecimal quantidade) {

        BigDecimal atual = insumo.getQuantidadeEstoque();

        if (atual == null) {
            atual = BigDecimal.ZERO;
        }

        switch (tipo) {

            case ENTRADA -> insumo.setQuantidadeEstoque(
                    atual.add(quantidade));

            case SAIDA -> {

                if (atual.compareTo(quantidade) < 0) {
                    throw new RegraNegocioException(
                            "Estoque insuficiente para realizar a saída.");
                }

                insumo.setQuantidadeEstoque(
                        atual.subtract(quantidade));
            }

            case AJUSTE -> throw new RegraNegocioException(
                    "A regra de ajuste manual de estoque ainda precisa ser definida.");
        }
    }

    private void validarRequest(
            MovimentacaoEstoqueRequest request) {

        OrigemMovimentacaoEstoque origem = request.origem();

        if (origem == OrigemMovimentacaoEstoque.PRODUCAO_PEDIDO) {

            if (request.pedidoId() == null) {
                throw new RegraNegocioException(
                        "Movimentação de produção precisa possuir pedido.");
            }

            if (request.fornecedorInsumoId() != null) {
                throw new RegraNegocioException(
                        "Produção de pedido não deve possuir fornecedor.");
            }
        }

        if (origem == OrigemMovimentacaoEstoque.COMPRA_FORNECEDOR) {

            if (request.fornecedorInsumoId() == null) {
                throw new RegraNegocioException(
                        "Compra precisa possuir fornecedor/insumo.");
            }

            if (request.pedidoId() != null) {
                throw new RegraNegocioException(
                        "Compra de fornecedor não deve possuir pedido.");
            }
        }

        if (origem == OrigemMovimentacaoEstoque.AJUSTE_MANUAL) {

            if (request.pedidoId() != null
                    || request.fornecedorInsumoId() != null) {

                throw new RegraNegocioException(
                        "Ajuste manual não possui pedido nem fornecedor.");
            }
        }
    }

    private MovimentacaoEstoqueResponse converter(
            MovimentacaoEstoque movimentacao) {

        return new MovimentacaoEstoqueResponse(
                movimentacao.getId(),
                movimentacao.getInsumo().getId(),
                movimentacao.getInsumo().getNome(),
                movimentacao.getTipo(),
                movimentacao.getQuantidade(),
                movimentacao.getOrigem(),

                movimentacao.getPedido() != null
                        ? movimentacao.getPedido().getId()
                        : null,

                movimentacao.getFornecedorInsumo() != null
                        ? movimentacao.getFornecedorInsumo().getId()
                        : null,

                movimentacao.getCriadoEm());
    }
}
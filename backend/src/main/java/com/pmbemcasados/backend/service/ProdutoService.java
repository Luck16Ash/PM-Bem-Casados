package com.pmbemcasados.backend.service;

import com.pmbemcasados.backend.domain.Produto;
import com.pmbemcasados.backend.domain.TomCor;
import com.pmbemcasados.backend.dto.ProdutoRequest;
import com.pmbemcasados.backend.dto.ProdutoResponse;
import com.pmbemcasados.backend.exception.RecursoNaoEncontradoException;
import com.pmbemcasados.backend.exception.RegraNegocioException;
import com.pmbemcasados.backend.repository.ProdutoRepository;
import com.pmbemcasados.backend.repository.TomCorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final TomCorRepository tomCorRepository;

    public List<ProdutoResponse> listarTodos() {

        return produtoRepository.findAll()
                .stream()
                .map(this::converter)
                .toList();
    }

    public List<ProdutoResponse> listarAtivos() {

        return produtoRepository.findByAtivoTrue()
                .stream()
                .map(this::converter)
                .toList();
    }

    public ProdutoResponse buscar(UUID id) {
        return converter(buscarEntidade(id));
    }

    public ProdutoResponse criar(ProdutoRequest request) {

        validarProduto(request);

        TomCor tomCor = buscarTomCor(request.tomCorId());

        Produto produto = Produto.builder()
                .nome(request.nome())
                .categoria(request.categoria())
                .personalizado(request.personalizado())
                .precoUnitario(request.precoUnitario())
                .sabor(request.sabor())
                .papelEmbalagem(request.papelEmbalagem())
                .tomCor(tomCor)
                .fotoUrl(request.fotoUrl())
                .ativo(true)
                .build();

        return converter(produtoRepository.save(produto));
    }

    public ProdutoResponse atualizar(
            UUID id,
            ProdutoRequest request) {

        validarProduto(request);

        Produto produto = buscarEntidade(id);

        produto.setNome(request.nome());
        produto.setCategoria(request.categoria());
        produto.setPersonalizado(request.personalizado());
        produto.setPrecoUnitario(request.precoUnitario());
        produto.setSabor(request.sabor());
        produto.setPapelEmbalagem(request.papelEmbalagem());
        produto.setTomCor(buscarTomCor(request.tomCorId()));
        produto.setFotoUrl(request.fotoUrl());

        return converter(produtoRepository.save(produto));
    }

    public void inativar(UUID id) {

        Produto produto = buscarEntidade(id);

        produto.setAtivo(false);

        produtoRepository.save(produto);
    }

    private Produto buscarEntidade(UUID id) {

        return produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Produto não encontrado."));
    }

    private TomCor buscarTomCor(UUID id) {

        if (id == null) {
            return null;
        }

        return tomCorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Tom de cor não encontrado."));
    }

    private void validarProduto(ProdutoRequest request) {

        if (Boolean.FALSE.equals(request.personalizado())) {

            if (request.sabor() == null
                    || request.sabor().isBlank()) {

                throw new RegraNegocioException(
                        "Produto não personalizado deve possuir sabor.");
            }

            if (request.papelEmbalagem() == null
                    || request.papelEmbalagem().isBlank()) {

                throw new RegraNegocioException(
                        "Produto não personalizado deve possuir papel/embalagem.");
            }

            if (request.tomCorId() == null) {

                throw new RegraNegocioException(
                        "Produto não personalizado deve possuir tom de cor.");
            }
        }
    }

    private ProdutoResponse converter(Produto produto) {

        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getCategoria(),
                produto.getPersonalizado(),
                produto.getPrecoUnitario(),
                produto.getSabor(),
                produto.getPapelEmbalagem(),
                produto.getTomCor() != null
                        ? produto.getTomCor().getId()
                        : null,
                produto.getTomCor() != null
                        ? produto.getTomCor().getNome()
                        : null,
                produto.getAtivo(),
                produto.getFotoUrl());
    }
}
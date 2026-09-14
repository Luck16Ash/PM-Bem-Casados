package com.pmbemcasados.backend.service;

import com.pmbemcasados.backend.domain.Fornecedor;
import com.pmbemcasados.backend.domain.FornecedorInsumo;
import com.pmbemcasados.backend.domain.Insumo;
import com.pmbemcasados.backend.dto.FornecedorInsumoRequest;
import com.pmbemcasados.backend.dto.FornecedorInsumoResponse;
import com.pmbemcasados.backend.exception.RecursoNaoEncontradoException;
import com.pmbemcasados.backend.repository.FornecedorInsumoRepository;
import com.pmbemcasados.backend.repository.FornecedorRepository;
import com.pmbemcasados.backend.repository.InsumoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FornecedorInsumoService {

    private final FornecedorInsumoRepository fornecedorInsumoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final InsumoRepository insumoRepository;

    public List<FornecedorInsumoResponse> listar() {

        return fornecedorInsumoRepository.findAll()
                .stream()
                .map(this::converter)
                .toList();
    }

    public FornecedorInsumoResponse buscar(UUID id) {

        return converter(buscarEntidade(id));
    }

    public FornecedorInsumoResponse criar(
            FornecedorInsumoRequest request) {

        Fornecedor fornecedor = fornecedorRepository
                .findById(request.fornecedorId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Fornecedor não encontrado."));

        Insumo insumo = insumoRepository
                .findById(request.insumoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Insumo não encontrado."));

        FornecedorInsumo fornecedorInsumo = FornecedorInsumo.builder()
                .fornecedor(fornecedor)
                .insumo(insumo)
                .ultimoPreco(request.ultimoPreco())
                .ultimaCompraEm(request.ultimaCompraEm())
                .build();

        return converter(
                fornecedorInsumoRepository.save(fornecedorInsumo));
    }

    public FornecedorInsumoResponse atualizar(
            UUID id,
            FornecedorInsumoRequest request) {

        FornecedorInsumo fornecedorInsumo = buscarEntidade(id);

        Fornecedor fornecedor = fornecedorRepository
                .findById(request.fornecedorId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Fornecedor não encontrado."));

        Insumo insumo = insumoRepository
                .findById(request.insumoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Insumo não encontrado."));

        fornecedorInsumo.setFornecedor(fornecedor);
        fornecedorInsumo.setInsumo(insumo);
        fornecedorInsumo.setUltimoPreco(
                request.ultimoPreco());
        fornecedorInsumo.setUltimaCompraEm(
                request.ultimaCompraEm());

        return converter(
                fornecedorInsumoRepository.save(fornecedorInsumo));
    }

    public void excluir(UUID id) {

        FornecedorInsumo fornecedorInsumo = buscarEntidade(id);

        fornecedorInsumoRepository.delete(fornecedorInsumo);
    }

    private FornecedorInsumo buscarEntidade(UUID id) {

        return fornecedorInsumoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Vínculo fornecedor/insumo não encontrado."));
    }

    private FornecedorInsumoResponse converter(
            FornecedorInsumo fornecedorInsumo) {

        return new FornecedorInsumoResponse(
                fornecedorInsumo.getId(),

                fornecedorInsumo.getFornecedor().getId(),
                fornecedorInsumo.getFornecedor().getNome(),

                fornecedorInsumo.getInsumo().getId(),
                fornecedorInsumo.getInsumo().getNome(),

                fornecedorInsumo.getUltimoPreco(),
                fornecedorInsumo.getUltimaCompraEm());
    }
}
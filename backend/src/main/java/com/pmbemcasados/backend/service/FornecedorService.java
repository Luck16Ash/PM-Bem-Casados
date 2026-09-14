package com.pmbemcasados.backend.service;

import com.pmbemcasados.backend.domain.Fornecedor;
import com.pmbemcasados.backend.dto.FornecedorRequest;
import com.pmbemcasados.backend.dto.FornecedorResponse;
import com.pmbemcasados.backend.exception.RecursoNaoEncontradoException;
import com.pmbemcasados.backend.repository.FornecedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FornecedorService {

    private final FornecedorRepository repository;

    public List<FornecedorResponse> listar() {

        return repository.findAll()
                .stream()
                .map(this::converter)
                .toList();
    }

    public FornecedorResponse criar(
            FornecedorRequest request) {

        Fornecedor fornecedor = Fornecedor.builder()
                .nome(request.nome())
                .bairro(request.bairro())
                .regiao(request.regiao())
                .contato(request.contato())
                .build();

        return converter(repository.save(fornecedor));
    }

    public FornecedorResponse buscar(UUID id) {

        Fornecedor fornecedor = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Fornecedor não encontrado."));

        return converter(fornecedor);
    }

    private FornecedorResponse converter(
            Fornecedor fornecedor) {

        return new FornecedorResponse(
                fornecedor.getId(),
                fornecedor.getNome(),
                fornecedor.getBairro(),
                fornecedor.getRegiao(),
                fornecedor.getContato());
    }
}
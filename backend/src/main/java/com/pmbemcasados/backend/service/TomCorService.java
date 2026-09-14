package com.pmbemcasados.backend.service;

import com.pmbemcasados.backend.domain.TomCor;
import com.pmbemcasados.backend.dto.TomCorRequest;
import com.pmbemcasados.backend.dto.TomCorResponse;
import com.pmbemcasados.backend.exception.RecursoNaoEncontradoException;
import com.pmbemcasados.backend.exception.RegraNegocioException;
import com.pmbemcasados.backend.repository.TomCorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TomCorService {

    private final TomCorRepository repository;

    public List<TomCorResponse> listar() {

        return repository.findAll()
                .stream()
                .map(this::converter)
                .toList();
    }

    public List<TomCorResponse> listarAtivos() {

        return repository.findByAtivoTrue()
                .stream()
                .map(this::converter)
                .toList();
    }

    public TomCorResponse criar(TomCorRequest request) {

        if (repository.existsByNomeIgnoreCase(request.nome())) {
            throw new RegraNegocioException(
                    "Já existe um tom de cor com esse nome.");
        }

        TomCor tomCor = TomCor.builder()
                .nome(request.nome())
                .ativo(true)
                .build();

        return converter(repository.save(tomCor));
    }

    public TomCorResponse atualizar(
            UUID id,
            TomCorRequest request) {

        TomCor tomCor = buscar(id);

        tomCor.setNome(request.nome());

        return converter(repository.save(tomCor));
    }

    public void inativar(UUID id) {

        TomCor tomCor = buscar(id);

        tomCor.setAtivo(false);

        repository.save(tomCor);
    }

    private TomCor buscar(UUID id) {

        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Tom de cor não encontrado."));
    }

    private TomCorResponse converter(TomCor tomCor) {

        return new TomCorResponse(
                tomCor.getId(),
                tomCor.getNome(),
                tomCor.getAtivo());
    }
}
package com.pmbemcasados.backend.service;

import com.pmbemcasados.backend.domain.Insumo;
import com.pmbemcasados.backend.dto.InsumoRequest;
import com.pmbemcasados.backend.dto.InsumoResponse;
import com.pmbemcasados.backend.exception.RecursoNaoEncontradoException;
import com.pmbemcasados.backend.repository.InsumoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InsumoService {

    private final InsumoRepository repository;

    public List<InsumoResponse> listar() {

        return repository.findAll()
                .stream()
                .map(this::converter)
                .toList();
    }

    public InsumoResponse criar(InsumoRequest request) {

        Insumo insumo = Insumo.builder()
                .nome(request.nome())
                .unidadeMedida(request.unidadeMedida())
                .quantidadeEstoque(BigDecimal.ZERO)
                .quantidadeMinima(request.quantidadeMinima())
                .build();

        return converter(repository.save(insumo));
    }

    public InsumoResponse buscar(UUID id) {

        return converter(buscarEntidade(id));
    }

    private Insumo buscarEntidade(UUID id) {

        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Insumo não encontrado."));
    }

    private InsumoResponse converter(Insumo insumo) {

        return new InsumoResponse(
                insumo.getId(),
                insumo.getNome(),
                insumo.getUnidadeMedida(),
                insumo.getQuantidadeEstoque(),
                insumo.getQuantidadeMinima());
    }
}
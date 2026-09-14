package com.pmbemcasados.backend.repository;

import com.pmbemcasados.backend.domain.FichaTecnica;
import com.pmbemcasados.backend.domain.FichaTecnicaId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FichaTecnicaRepository
        extends JpaRepository<FichaTecnica, FichaTecnicaId> {

    List<FichaTecnica> findByProdutoId(UUID produtoId);
}
package com.pmbemcasados.backend.repository;

import com.pmbemcasados.backend.domain.MovimentacaoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MovimentacaoEstoqueRepository
        extends JpaRepository<MovimentacaoEstoque, UUID> {

    List<MovimentacaoEstoque> findByInsumoIdOrderByCriadoEmDesc(
            UUID insumoId);
}
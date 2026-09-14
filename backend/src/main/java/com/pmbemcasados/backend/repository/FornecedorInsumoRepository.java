package com.pmbemcasados.backend.repository;

import com.pmbemcasados.backend.domain.FornecedorInsumo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FornecedorInsumoRepository
        extends JpaRepository<FornecedorInsumo, UUID> {

    List<FornecedorInsumo> findByFornecedor_Id(
            UUID fornecedorId);

    List<FornecedorInsumo> findByInsumo_Id(
            UUID insumoId);
}
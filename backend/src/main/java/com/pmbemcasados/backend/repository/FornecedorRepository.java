package com.pmbemcasados.backend.repository;

import com.pmbemcasados.backend.domain.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FornecedorRepository
        extends JpaRepository<Fornecedor, UUID> {
}
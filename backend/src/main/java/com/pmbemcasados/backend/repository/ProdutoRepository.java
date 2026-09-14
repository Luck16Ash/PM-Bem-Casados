package com.pmbemcasados.backend.repository;

import com.pmbemcasados.backend.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProdutoRepository extends JpaRepository<Produto, UUID> {

    List<Produto> findByAtivoTrue();
}
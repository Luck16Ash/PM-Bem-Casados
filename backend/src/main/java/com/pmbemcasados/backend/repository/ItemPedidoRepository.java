package com.pmbemcasados.backend.repository;

import com.pmbemcasados.backend.domain.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ItemPedidoRepository
        extends JpaRepository<ItemPedido, UUID> {

    List<ItemPedido> findByPedidoId(UUID pedidoId);
}
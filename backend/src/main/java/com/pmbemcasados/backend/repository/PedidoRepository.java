package com.pmbemcasados.backend.repository;

import com.pmbemcasados.backend.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

    List<Pedido> findByClienteId(UUID clienteId);
}
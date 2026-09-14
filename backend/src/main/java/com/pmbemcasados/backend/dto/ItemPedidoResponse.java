package com.pmbemcasados.backend.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemPedidoResponse(

        UUID id,
        UUID produtoId,
        String produtoNome,
        Integer quantidade,
        String personalizacao,
        BigDecimal precoUnitarioRegistrado

) {
}
package com.pmbemcasados.backend.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoResponse(
        UUID id,
        String nome,
        String categoria,
        Boolean personalizado,
        BigDecimal precoUnitario,
        String sabor,
        String papelEmbalagem,
        UUID tomCorId,
        String tomCorNome,
        Boolean ativo,
        String fotoUrl) {
}
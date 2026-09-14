package com.pmbemcasados.backend.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record InsumoResponse(
        UUID id,
        String nome,
        String unidadeMedida,
        BigDecimal quantidadeEstoque,
        BigDecimal quantidadeMinima) {
}
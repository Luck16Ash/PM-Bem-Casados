package com.pmbemcasados.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record FornecedorInsumoRequest(

        @NotNull UUID fornecedorId,

        @NotNull UUID insumoId,

        @PositiveOrZero BigDecimal ultimoPreco,

        LocalDate ultimaCompraEm

) {
}
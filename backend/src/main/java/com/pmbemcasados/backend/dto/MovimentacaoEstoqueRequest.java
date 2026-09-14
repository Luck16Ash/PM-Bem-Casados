package com.pmbemcasados.backend.dto;

import com.pmbemcasados.backend.domain.enums.OrigemMovimentacaoEstoque;
import com.pmbemcasados.backend.domain.enums.TipoMovimentacaoEstoque;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record MovimentacaoEstoqueRequest(

        @NotNull UUID insumoId,

        @NotNull TipoMovimentacaoEstoque tipo,

        @NotNull @Positive BigDecimal quantidade,

        @NotNull OrigemMovimentacaoEstoque origem,

        UUID pedidoId,

        UUID fornecedorInsumoId

) {
}
package com.pmbemcasados.backend.dto;

import com.pmbemcasados.backend.domain.enums.OrigemMovimentacaoEstoque;
import com.pmbemcasados.backend.domain.enums.TipoMovimentacaoEstoque;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record MovimentacaoEstoqueResponse(

        UUID id,

        UUID insumoId,

        String insumoNome,

        TipoMovimentacaoEstoque tipo,

        BigDecimal quantidade,

        OrigemMovimentacaoEstoque origem,

        UUID pedidoId,

        UUID fornecedorInsumoId,

        LocalDateTime criadoEm

) {
}
package com.pmbemcasados.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record FornecedorInsumoResponse(

        UUID id,

        UUID fornecedorId,
        String fornecedorNome,

        UUID insumoId,
        String insumoNome,

        BigDecimal ultimoPreco,

        LocalDate ultimaCompraEm

) {
}
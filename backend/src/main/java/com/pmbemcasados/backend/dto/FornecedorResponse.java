package com.pmbemcasados.backend.dto;

import com.pmbemcasados.backend.domain.enums.RegiaoFornecedor;

import java.util.UUID;

public record FornecedorResponse(
        UUID id,
        String nome,
        String bairro,
        RegiaoFornecedor regiao,
        String contato) {
}
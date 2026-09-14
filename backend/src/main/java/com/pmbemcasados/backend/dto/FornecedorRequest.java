package com.pmbemcasados.backend.dto;

import com.pmbemcasados.backend.domain.enums.RegiaoFornecedor;
import jakarta.validation.constraints.NotBlank;

public record FornecedorRequest(

        @NotBlank(message = "O nome do fornecedor é obrigatório.") String nome,

        String bairro,

        RegiaoFornecedor regiao,

        String contato

) {
}
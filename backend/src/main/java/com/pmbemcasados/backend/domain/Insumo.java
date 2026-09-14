package com.pmbemcasados.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "insumo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Insumo {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "unidade_medida", nullable = false)
    private String unidadeMedida;

    @Column(name = "quantidade_estoque", nullable = false)
    private BigDecimal quantidadeEstoque;

    @Column(name = "quantidade_minima", nullable = false)
    private BigDecimal quantidadeMinima;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }

        if (quantidadeEstoque == null) {
            quantidadeEstoque = BigDecimal.ZERO;
        }
    }
}
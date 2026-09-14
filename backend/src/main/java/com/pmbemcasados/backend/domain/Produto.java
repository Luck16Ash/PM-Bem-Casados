package com.pmbemcasados.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "produto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produto {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String nome;

    private String categoria;

    @Column(nullable = false)
    private Boolean personalizado;

    @Column(name = "preco_unitario", nullable = false)
    private BigDecimal precoUnitario;

    private String sabor;

    @Column(name = "papel_embalagem")
    private String papelEmbalagem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tom_cor_id")
    private TomCor tomCor;

    @Column(nullable = false)
    private Boolean ativo;

    @Column(name = "foto_url")
    private String fotoUrl;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {

        if (id == null) {
            id = UUID.randomUUID();
        }

        if (ativo == null) {
            ativo = true;
        }

        if (criadoEm == null) {
            criadoEm = LocalDateTime.now();
        }
    }
}
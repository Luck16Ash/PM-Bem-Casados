package com.pmbemcasados.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ficha_tecnica")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FichaTecnica {

    @EmbeddedId
    private FichaTecnicaId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("produtoId")
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("insumoId")
    @JoinColumn(name = "insumo_id")
    private Insumo insumo;

    @Column(name = "quantidade_necessaria", nullable = false)
    private BigDecimal quantidadeNecessaria;
}
package com.pmbemcasados.backend.domain;

import com.pmbemcasados.backend.domain.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "tipo_evento")
    private String tipoEvento;

    @Column(name = "data_evento_solicitada")
    private LocalDate dataEventoSolicitada;

    @Column(name = "data_evento_confirmada")
    private LocalDate dataEventoConfirmada;

    @Column(name = "local_cep")
    private String localCep;

    @Column(name = "local_cidade")
    private String localCidade;

    @Column(name = "local_bairro")
    private String localBairro;

    @Column(name = "local_logradouro")
    private String localLogradouro;

    @Column(name = "local_numero")
    private String localNumero;

    @Column(name = "local_complemento")
    private String localComplemento;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, columnDefinition = "status_pedido")
    private StatusPedido status;

    @Column(name = "valor_estimado_inicial")
    private BigDecimal valorEstimadoInicial;

    @Column(name = "valor_confirmado")
    private BigDecimal valorConfirmado;

    @Column(name = "consentimento_lgpd", nullable = false)
    private Boolean consentimentoLgpd;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    @PrePersist
    public void prePersist() {

        if (id == null) {
            id = UUID.randomUUID();
        }

        if (status == null) {
            status = StatusPedido.SOLICITACAO;
        }

        criadoEm = LocalDateTime.now();
        atualizadoEm = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        atualizadoEm = LocalDateTime.now();
    }
}
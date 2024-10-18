package br.com.zbhousereservas.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "pagamentos")
@EqualsAndHashCode(of = "id")
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int parcela;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    @NotNull(message = "Data de pagamento deve ser informada")
    private LocalDateTime data_pagamento;
    @NotNull(message = "Valor do pagamento deve ser informado")
    private Double valor_pagamento;
    private Long reservaId;

}

package br.com.zbhousereservas.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Data de pagamento deve ser informada")
    private LocalDate data_pagamento;
    @NotNull(message = "Valor do pagamento deve ser informado")
    private Double valor_pagamento;
    private Long reservaId;
}

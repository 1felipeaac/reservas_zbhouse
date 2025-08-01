package br.com.zbhousereservas.dto;

import br.com.zbhousereservas.entities.Pagamento;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record Parcelas(int parcela,

                       @JsonFormat(pattern = "yyyy-MM-dd") LocalDate data_pagamento,

                       double valor_pagamento) {


    public Parcelas(@NotNull Pagamento pagamento){
        this(pagamento.getParcela(), pagamento.getData_pagamento(), pagamento.getValor_pagamento());
    }
}

package br.com.zbhousereservas.dto;

import br.com.zbhousereservas.entities.Pagamento;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public record PagamentoDTO(
        int parcela,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate data_pagamento,
        double valor_pagamento) {


    public PagamentoDTO(@NotNull Pagamento pagamento) {
        this(
                pagamento.getParcela(),
                pagamento.getData_pagamento(),
                pagamento.getValor_pagamento());
    }
}

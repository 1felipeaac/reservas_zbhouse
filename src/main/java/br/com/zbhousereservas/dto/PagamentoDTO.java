package br.com.zbhousereservas.dto;

import br.com.zbhousereservas.entities.Pagamento;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public record PagamentoDTO(
        int parcela,
        long reservaId,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataPagamento,
        double valorPagamento) {


    public PagamentoDTO(@NotNull Pagamento pagamento, long reservaId) {
        this(
                pagamento.getParcela(),
                reservaId,
                pagamento.getData_pagamento(),
                pagamento.getValor_pagamento());
    }


}

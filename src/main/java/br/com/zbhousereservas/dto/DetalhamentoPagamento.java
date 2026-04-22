package br.com.zbhousereservas.dto;

import org.jetbrains.annotations.NotNull;


public record DetalhamentoPagamento (String nome, PagamentoDTO pagamentoDTO){

    public DetalhamentoPagamento(@NotNull PagamentoDTO parcela, String nome) {

        this(nome, parcela);
    }

}

package br.com.zbhousereservas.dto;


import br.com.zbhousereservas.entities.Reserva;
import br.com.zbhousereservas.validations.ValidarObjetos;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservaResponseDTO{

    private String nome;
    private int qtdDias;
    private int diaria;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataPagamento;
    private  double valorReserva;
    private double entrada;
    private double restante;

    public ReservaResponseDTO(Reserva reserva){
        ValidarObjetos validar = new ValidarObjetos();
        this.nome = reserva.getNome();
        this.qtdDias = validar.intervaloCheckinChekout(reserva.getCheckin(), reserva.getCheckout()).size();
        this.diaria = reserva.getDiaria();
        this.dataPagamento = reserva.getPagamentos().get(0).getData_pagamento();
        this.valorReserva = reserva.getValor_reserva();
        this.entrada = reserva.getPagamentos().get(0).getValor_pagamento();
        this.restante = reserva.getValor_reserva() - reserva.getPagamentos().get(0).getValor_pagamento();
    }

}

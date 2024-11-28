package br.com.zbhousereservas.validations;

import br.com.zbhousereservas.entities.Reserva;
import br.com.zbhousereservas.exceptions.*;
import br.com.zbhousereservas.repositories.ReservaRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Component
public class ValidarObjetos {

    @Autowired
    private ReservaRepository reservaRepository;

    public void validarPagamento(double valorAReceber, Long reservaId, LocalDate data) {
        Reserva reserva = reservaRepository.findById(reservaId).orElseThrow();


        if (reserva.getPagamentos().toArray().length >= 2) {
            throw new ValorParcelaException("Número máximo de parcelas permitidas é 2");
        }

        if (data == null) {
            throw new DataPagamentoNuloException();
        }

        if (valorAReceber < reserva.getValor_reserva()) {
            double diferenca = reserva.getValor_reserva() - reserva.getPagamentos().get(0).getValor_pagamento();
            var message = "Valor insuficiente ! Restam R$" + diferenca + " para finalizar o pagamento da reserva.";
            throw new ValorParcelaException(message);
        }

        if (valorAReceber > reserva.getValor_reserva()) {
            throw new ValorParcelaException("Valor pago ultrapassa o valor total da reserva!");
        }
    }

    public static void validarPrimeiroPagamento(@NotNull Reserva reserva) {
        Double valorPagamento = reserva.getPagamentos().get(0).getValor_pagamento();
        if (reserva.getPagamentos().get(0).getData_pagamento().isAfter(reserva.getCheckin())) {
            throw new PrimeiraParcelaMaiorQueCheckinException();
        }

        if (valorPagamento == null || valorPagamento <= 0) {
            throw new ValorParcelaException("Valor da parcela não pode ser menor que 0 ou nulo!");
        }
    }

    public static void validarValorParcela(double valorReserva, double valor_pagamento) {
        if (valorReserva < valor_pagamento) {
            throw new ValorParcelaException("Valor da Parcela está superior ao valor da reserva");
        }
    }

    public void validarReserva(@NotNull Reserva reserva) {
        if (reserva.getPagamentos().get(0).getData_pagamento() == null) {
            throw new DataPagamentoNuloException();
        }

        if (reserva.getCheckin().isAfter(reserva.getCheckout())) {
            throw new CheuckoutMenorQueCheckinException();
        }

        if (!validarDatasDaReserva(reserva)) {
            throw new ReservaExistenteException();
        }

//        if (!validarDatas(reserva.getCheckin(), reserva.getCheckout())) {
//            throw new ReservaExistenteException();
//        }

        if (!validarPrimeiraParcela(reserva)) {
            throw new PrimeiraParcelaMaiorQueCheckinException();
        }
    }

    public boolean validarDatas(LocalDate checkin, LocalDate checkout) {
        boolean novaReserva = true;

        for (LocalDate dia : intervaloCheckinChekout(checkin, checkout)) {

            boolean checkinExiste = this.reservaRepository.findByCheckin(dia).isPresent();
            boolean checkoutExiste = this.reservaRepository.findByCheckout(dia).isPresent();

            if (checkinExiste || checkoutExiste) {
                throw new ReservaExistenteException();
            }

        }
        return novaReserva;
    }

    public boolean validarDatasDaReserva(Reserva reserva) {
        boolean novaReserva = true;


        for (LocalDate dia : intervaloCheckinChekout(reserva.getCheckin(), reserva.getCheckout())) {
            var usuarioCheckin = this.reservaRepository.findAllByCheckin(dia).stream().filter(Reserva::isAtivo);
            var usuarioCheckout = this.reservaRepository.findAllByCheckout(dia).stream().filter(Reserva::isAtivo);

            boolean checkinExiste = usuarioCheckin.anyMatch(usuario -> true);
            boolean checkoutExiste = usuarioCheckout.anyMatch(usuario -> true);


            if ((checkinExiste) || checkoutExiste) {
                throw new ReservaExistenteException();
            }

        }


        return novaReserva;
    }

    public @NotNull ArrayList<LocalDate> intervaloCheckinChekout(LocalDate checkin, LocalDate checkout) {
        ArrayList<LocalDate> datas = new ArrayList<>();

        LocalDate dataAtual = checkin;

//        LocalDate inicio = checkin.toLocalDate();
//        LocalDate fim = checkout.toLocalDate();
//
//        while (!inicio.isAfter(fim)){
//            datas.add(checkin);
//            inicio = inicio.plusDays(1);
//        }

        while (!dataAtual.isAfter(checkout)) {
            datas.add(dataAtual);
            dataAtual = dataAtual.plusDays(1);
        }

        return datas;
    }

    public static boolean validarPrimeiraParcela(@NotNull Reserva reserva) {
        boolean primeiraParcela = true;

        validarPrimeiroPagamento(reserva);

        reserva.getPagamentos().get(0).setParcela(1);
        return primeiraParcela;
    }
}

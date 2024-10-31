package br.com.zbhousereservas.validations;

import br.com.zbhousereservas.entities.Reserva;
import br.com.zbhousereservas.exceptions.CheuckoutMenorQueCheckinException;
import br.com.zbhousereservas.exceptions.PrimeiraParcelaMaiorQueCheckinException;
import br.com.zbhousereservas.exceptions.ReservaExistenteException;
import br.com.zbhousereservas.exceptions.ValorParcelaException;
import br.com.zbhousereservas.repositories.ReservaRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
public class ValidarObjetos {

    @Autowired
    private ReservaRepository reservaRepository;

    public void validarPagamento(double valorAReceber, Long reservaId){
        Reserva reserva = reservaRepository.findById(reservaId).orElseThrow();


        if (reserva.getPagamentos().toArray().length >= 2) {
            throw new ValorParcelaException("Número máximo de parcelas permitidas é 2");
        }

        if (valorAReceber < reserva.getValor_reserva()) {
            double diferenca = reserva.getValor_reserva() - reserva.getPagamentos().getFirst().getValor_pagamento();
            var message = "Valor insuficiente ! Restam R$" +diferenca + " para finalizar o pagamento da reserva.";
            throw new  ValorParcelaException(message);
        }
        if (valorAReceber > reserva.getValor_reserva()) {
            throw new ValorParcelaException("Valor pago ultrapassa o valor total da reserva!");
        }
    }

    public static void validarPrimeiroPagamento(@NotNull Reserva reserva){
        Double valorPagamento = reserva.getPagamentos().getFirst().getValor_pagamento();
        if (reserva.getPagamentos().getFirst().getData_pagamento().isAfter(reserva.getCheckin())) {
            throw new PrimeiraParcelaMaiorQueCheckinException();
        }

        if (valorPagamento == null || valorPagamento <= 0) {
            throw new ValorParcelaException("Valor da parcela não pode ser menor que 0 ou nulo!");
        }
    }
    
    public static void validarValorParcela(double valorReserva, double valor_pagamento){
        if (valorReserva < valor_pagamento) {
            throw new ValorParcelaException("Valor da Parcela está superior ao valor da reserva");
        }
    }
    
    public void validarReserva(@NotNull Reserva reserva){
        if (reserva.getCheckin().isAfter(reserva.getCheckout())) {
            throw new CheuckoutMenorQueCheckinException();
        }

        if (!validarDatas(reserva.getCheckin(), reserva.getCheckout())) {
            throw new ReservaExistenteException();
        }

        if (!validarPrimeiraParcela(reserva)) {
            throw new PrimeiraParcelaMaiorQueCheckinException();
        }
    }

    public boolean validarDatas(LocalDateTime checkin, LocalDateTime checkout) {
        boolean novaReserva = true;

        for (LocalDateTime dia : intervaloCheckinChekout(checkin, checkout)) {

            boolean checkinExiste = this.reservaRepository.findByCheckin(dia).isPresent();
            boolean checkoutExiste = this.reservaRepository.findByCheckout(dia).isPresent();

            if (checkinExiste || checkoutExiste) {
                throw new ReservaExistenteException();
            }

        }
        return novaReserva;
    }

    public @NotNull ArrayList<LocalDateTime> intervaloCheckinChekout(LocalDateTime checkin, LocalDateTime checkout) {
        ArrayList<LocalDateTime> datas = new ArrayList<>();

        LocalDateTime dataAtual = checkin;

        while (!dataAtual.isAfter(checkout)) {
            datas.add(dataAtual);
            dataAtual = dataAtual.plusDays(1);
        }

        return datas;
    }

    public static boolean validarPrimeiraParcela(@NotNull Reserva reserva) {
        boolean primeiraParcela = true;

        validarPrimeiroPagamento(reserva);

        reserva.getPagamentos().getFirst().setParcela(1);
        return primeiraParcela;
    }
}

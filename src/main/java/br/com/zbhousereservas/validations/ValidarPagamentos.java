package br.com.zbhousereservas.validations;

import br.com.zbhousereservas.entities.Reserva;
import br.com.zbhousereservas.exceptions.PrimeiraParcelaMaiorQueCheckinException;
import br.com.zbhousereservas.exceptions.ValorParcelaException;
import org.springframework.stereotype.Component;

@Component
public class ValidarPagamentos {

    public void validarPagamento(double valorAReceber, Reserva reserva){


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

    public void validarPrimeiraParcela(Reserva reserva){
        Double valorPagamento = reserva.getPagamentos().getFirst().getValor_pagamento();
        if (reserva.getPagamentos().getFirst().getData_pagamento().isAfter(reserva.getCheckin())) {
            throw new PrimeiraParcelaMaiorQueCheckinException();
        }

        if (valorPagamento == null || valorPagamento <= 0) {
            throw new ValorParcelaException("Valor da parcela não pode ser menor que 0 ou nulo!");
        }
    }
}

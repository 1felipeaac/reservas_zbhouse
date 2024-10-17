package br.com.zbhousereservas.exceptions;

public class ValorPagoUltrapassaValorDaReservaException extends RuntimeException {
    public ValorPagoUltrapassaValorDaReservaException(){
        super("Valor pago ultrapassa o valor total da reserva!");
    }
}

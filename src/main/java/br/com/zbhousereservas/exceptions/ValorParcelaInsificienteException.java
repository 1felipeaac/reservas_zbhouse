package br.com.zbhousereservas.exceptions;

public class ValorParcelaInsificienteException extends RuntimeException{
    public ValorParcelaInsificienteException(double diferenca){
        super("Valor insuficiente ! Restam R$" + diferenca + " para finalizar o pagamento da reserva.");
    }
}

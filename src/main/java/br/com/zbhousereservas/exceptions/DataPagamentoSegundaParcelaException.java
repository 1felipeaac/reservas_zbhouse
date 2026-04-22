package br.com.zbhousereservas.exceptions;

public class DataPagamentoSegundaParcelaException extends RuntimeException{
    public DataPagamentoSegundaParcelaException(){
        super("Pagamento da segunda parcela deve ser entre o checkin e o checkout!");
    }
}

package br.com.zbhousereservas.exceptions;

public class DataPagamentoNuloException extends RuntimeException{
    public DataPagamentoNuloException(){
        super("A Data da Parcela deve ser preenchida!");
    }
}

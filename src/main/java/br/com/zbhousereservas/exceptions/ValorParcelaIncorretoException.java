package br.com.zbhousereservas.exceptions;

public class ValorParcelaIncorretoException extends RuntimeException{
    public ValorParcelaIncorretoException(){
        super("Valor da parcela não pode ser menor que 0 ou nulo!");
    }
}

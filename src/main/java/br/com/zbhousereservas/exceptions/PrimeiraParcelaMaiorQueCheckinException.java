package br.com.zbhousereservas.exceptions;

public class PrimeiraParcelaMaiorQueCheckinException extends RuntimeException{
    public PrimeiraParcelaMaiorQueCheckinException(){
        super("Primeira Parcela deve ser paga até a data do checkin!");
    }
}

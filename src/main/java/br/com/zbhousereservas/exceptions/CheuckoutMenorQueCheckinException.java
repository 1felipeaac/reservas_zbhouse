package br.com.zbhousereservas.exceptions;

public class CheuckoutMenorQueCheckinException extends RuntimeException{
    public CheuckoutMenorQueCheckinException(){
        super("Checkout menor que checkin!");
    }
}

package br.com.zbhousereservas.exceptions;

public class ReservaNaoExistenteException extends RuntimeException{
    public ReservaNaoExistenteException(){
        super("A reserva não foi encontrada!");
    }
}

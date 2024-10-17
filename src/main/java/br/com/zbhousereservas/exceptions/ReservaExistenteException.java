package br.com.zbhousereservas.exceptions;

public class ReservaExistenteException extends RuntimeException{
    public ReservaExistenteException(){
        super("Já existe reserva para o período selecionado!");
    }
}

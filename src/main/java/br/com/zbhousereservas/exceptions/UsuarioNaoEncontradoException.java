package br.com.zbhousereservas.exceptions;

public class UsuarioNaoEncontradoException extends RuntimeException{
    public UsuarioNaoEncontradoException(){
        super("Checkout menor que checkin!");
    }
}

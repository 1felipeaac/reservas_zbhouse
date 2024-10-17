package br.com.zbhousereservas.exceptions;

public class ErroAoSalvarException extends RuntimeException{
    public ErroAoSalvarException(){
        super("Erro ao salvar! Entre em contato com o administrador do sistema.");
    }
}

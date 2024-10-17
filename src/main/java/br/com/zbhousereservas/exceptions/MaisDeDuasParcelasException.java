package br.com.zbhousereservas.exceptions;

public class MaisDeDuasParcelasException extends RuntimeException {
    public MaisDeDuasParcelasException(){
        super("Número máximo de parcelas permitidas é 2");
    }
}

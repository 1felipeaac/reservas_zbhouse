package br.com.zbhousereservas.exceptions;

public class ValorParcelaSuperiorAReservaException extends RuntimeException {
    public ValorParcelaSuperiorAReservaException(){
        super("Valor da Parcela está superior ao valor da reserva");
    }
}

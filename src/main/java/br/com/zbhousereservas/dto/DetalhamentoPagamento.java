package br.com.zbhousereservas.dto;

import br.com.zbhousereservas.entities.Pagamento;
import lombok.Data;
import org.jetbrains.annotations.NotNull;


public record DetalhamentoPagamento (String nome,Parcelas parcelas){

    public DetalhamentoPagamento(@NotNull Parcelas parcela, String nome) {

        this(nome, parcela);
    }

}

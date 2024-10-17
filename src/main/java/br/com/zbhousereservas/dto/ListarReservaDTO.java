package br.com.zbhousereservas.dto;

import br.com.zbhousereservas.entities.Reserva;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.List;


public record ListarReservaDTO (String nome,
                                LocalDateTime checkin,
                                LocalDateTime checkout,
                                Double valor_reserva,
                                int desconto,
                                List<Parcelas> pagamentos){


    public ListarReservaDTO(@NotNull Reserva reserva){

        this(reserva.getNome(),
                reserva.getCheckin(),
                reserva.getCheckout(),
                reserva.getValor_reserva(),
                (int)reserva.getDesconto(),
                reserva.getPagamentos().stream().map(Parcelas::new).toList());
    }



}

package br.com.zbhousereservas.dto;

import br.com.zbhousereservas.entities.Reserva;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public record ListarReservaDTO (Long id, String nome,
                                String documento,
                                @JsonFormat(pattern = "yyyy-MM-dd") LocalDate checkin,
                                @JsonFormat(pattern = "yyyy-MM-dd") LocalDate checkout,
                                Double valor_reserva,
                                int desconto,
                                List<Parcelas> pagamentos,
                                boolean ativo){


    public ListarReservaDTO(@NotNull Reserva reserva){

        this(reserva.getId(), reserva.getNome(),
                reserva.getDocumento(),
                reserva.getCheckin(),
                reserva.getCheckout(),
                reserva.getValor_reserva(),
                (int)reserva.getDesconto(),
                reserva.getPagamentos().stream().map(Parcelas::new).toList(),
                reserva.isAtivo());
    }



}

package br.com.zbhousereservas.dto;

import br.com.zbhousereservas.entities.Reserva;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;


public record ListarReservaDTO (Long id,
                                String nome,
                                String documento,
                                @JsonFormat(pattern = "dd/MM/yyyy")
                                LocalDate checkin,
                                @JsonFormat(pattern = "dd/MM/yyyy")
                                LocalDate checkout,
                                Double valorReserva,
                                int desconto,
                                List<PagamentoDTO> pagamentos,
                                boolean ativo){


    public ListarReservaDTO(@NotNull Reserva reserva){

        this(reserva.getId(), reserva.getNome(),
                reserva.getDocumento(),
                reserva.getCheckin(),
                reserva.getCheckout(),
                reserva.getValor_reserva(),
                (int)reserva.getDesconto(),
                reserva.getPagamentos().stream().map(pagamento -> new PagamentoDTO(pagamento, reserva.getId())).toList(),
                reserva.isAtivo());
    }



}

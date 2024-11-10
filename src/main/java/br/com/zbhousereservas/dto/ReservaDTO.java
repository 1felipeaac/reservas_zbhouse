package br.com.zbhousereservas.dto;

import br.com.zbhousereservas.entities.Reserva;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReservaDTO(
        Long id,
        String nome,
        String documento,
        Double valor_reserva,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate checkin,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate checkout)

{
    public ReservaDTO(@NotNull Reserva reserva) {
        this(reserva.getId(), reserva.getNome(), reserva.getDocumento(), reserva.getValor_reserva(), reserva.getCheckin(), reserva.getCheckout());
    }
}

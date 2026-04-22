package br.com.zbhousereservas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record ReservaDTO(

        @NotBlank(message = "O nome do responsável deve ser informado")
        String nome,
        @NotBlank(message = "O documento de identificação deve ser informado")
        String documento,
        @NotNull(message = "A data de entrada deve ser informada")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate checkin,
        @NotNull(message = "A data de saída deve ser informada")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate checkout,
        @NotNull(message = "Valor da Diária deve ser informado!")
        @Min(value = 0, message = "Diaria não pode ser menor que zero")
        int diaria,
        @NotNull(message = "Necessário pagar ao menos uma parcela para realizar a reserva")
        List<PagamentoDTO> pagamentos,
        @Min(value = 0, message = "O desconto deve ser no mínimo 0")
        @Max(value = 100, message = "O desconto deve ser no máximo 100")
        double desconto
){}

package br.com.zbhousereservas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record SegundaParcela(
        double valor,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate data) {
}

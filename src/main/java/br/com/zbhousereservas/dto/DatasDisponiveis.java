package br.com.zbhousereservas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record DatasDisponiveis(@JsonFormat(pattern = "dd/MM/yyyy") List<LocalDate> datasDisponiveis) {
}

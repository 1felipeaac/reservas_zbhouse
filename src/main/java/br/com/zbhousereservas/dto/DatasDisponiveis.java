package br.com.zbhousereservas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record DatasDisponiveis(@JsonFormat(pattern = "yyyy-MM-dd'T'hh:mm")List<LocalDateTime> datasDisponiveis) {
}

package br.com.zbhousereservas.dto;

import lombok.Data;

import java.util.List;

@Data
public class ListarReservaResponse {
    private List<ListarReservaDTO> content;
    private int totalPages;
    private long totalElements;
}

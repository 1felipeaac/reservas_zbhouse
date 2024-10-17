package br.com.zbhousereservas.entities;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Datas {
    private UUID id;
    private LocalDateTime chegada;
    private LocalDateTime partida;
}

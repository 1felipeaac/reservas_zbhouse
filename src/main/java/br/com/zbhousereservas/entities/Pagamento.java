package br.com.zbhousereservas.entities;

import br.com.zbhousereservas.dto.PagamentoDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;


import java.time.LocalDate;

@Data
@Entity(name = "pagamentos")
@EqualsAndHashCode(of = "id")
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int parcela;
    private LocalDate data_pagamento;
    private Double valor_pagamento;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserva_id")
    private Reserva reserva;

    public Pagamento(){}

    public Pagamento(@NotNull PagamentoDTO dto){
        this.valor_pagamento = dto.valor_pagamento();
        this.data_pagamento = dto.data_pagamento();
        this.parcela = dto.parcela();
    }

}

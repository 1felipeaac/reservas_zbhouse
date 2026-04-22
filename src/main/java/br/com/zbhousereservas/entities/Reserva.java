package br.com.zbhousereservas.entities;

import br.com.zbhousereservas.dto.ReservaDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "reservas")
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String documento;
    private Double valor_reserva;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate checkin;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate checkout;
    @Builder.Default
    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pagamento> pagamentos = new ArrayList<>();
    @CreationTimestamp
    private LocalDateTime created_at;
    private double desconto;
    private boolean ativo;
    private int diaria;

    public void adicionarPagamento(Pagamento pagamento) {
        this.pagamentos.add(pagamento);
        pagamento.setReserva(this);
    }

    public static @Nullable ReservaBuilder fromEntityReserva(ReservaDTO dto){

        if(Objects.isNull(dto) || Objects.isNull(dto.pagamentos())){
            return null;
        }

        return Reserva.builder()
                .nome(dto.nome())
                .documento(dto.documento())
                .checkin(dto.checkin())
                .checkout(dto.checkout())
                .diaria(dto.diaria())
                .desconto(dto.desconto())
                .ativo(true);
    }

}

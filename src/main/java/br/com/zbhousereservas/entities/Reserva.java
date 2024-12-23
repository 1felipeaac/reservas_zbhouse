package br.com.zbhousereservas.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity(name = "reservas")
@EqualsAndHashCode(of = "id")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "O nome do responsável deve ser informado")
    private String nome;
    @NotBlank(message = "O documento de identificação deve ser informado")
    private String documento;
    private Double valor_reserva;
    @NotNull(message = "A data de entrada deve ser informada")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkin;
    @NotNull(message = "A data de saída deve ser informada")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkout;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "reservaId")
    @NotNull(message = "Necessário pagar ao menos uma parcela para realizar a reserva")
    private List<Pagamento> pagamentos;
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime created_at;
    @NotNull
    @Min(value = 0, message = "O desconto deve ser no mínimo 0")
    @Max(value = 100, message = "O desconto deve ser no máximo 100")
    private double desconto;
    private boolean ativo;
    @NotNull(message = "Valor da Diária deve ser informado!")
    @Min(value = 0, message = "Diaria não pode ser menor que zero")
    private int diaria;
}

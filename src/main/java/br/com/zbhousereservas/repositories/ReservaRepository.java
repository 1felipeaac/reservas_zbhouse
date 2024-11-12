package br.com.zbhousereservas.repositories;

import br.com.zbhousereservas.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    Optional<Reserva> findByCheckin(LocalDate checkin);
    Optional<Reserva> findByCheckout(LocalDate checkout);
    List<Reserva> findAllByNomeIgnoreCase(String nome);

}

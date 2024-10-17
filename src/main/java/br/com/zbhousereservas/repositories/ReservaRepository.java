package br.com.zbhousereservas.repositories;

import br.com.zbhousereservas.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    Optional<Reserva> findByDocumentoAndCheckin(String documento, LocalDateTime checkin);

    Optional<Reserva> findByCheckin(LocalDateTime checkin);
    Optional<Reserva> findByCheckout(LocalDateTime checkout);
    Optional<Reserva> findByCheckinOrCheckout(LocalDateTime checkin, LocalDateTime checkout);


}

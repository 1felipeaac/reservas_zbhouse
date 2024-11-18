package br.com.zbhousereservas.repositories;

import aj.org.objectweb.asm.commons.Remapper;
import br.com.zbhousereservas.entities.Reserva;
import org.apache.el.stream.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    Optional<Reserva> findByCheckin(LocalDate checkin);
    Optional<Reserva> findByCheckout(LocalDate checkout);
    List<Reserva> findAllByNomeIgnoreCase(String nome);

    Page<Reserva> findAllByAtivoTrue(Pageable pageable);

    List<Reserva> findAllByCheckin(LocalDate dia);

    List<Reserva> findAllByCheckout(LocalDate dia);
}

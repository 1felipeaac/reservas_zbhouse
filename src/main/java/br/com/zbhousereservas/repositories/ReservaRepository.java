package br.com.zbhousereservas.repositories;

import aj.org.objectweb.asm.commons.Remapper;
import br.com.zbhousereservas.entities.Reserva;
import org.apache.el.stream.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findAllByNomeContainingIgnoreCase(String nome);

    Page<Reserva> findAllByAtivoTrue(Pageable pageable);

    List<Reserva> findAllByCheckin(LocalDate dia);

    List<Reserva> findAllByCheckout(LocalDate dia);

    @Query("SELECT r FROM Reserva r WHERE r.ativo = true AND r.checkout >= :hoje")
    List<Reserva> buscarReservasFuturas(@Param("hoje") LocalDate hoje);
}

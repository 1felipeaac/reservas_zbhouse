package br.com.zbhousereservas.repositories;

import br.com.zbhousereservas.entities.Pagamento;
import br.com.zbhousereservas.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PagamentosRepository extends JpaRepository<Pagamento , Long> {

    Optional<List<Pagamento>> findByReservaId(Long id);

    Optional<Pagamento> findFirstByReservaId(Long id);

}

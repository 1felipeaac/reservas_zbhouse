package br.com.zbhousereservas.repositories;

import br.com.zbhousereservas.entities.Autenticacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Autenticacao, Long> {
    UserDetails findByLogin(String login);
}

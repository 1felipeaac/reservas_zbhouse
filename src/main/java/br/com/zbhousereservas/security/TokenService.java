package br.com.zbhousereservas.security;

import br.com.zbhousereservas.entities.Autenticacao;
import br.com.zbhousereservas.exceptions.TokenJWTValidaitonException;
import br.com.zbhousereservas.services.AutenticacaoService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Autowired
    private AutenticacaoService autenticacaoService;

    public String gerarToken(@NotNull Autenticacao autenticacao) {
        UserDetails userDetails = autenticacaoService.loadUserByUsername(autenticacao.getUsername());

        if (userDetails != null) {
            try {
                var algorithm = Algorithm.HMAC256(secret);

                return JWT.create()
                        .withIssuer("API zbhousereservas")
                        .withSubject(autenticacao.getLogin())
                        .withExpiresAt(dataExpiracao())
                        .sign(algorithm);
            } catch (JWTCreationException exception) {
                // Invalid Signing configuration / Couldn't convert Claims.
                throw new RuntimeException("Erro ao gerar o token jwt", exception);
            }

        }else{
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
    }


    public String getSubject(String tokenJWT) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("API zbhousereservas")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();

        } catch (JWTVerificationException exception) {
            throw new TokenJWTValidaitonException("Token JWT inválido ou expirado!");
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}

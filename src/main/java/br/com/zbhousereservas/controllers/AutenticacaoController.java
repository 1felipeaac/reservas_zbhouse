package br.com.zbhousereservas.controllers;

import br.com.zbhousereservas.dto.DadosAutenticacao;
import br.com.zbhousereservas.entities.Usuario;
import br.com.zbhousereservas.security.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid @NotNull DadosAutenticacao dados, HttpServletResponse response){
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        try{

            var authentication = manager.authenticate(authenticationToken);

            var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

            String cookieValue = "token=" + tokenJWT + "; HttpOnly; Secure; SameSite=None; Domain=localhost; Path=/; Max-Age=" + (24 * 60 * 60);
            response.addHeader("Set-Cookie", cookieValue);

            return ResponseEntity.ok(dados.login());

        }catch (Exception e){
            return ResponseEntity.badRequest().body("Usuário/Senha errados ou ausentes");
        }
    }
}

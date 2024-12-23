package br.com.zbhousereservas.controllers;

import br.com.zbhousereservas.dto.DadosAutenticacao;
import br.com.zbhousereservas.dto.UsuarioAutenticado;
import br.com.zbhousereservas.entities.Autenticacao;
import br.com.zbhousereservas.security.TokenService;
import br.com.zbhousereservas.services.AutenticacaoService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AutenticacaoService autenticacaoService;
    @Value("${api.security.cookie.domain}")
    private String cookieDomain;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid @NotNull DadosAutenticacao dados, HttpServletResponse response){
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        try{

            var authentication = manager.authenticate(authenticationToken);

            var tokenJWT = tokenService.gerarToken((Autenticacao) authentication.getPrincipal());

//            String cookieValue = "token=" + tokenJWT + "; HttpOnly; Secure; SameSite=None; Domain="+cookieDomain+"; Path=/; Max-Age=" + (24 * 60 * 60);

            int maxAgeInSeconds = 24 * 60 * 60;
            Cookie cookieValue = new Cookie("token", tokenJWT);
            cookieValue.setHttpOnly(true);
            cookieValue.setSecure(true);
//            cookie.setSameSite("None");
            cookieValue.setDomain(cookieDomain); // Defina o domínio correto do seu cookie
            cookieValue.setPath("/");
            cookieValue.setMaxAge(maxAgeInSeconds);

//            response.addHeader("Set-Cookie", cookieValue);

            response.addCookie(cookieValue);
            return ResponseEntity.ok(new UsuarioAutenticado(dados.login()));


        }catch (InternalAuthenticationServiceException e){
            String message = "Formulário Obrigatório!";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{user}")
    public ResponseEntity validarUsuario(@PathVariable String user){

        try {
            var usuario = this.autenticacaoService.loadUserByUsername(user);

            return ResponseEntity.ok().body(usuario);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Usuário inválido");
        }

    }
}

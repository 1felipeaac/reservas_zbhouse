package br.com.zbhousereservas.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ExceptionHandlerControllerAdvice {

    private final MessageSource messageSource;

    private ExceptionHandlerControllerAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(ReservaExistenteException.class)
    public ResponseEntity<String> handleReservaExistenteExceptio(@NotNull ReservaExistenteException e){

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(PrimeiraParcelaMaiorQueCheckinException.class)
    public ResponseEntity<String> handlePrimeiraParcelaMaiorQueCheckinException(@NotNull PrimeiraParcelaMaiorQueCheckinException e){

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(ValorParcelaException.class)
    public ResponseEntity<String> handleValorParcelaException(@NotNull ValorParcelaException e){

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(CheuckoutMenorQueCheckinException.class)
    public ResponseEntity<String> handleCheuckoutMenorQueCheckinException(@NotNull CheuckoutMenorQueCheckinException e){

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(DataPagamentoSegundaParcelaException.class)
    public ResponseEntity<String> handleDataPagamentoSegundaParcelaException(@NotNull DataPagamentoSegundaParcelaException e){

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(ReservaNaoExistenteException.class)
    public ResponseEntity<String> handleReservaNaoExistenteException(@NotNull ReservaNaoExistenteException e){

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessageDTO> handleHttpMessageNotReadableException(@NotNull HttpMessageNotReadableException e) {

        if (e.getCause() instanceof InvalidFormatException invalidFormatException) {

            String campo = invalidFormatException.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .reduce((first, second) -> second)
                    .orElse("campo_desconhecido");

            String mensagem = String.format("O valor informado para o campo '%s' é de um tipo inválido.", campo);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessageDTO(campo, mensagem));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessageDTO(null, "O corpo da requisição possui um formato JSON inválido."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorMessageDTO>> handleValidationExceptions(@NotNull MethodArgumentNotValidException ex) {

        List<ErrorMessageDTO> erros = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> new ErrorMessageDTO(erro.getField(), erro.getDefaultMessage()))
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ErrorMessageDTO> handleInternalAuthenticationServiceException(@NotNull InternalAuthenticationServiceException e) {
        String message = "Erro de autenticação interna: " + e.getMessage(); // Mensagem de erro personalizada
        ErrorMessageDTO dto = new ErrorMessageDTO("autenticacao", message);

        return new ResponseEntity<>(dto, HttpStatus.UNAUTHORIZED); // Defina o status HTTP apropriado
    }

    @ExceptionHandler(TokenJWTValidaitonException.class)
    public ResponseEntity<ErrorMessageDTO> handleTokenJWTValidaitonException(TokenJWTValidaitonException e) {
        try {
            ErrorMessageDTO errorDto = new ErrorMessageDTO(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDto);

        }catch (Exception ex){
            ErrorMessageDTO errorDto = new ErrorMessageDTO(null, ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
        }
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<ErrorMessageDTO> handleJWTVerificationException(@NotNull JWTVerificationException e) {
        ErrorMessageDTO errorDto = new ErrorMessageDTO(null, e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDto);
    }
}

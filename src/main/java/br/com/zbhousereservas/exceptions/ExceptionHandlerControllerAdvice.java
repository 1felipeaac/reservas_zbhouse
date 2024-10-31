package br.com.zbhousereservas.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorMessageDTO>> handlerMethodArgumentNotValidException(@NotNull MethodArgumentNotValidException e) {
        List<ErrorMessageDTO> dto = new ArrayList<>();
        e.getBindingResult().getFieldErrors().forEach(err -> {

            String message = messageSource.getMessage(err, LocaleContextHolder.getLocale());
            dto.add(new ErrorMessageDTO(err.getField(), message));
        });

        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessageDTO> handlerHttpMessageNotReadableException(HttpMessageNotReadableException e) {

        try {

            ErrorMessageDTO errorDto = new ErrorMessageDTO(null, "Falta informação no formulário!");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessageDTO("Erro ao processar a mensagem JSON", null));
        }
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
    public ResponseEntity<ErrorMessageDTO> handleJWTVerificationException(JWTVerificationException e) {
        ErrorMessageDTO errorDto = new ErrorMessageDTO(null, e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDto);
    }
}

package vn.dating.app.gateway.configs.exception;

import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@ControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<String> handleTokenValidationException(TokenExpiredException ex) {
        // Handle the token validation exception and return an appropriate response
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage()+"asdsa");
    }
}


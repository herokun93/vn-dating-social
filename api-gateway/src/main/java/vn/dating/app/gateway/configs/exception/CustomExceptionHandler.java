package vn.dating.app.gateway.configs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(AuthenticationTokenMissingException.class)
    public ResponseEntity<String> handleAuthenticationTokenMissingException(AuthenticationTokenMissingException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(AuthenticationTokenForbidden.class)
    public ResponseEntity<String> handleAuthenticationTokenForbidden(AuthenticationTokenForbidden ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
}


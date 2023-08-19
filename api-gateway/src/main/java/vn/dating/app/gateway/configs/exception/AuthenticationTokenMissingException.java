package vn.dating.app.gateway.configs.exception;

public class AuthenticationTokenMissingException extends RuntimeException {

    public AuthenticationTokenMissingException(String message) {
        super(message);
    }


}


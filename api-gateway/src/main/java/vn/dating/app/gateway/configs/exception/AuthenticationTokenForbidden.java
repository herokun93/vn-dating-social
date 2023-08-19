package vn.dating.app.gateway.configs.exception;

public class AuthenticationTokenForbidden extends RuntimeException {

    public AuthenticationTokenForbidden(String message) {
        super(message);
    }


}
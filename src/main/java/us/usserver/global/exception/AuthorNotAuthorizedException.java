package us.usserver.global.exception;

public class AuthorNotAuthorizedException extends RuntimeException {
    public AuthorNotAuthorizedException(String message) {
        super(message);
    }
}

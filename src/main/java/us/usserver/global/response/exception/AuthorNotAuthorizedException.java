package us.usserver.global.response.exception;

public class AuthorNotAuthorizedException extends RuntimeException {
    public AuthorNotAuthorizedException(String message) {
        super(message);
    }
}

package us.usserver.global.response.exception;

public class DuplicatedVoteException extends RuntimeException {
    public DuplicatedVoteException(String message) {
        super(message);
    }
}

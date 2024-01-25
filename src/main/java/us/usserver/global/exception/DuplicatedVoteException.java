package us.usserver.global.exception;

public class DuplicatedVoteException extends RuntimeException {
    public DuplicatedVoteException(String message) {
        super(message);
    }
}

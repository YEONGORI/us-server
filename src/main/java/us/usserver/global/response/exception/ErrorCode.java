package us.usserver.global.response.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    AUTHOR_NOT_FOUND(HttpStatus.NOT_FOUND, ExceptionMessage.AUTHOR_NOT_FOUND),

    CHAPTER_NOT_FOUND(HttpStatus.NOT_FOUND, ExceptionMessage.CHAPTER_NOT_FOUND),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, ExceptionMessage.MEMBER_NOT_FOUND),

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");

    private final HttpStatus status;
    private final String message;
    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}

package us.usserver.global;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import us.usserver.ApiResponse;
import us.usserver.global.exception.NovelNotFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(NovelNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiResponse<Object> novelNotFoundHandler(Exception e) {
        log.error(ExceptionMessage.Novel_NOT_FOUND);
        return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
    }
}

package us.usserver.global;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import us.usserver.global.exception.*;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(NovelNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiResponse<Object> novelNotFoundHandler(Exception e) {
        log.error(ExceptionMessage.Novel_NOT_FOUND);
        return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
    }
    @ExceptionHandler(AuthorNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiResponse<Object> authorNotFoundHandler(Exception e) {
        log.error(ExceptionMessage.Author_NOT_FOUND);
        return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
    }

    @ExceptionHandler(ChapterNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiResponse<Object> chapterNotFoundHandler(Exception e) {
        log.error(ExceptionMessage.Chapter_NOT_FOUND);
        return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
    }

    @ExceptionHandler(ParagraphNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiResponse<Object> paragraphNotFoundHandler(Exception e) {
        log.error(ExceptionMessage.Paragraph_NOT_FOUND);
        return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
    }

    @ExceptionHandler(MainAuthorIsNotMatchedException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    protected ApiResponse<Object> mainAuthorIsNotMatchedHandler(Exception e) {
        log.error(ExceptionMessage.Main_Author_NOT_MATCHED);
        return new ApiResponse<>(HttpStatus.NOT_ACCEPTABLE.value(), e.getMessage(), null);
    }
}

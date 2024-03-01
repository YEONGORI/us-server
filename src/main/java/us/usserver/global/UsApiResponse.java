package us.usserver.global;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@AllArgsConstructor
public class UsApiResponse<T> {
    private HttpStatus status;
    private String message;
    private T data;
}

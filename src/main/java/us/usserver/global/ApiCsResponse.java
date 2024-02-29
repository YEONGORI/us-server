package us.usserver.global;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class ApiCsResponse<T> {
    private int status;
    private String message;
    private T data;

    private ApiCsResponse() {}
}

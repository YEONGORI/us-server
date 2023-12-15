package us.usserver.global;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiCsResponse<T> {
    private int status;
    private String message;
    private T data;
}

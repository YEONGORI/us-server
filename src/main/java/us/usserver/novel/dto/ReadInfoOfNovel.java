package us.usserver.novel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadInfoOfNovel {
    // 읽은 소설 같은 경우에는 Author Column에 종속되기 때문에 query문을 사용하여 Novel을 조회하지 않는다.
    // 따라서 subList를 사용하기 위해서 from ~ to 까지의 값이 필요하여 아래와 같이 Column 정의
    @Schema(description = "From Novel Number", example = "5")
    @NotNull
    Integer getNovelSize;
    @Schema(description = "To Novel Number", example = "10")
    @NotNull
    Integer size;
}

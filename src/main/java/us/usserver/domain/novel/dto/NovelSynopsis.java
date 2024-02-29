package us.usserver.domain.novel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NovelSynopsis {
    @Size(max = 300)
    @NotBlank
    private String synopsis;
}

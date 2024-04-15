package us.usserver.domain.novel.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NovelSynopsis(
        @Size(max = 300)
        @NotBlank
        String synopsis
) {}

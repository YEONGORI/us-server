package us.usserver.chapter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChaptersOfNovel {
    private Long id;
    private String title;
    private Integer part;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

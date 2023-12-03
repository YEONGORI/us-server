package us.usserver.chapter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.usserver.chapter.chapterEnum.ChapterStatus;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChaptersOfNovel {
    private Long id;
    private String title;
    private Integer part;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ChapterStatus status;
}

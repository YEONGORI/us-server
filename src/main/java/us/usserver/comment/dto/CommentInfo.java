package us.usserver.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.usserver.comment.Comment;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentInfo {
    private Long id;
    private String content;
    private String authorName;
    private String location;
    private Integer likeCnt;
    private LocalDateTime createdAt;

    public static CommentInfo fromComment(Comment comment, String location, Integer likeCnt) {
        return CommentInfo.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .authorName(comment.getAuthor().getNickname())
                .location(location)
                .likeCnt(likeCnt)
                .createdAt(comment.getCreatedAt())
                .build();
    }
}

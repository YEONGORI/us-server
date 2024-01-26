package us.usserver.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCommentResponse {
    @Schema(description = "댓글 목록", example = "[댓글정보1, 댓글정보2, 댓글정보3, ...]")
    private List<CommentInfo> commentInfos;
}

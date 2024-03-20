package us.usserver.domain.comment.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import us.usserver.domain.comment.service.CommentLikeService;
import us.usserver.global.response.exception.AuthorNotFoundException;
import us.usserver.global.response.exception.DuplicatedLikeException;
import us.usserver.global.response.ApiCsResponse;

@Tag(name = "댓글 좋아요 API")
@ResponseBody
@RestController
@RequestMapping("/like/comment")
@RequiredArgsConstructor
public class CommentLikeController {
    private final CommentLikeService commentLikeService;

    @Operation(summary = "댓글 좋아요", description = "댓글 하트 누르기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요 성공"),
            @ApiResponse(
                    responseCode = "400", description = "작가 혹은 댓글 정보가 유효하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class))),
            @ApiResponse(
                    responseCode = "400", description = "이미 좋아요를 누르셨습니다..",
                    content = @Content(schema = @Schema(implementation = DuplicatedLikeException.class)))
    })
    @PostMapping("/{commentId}")
    public ApiCsResponse<Void> setLike(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long commentId
    ) {
        commentLikeService.postLike(commentId, memberId);
        return ApiCsResponse.success();
    }

    @Operation(summary = "댓글 좋아요 취소", description = "댓글 하트 다시 누르기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "취소 완료"),
            @ApiResponse(
                    responseCode = "400", description = "작가 혹은 소설 정보가 유효하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class))
            )
    })
    @DeleteMapping("/{commentId}")
    public ApiCsResponse<Void> deleteLike(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long commentId
    ) {
        commentLikeService.deleteLike(commentId, memberId);
        return ApiCsResponse.success();
    }
}

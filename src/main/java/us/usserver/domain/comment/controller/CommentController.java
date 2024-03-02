package us.usserver.domain.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import us.usserver.domain.comment.dto.CommentContent;
import us.usserver.domain.comment.dto.CommentInfo;
import us.usserver.domain.comment.dto.GetCommentResponse;
import us.usserver.domain.comment.service.CommentService;
import us.usserver.global.exception.AuthorNotAuthorizedException;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.global.exception.ChapterNotFoundException;
import us.usserver.global.exception.CommentLengthOutOfRangeException;
import us.usserver.global.exception.NovelNotFoundException;
import us.usserver.global.response.ApiCsResponse;

@Tag(name = "댓글 API")
@ResponseBody
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "소설의 댓글 불러오기", description = "한 소설에서 작성된 댓글 전부 불러오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 불러오기 성공",
                    content = @Content(schema = @Schema(implementation = GetCommentResponse.class))),
            @ApiResponse(
                    responseCode = "400", description = "소설 정보가 유효하지 않습니다..",
                    content = @Content(schema = @Schema(implementation = NovelNotFoundException.class)))
    })
    @GetMapping("/novel/{novelId}")
    public ApiCsResponse<GetCommentResponse> getCommentsOfNovel(@PathVariable Long novelId) {
        GetCommentResponse comments = commentService.getCommentsOfNovel(novelId);
        return ApiCsResponse.success(comments);
    }

    @Operation(summary = "회차의 댓글 불러오기", description = "한 회차에서 작성된 댓글 전부 불러오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 불러오기 성공",
                    content = @Content(schema = @Schema(implementation = GetCommentResponse.class))),
            @ApiResponse(
                    responseCode = "400", description = "회차 정보가 유효하지 않습니다..",
                    content = @Content(schema = @Schema(implementation = ChapterNotFoundException.class)))
    })
    @GetMapping("/chapter/{chapterId}")
    public ApiCsResponse<GetCommentResponse> getCommentsOfChapter(@PathVariable Long chapterId) {
        GetCommentResponse comments = commentService.getCommentsOfChapter(chapterId);
        return ApiCsResponse.success(comments);
    }

    @Operation(summary = "소설에 댓글 작성하기", description = "한 소설에 대한 댓글 작성하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "댓글 작성하기 성공",
                    content = @Content(schema = @Schema(implementation = CommentInfo.class))),
            @ApiResponse(
                    responseCode = "400", description = "작가 정보가 유효하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class))),
            @ApiResponse(
                    responseCode = "400", description = "댓글이 너무 깁니다.",
                    content = @Content(schema = @Schema(implementation = CommentLengthOutOfRangeException.class)))
    })
    @PostMapping("/novel/{novelId}")
    public ResponseEntity<ApiCsResponse<CommentInfo>> postCommentOnNovel(
            @PathVariable Long novelId,
            @Validated @RequestBody CommentContent commentContent
    ) {
        Long authorId = 500L;
        CommentInfo comment = commentService.writeCommentOnNovel(novelId, authorId, commentContent);

        ApiCsResponse<CommentInfo> response = ApiCsResponse.success(comment);
        URI redirectUri = URI.create("/novel/" + novelId);

        return ResponseEntity.created(redirectUri).body(response);
    }

    @Operation(summary = "회차에 댓글 작성하기", description = "한 회차에 대한 댓글 작성하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "댓글 작성하기 성공",
                    content = @Content(schema = @Schema(implementation = CommentInfo.class))),
            @ApiResponse(
                    responseCode = "400", description = "작가 정보가 유효하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class))),
            @ApiResponse(
                    responseCode = "400", description = "댓글이 너무 깁니다.",
                    content = @Content(schema = @Schema(implementation = CommentLengthOutOfRangeException.class)))
    })
    @PostMapping("/chapter/{chapterId}")
    public ResponseEntity<ApiCsResponse<CommentInfo>> postCommentOnChapter(
            @PathVariable Long chapterId,
            @Validated @RequestBody CommentContent commentContent
    ) {
        Long authorId = 500L;
        CommentInfo comment = commentService.writeCommentOnChapter(chapterId, authorId, commentContent);

        ApiCsResponse<CommentInfo> response = ApiCsResponse.success(comment);
        URI redirectUri = URI.create("/chapter/" + chapterId);

        return ResponseEntity
                .created(redirectUri)
                .body(response);
    }

    @Operation(summary = "내가 작성한 댓글 불러오기", description = "내가 작성한 모든 댓글 불러오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 불러오기 성공",
                    content = @Content(schema = @Schema(implementation = GetCommentResponse.class))),
            @ApiResponse(
                    responseCode = "400", description = "작가 정보가 유효하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class)))
    })
    @GetMapping("/author")
    public ApiCsResponse<GetCommentResponse> getCommentsOfAuthor() {
        Long authorId = 500L; // TODO: 토큰 에서 뺴올 예정
        GetCommentResponse comments = commentService.getCommentsByAuthor(authorId);
        return ApiCsResponse.success(comments);
    }

    @Operation(summary = "작성한 댓글 삭제하기", description = "내가 작성한 댓글 삭제하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "댓글 삭제하기 성공",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(
                    responseCode = "400", description = "작가 정보가 유효하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class))),
            @ApiResponse(
                    responseCode = "401", description = "작성자 본인이 아닙니다.",
                    content = @Content(schema = @Schema(implementation = AuthorNotAuthorizedException.class)))
    })
    @DeleteMapping("/{commentId}")
    public ApiCsResponse<Void> deleteComment(@PathVariable Long commentId) {
        Long authorId = 500L;
        commentService.deleteComment(commentId, authorId);
        return ApiCsResponse.success();
    }
}

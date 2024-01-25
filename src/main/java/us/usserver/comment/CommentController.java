package us.usserver.comment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import us.usserver.bookshelf.dto.NovelPreview;
import us.usserver.comment.dto.CommentContent;
import us.usserver.comment.dto.CommentInfo;
import us.usserver.comment.dto.GetCommentResponse;
import us.usserver.global.ApiCsResponse;
import us.usserver.global.exception.*;

import java.net.URI;
import java.util.List;

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
    public ResponseEntity<ApiCsResponse<?>> getCommentsOfNovel(@PathVariable Long novelId) {
        GetCommentResponse comments = commentService.getCommentsOfNovel(novelId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(comments)
                .build();
        return ResponseEntity.ok(response);
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
    public ResponseEntity<ApiCsResponse<?>> getCommentsOfChapter(@PathVariable Long chapterId) {
        GetCommentResponse comments = commentService.getCommentsOfChapter(chapterId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(comments)
                .build();
        return ResponseEntity.ok(response);
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
    public ResponseEntity<ApiCsResponse<?>> postCommentOnNovel(
            @PathVariable Long novelId,
            @Validated @RequestBody CommentContent commentContent
    ) {
        Long authorId = 0L;
        CommentInfo comment = commentService.writeCommentOnNovel(novelId, authorId, commentContent);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(comment)
                .build();
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
    public ResponseEntity<ApiCsResponse<?>> postCommentOnChapter(
            @PathVariable Long chapterId,
            @Validated @RequestBody CommentContent commentContent
    ) {
        Long authorId = 0L;
        CommentInfo comment = commentService.writeCommentOnChapter(chapterId, authorId, commentContent);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(comment)
                .build();
        URI redirectUri = URI.create("/chapter/" + chapterId);

        return ResponseEntity.created(redirectUri).body(response);
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
    public ResponseEntity<ApiCsResponse<?>> getCommentsOfAuthor() {
        Long authorId = 1L; // TODO: 토큰 에서 뺴올 예정
        GetCommentResponse comments = commentService.getCommentsByAuthor(authorId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(comments)
                .build();
        return ResponseEntity.ok(response);
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
    public ResponseEntity<ApiCsResponse<?>> deleteComment(@PathVariable Long commentId) {
        Long authorId = 0L;
        commentService.deleteComment(commentId, authorId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message(HttpStatus.NO_CONTENT.getReasonPhrase())
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }
}

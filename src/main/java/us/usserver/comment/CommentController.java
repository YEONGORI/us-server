package us.usserver.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import us.usserver.bookshelf.dto.NovelPreview;
import us.usserver.comment.dto.CommentContent;
import us.usserver.comment.dto.CommentInfo;
import us.usserver.global.ApiCsResponse;

import java.net.URI;
import java.util.List;

@ResponseBody
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/novel/{novelId}")
    public ResponseEntity<ApiCsResponse<?>> getCommentsOfNovel(@PathVariable Long novelId) {
        List<CommentInfo> comments = commentService.getCommentsOfNovel(novelId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(comments)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/chapter/{chapterId}")
    public ResponseEntity<ApiCsResponse<?>> getCommentsOfChapter(@PathVariable Long chapterId) {
        List<CommentInfo> comments = commentService.getCommentsOfChapter(chapterId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(comments)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/novel/{novelId}")
    public ResponseEntity<ApiCsResponse<?>> postCommentOnNovel(
            @PathVariable Long novelId,
            @Validated @RequestBody CommentContent commentContent
    ) {
        Long authorId = 0L;
        CommentInfo comment = commentService.writeCommentOnNovel(novelId, authorId, commentContent);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(comment)
                .build();
        URI redirectUri = URI.create("/novel/" + novelId);

        return ResponseEntity.created(redirectUri).body(response);
    }

    @PostMapping("/chapter/{chapterId}")
    public ResponseEntity<ApiCsResponse<?>> postCommentOnChapter(
            @PathVariable Long chapterId,
            @Validated @RequestBody CommentContent commentContent
    ) {
        Long authorId = 0L;
        CommentInfo comment = commentService.writeCommentOnChapter(chapterId, authorId, commentContent);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(comment)
                .build();
        URI redirectUri = URI.create("/chapter/" + chapterId);

        return ResponseEntity.created(redirectUri).body(response);
    }

    @GetMapping("/author")
    public ResponseEntity<ApiCsResponse<?>> getCommentsOfAuthor() {
        Long authorId = 0L; // TODO: 토큰 에서 뺴올 예정
        List<CommentInfo> comments = commentService.getCommentsByAuthor(authorId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(comments)
                .build();
        return ResponseEntity.ok(response);
    }

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

    @DeleteMapping("/chapter/{chapterId}")
    public ResponseEntity<ApiCsResponse<?>> deleteCommentOnChapter(@PathVariable Long chapterId) {
        Long authorId = 0L;
        commentService.deleteComment(chapterId, authorId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message(HttpStatus.NO_CONTENT.getReasonPhrase())
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }
}

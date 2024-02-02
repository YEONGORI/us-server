package us.usserver.like.novel;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.usserver.global.ApiCsResponse;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.global.exception.DuplicatedLikeException;

@Tag(name = "소설 좋아요(구독) API")
@ResponseBody
@RestController
@RequestMapping("/like/novel")
@RequiredArgsConstructor
public class NovelLikeController {
    private final NovelLikeService novelLikeService;

    @Operation(summary = "소설 좋아요", description = "소설 메인 옆 하트 누르기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요 성공"),
            @ApiResponse(
                    responseCode = "400", description = "작가 혹은 소설 정보가 유효하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class))),
            @ApiResponse(
                    responseCode = "400", description = "이미 좋아요를 누르셨습니다..",
                    content = @Content(schema = @Schema(implementation = DuplicatedLikeException.class)))
    })
    @PostMapping("/{novelId}")
    public ResponseEntity<ApiCsResponse<?>> setLike(@PathVariable Long novelId) {
        Long authorId = 500L; // TODO: 유저 정보는 토큰 에서 가져올 예정

        novelLikeService.setNovelLike(novelId, authorId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "소설 좋아요 취소", description = "소설 메인 옆 하트 다시 누르기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "좋아요 취소"),
            @ApiResponse(
                    responseCode = "400", description = "작가 혹은 소설 정보가 유효하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class))
            )
    })
    @DeleteMapping("/{novelId}")
    public ResponseEntity<ApiCsResponse<?>> deleteLike(@PathVariable Long novelId) {
        Long authorId = 500L; // TODO: 유저 정보는 토큰 에서 가져올 예정
        novelLikeService.deleteNovelLike(novelId, authorId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message(HttpStatus.NO_CONTENT.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}

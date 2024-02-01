package us.usserver.like.paragraph;


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

@Tag(name = "한줄 좋아요 API")
@ResponseBody
@RestController
@RequestMapping("/like/paragraph")
@RequiredArgsConstructor
public class ParagraphLikeController {
    private final ParagraphLikeService paragraphLikeService;

    @Operation(summary = "한줄 좋아요", description = "특정 한줄에 좋아요 누르기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요 성공"),
            @ApiResponse(
                    responseCode = "400", description = "작가 혹은 회차 정보가 유효하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class))
            ),
            @ApiResponse(
                    responseCode = "400", description = "이미 좋아요를 누르셨습니다.",
                    content = @Content(schema = @Schema(implementation = DuplicatedLikeException.class))
            )
    })
    @PostMapping("/{paragraphId}")
    public ResponseEntity<ApiCsResponse<?>> setLike(@PathVariable Long paragraphId) {
        Long authorId = 500L; // TODO: 유저 정보는 토큰 에서 가져올 예정

        paragraphLikeService.setParagraphLike(paragraphId, authorId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "한줄 좋아요 취소", description = "특정 한줄에 좋아요 취소하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "좋아요 취소"),
            @ApiResponse(
                    responseCode = "400", description = "작가 혹은 한줄 정보가 유효하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class))
            )
    })
    @DeleteMapping("/{paragraphId}")
    public ResponseEntity<ApiCsResponse<?>> deleteLike(@PathVariable Long paragraphId) {
        Long authorId = 500L; // TODO: 유저 정보는 토큰 에서 가져올 예정

        paragraphLikeService.deleteParagraphLike(paragraphId, authorId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message(HttpStatus.NO_CONTENT.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}

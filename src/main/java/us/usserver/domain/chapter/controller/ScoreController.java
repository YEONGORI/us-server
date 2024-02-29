package us.usserver.domain.chapter.controller;

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
import us.usserver.domain.chapter.service.ScoreService;
import us.usserver.global.ApiCsResponse;
import us.usserver.global.exception.ScoreOutOfRangeException;
import us.usserver.domain.chapter.dto.PostScore;

@Tag(name = "평점 API")
@ResponseBody
@RestController
@RequestMapping("/score")
@RequiredArgsConstructor
public class ScoreController {
    private final ScoreService scoreService;

    @Operation(summary = "챕터 평점 주기", description = "챕터에 대한 평점 1 ~ 10 까지 입력 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "평점을 1 ~ 10 사이로 입력해 주세요",
                    content = @Content(schema = @Schema(implementation = ScoreOutOfRangeException.class)))
    })
    @PostMapping("/{chapterId}")
    public ResponseEntity<ApiCsResponse<?>> setScore(
            @PathVariable Long chapterId,
            @Validated @RequestBody PostScore score
            ) {
        Long authorId = 500L;
        scoreService.postScore(chapterId, authorId, score);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}

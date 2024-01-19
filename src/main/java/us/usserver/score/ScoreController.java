package us.usserver.score;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import us.usserver.global.ApiCsResponse;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.global.exception.ChapterNotFoundException;
import us.usserver.global.exception.MainAuthorIsNotMatchedException;
import us.usserver.global.exception.ScoreOutOfRangeException;
import us.usserver.paragraph.dto.ParagraphInVoting;
import us.usserver.score.dto.PostScore;

@ResponseBody
@RestController
@RequestMapping("/score")
@RequiredArgsConstructor
public class ScoreController {
    private final ScoreService scoreService;
    
    @Operation(summary = "한줄 선택하기", description = "다음으로 추가될 한줄을 선정(메인 작가만 가능)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "선택 성공"),
            @ApiResponse(responseCode = "400", description = "회차가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ChapterNotFoundException.class))),
            @ApiResponse(responseCode = "400", description = "작가가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class))),
            @ApiResponse(responseCode = "400", description = "권한이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ScoreOutOfRangeException.class))),
    })
    @PostMapping("/{chapterId}")
    public ResponseEntity<ApiCsResponse<?>> getParagraphsInVoting(
            @PathVariable Long chapterId,
            @Validated @RequestBody PostScore score
            ) {
        Long authorId = 0L;
        scoreService.postScore(chapterId, authorId, score);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}

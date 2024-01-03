package us.usserver.score;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import us.usserver.global.ApiCsResponse;
import us.usserver.score.dto.PostScore;

@ResponseBody
@RestController
@RequestMapping("/score")
@RequiredArgsConstructor
public class ScoreController {
    private final ScoreService scoreService;

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

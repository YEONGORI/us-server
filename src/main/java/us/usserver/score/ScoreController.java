package us.usserver.score;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import us.usserver.global.ApiResponse;
import us.usserver.paragraph.dto.ParagraphInVoting;
import us.usserver.score.dto.PostScore;

import java.util.List;

@ResponseBody
@RestController
@RequestMapping("/score")
@RequiredArgsConstructor
public class ScoreController {
    private final ScoreService scoreService;

    @PostMapping("/{chapterId}")
    public ResponseEntity<ApiResponse<?>> getParagraphsInVoting(
            @PathVariable Long chapterId,
            @Validated @RequestBody PostScore score
            ) {
        Long authorId = 0L;
        scoreService.postScore(chapterId, authorId, score);

        ApiResponse<Object> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}

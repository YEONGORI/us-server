package us.usserver.vote;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.usserver.global.ApiCsResponse;
import us.usserver.paragraph.dto.ParagraphInVoting;

import java.util.List;

@ResponseBody
@RestController
@RequestMapping("/vote")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;

    @PostMapping("/{paragraphId}")
    public ResponseEntity<ApiCsResponse<?>> voting(@PathVariable Long paragraphId) {
        Long authorId = 0L;
        voteService.voting(paragraphId, authorId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message(HttpStatus.NO_CONTENT.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{voteId}")
    public ResponseEntity<ApiCsResponse<?>> cancelVote(@PathVariable Long voteId) {
        Long authorId = 0L;
        voteService.unvoting(voteId, authorId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message(HttpStatus.NO_CONTENT.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}

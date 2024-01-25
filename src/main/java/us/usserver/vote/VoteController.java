package us.usserver.vote;

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
import us.usserver.global.exception.AuthorNotAuthorizedException;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.global.exception.DuplicatedVoteException;
import us.usserver.global.exception.ParagraphNotFoundException;

@Tag(name = "투표 API")
@ResponseBody
@RestController
@RequestMapping("/vote")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;

    @Operation(summary = "투표 하기", description = "특정 한줄에 투표하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "투표 성공"),
            @ApiResponse(responseCode = "400", description = "작가가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class))),
            @ApiResponse(responseCode = "400", description = "회차가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ParagraphNotFoundException.class))),
            @ApiResponse(responseCode = "400", description = "이미 투표하셨습니다..",
                    content = @Content(schema = @Schema(implementation = DuplicatedVoteException.class))),
    })
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

    @Operation(summary = "투표 취소 하기", description = "본인 투표 취소 하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "취소 성공"),
            @ApiResponse(responseCode = "400", description = "작가가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class))),
            @ApiResponse(responseCode = "400", description = "회차가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ParagraphNotFoundException.class))),
            @ApiResponse(responseCode = "401", description = "본인이 아닙니다.",
                    content = @Content(schema = @Schema(implementation = AuthorNotAuthorizedException.class))),
    })
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

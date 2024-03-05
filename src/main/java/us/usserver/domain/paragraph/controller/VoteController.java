package us.usserver.domain.paragraph.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import us.usserver.domain.paragraph.service.VoteService;
import us.usserver.global.response.exception.AuthorNotAuthorizedException;
import us.usserver.global.response.exception.AuthorNotFoundException;
import us.usserver.global.response.exception.DuplicatedVoteException;
import us.usserver.global.response.exception.ParagraphNotFoundException;
import us.usserver.global.response.ApiCsResponse;

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
    public ApiCsResponse<Void> voting(@PathVariable Long paragraphId) {
        Long authorId = 500L;
        voteService.voting(paragraphId, authorId);
        return ApiCsResponse.success();
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
    public ApiCsResponse<Void> cancelVote(@PathVariable Long voteId) {
        Long authorId = 500L;
        voteService.unvoting(voteId, authorId);
        return ApiCsResponse.success();
    }
}

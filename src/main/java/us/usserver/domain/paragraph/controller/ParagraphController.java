package us.usserver.domain.paragraph.controller;

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
import us.usserver.domain.paragraph.dto.res.GetParagraphResponse;
import us.usserver.domain.paragraph.dto.req.PostParagraphReq;
import us.usserver.domain.paragraph.service.ParagraphService;
import us.usserver.global.response.ApiCsResponse;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.global.exception.ChapterNotFoundException;
import us.usserver.global.exception.MainAuthorIsNotMatchedException;
import us.usserver.domain.paragraph.dto.ParagraphInVoting;

import java.net.URI;

@Tag(name = "한줄 API")
@ResponseBody
@RestController
@RequestMapping("/paragraph")
@RequiredArgsConstructor
public class ParagraphController {
    private final ParagraphService paragraphService;

    @Operation(summary = "투표중인 한줄 불러오기", description = "뷰어에서 투표중인 한줄을 클릭하면 나오는 투표중 한줄 전부 불러오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "불러오기 성공",
                    content = @Content(schema = @Schema(implementation = GetParagraphResponse.class))),
            @ApiResponse(responseCode = "400", description = "회차가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ChapterNotFoundException.class)))
    })
    @GetMapping("/{chapterId}/voting")
    public ApiCsResponse<GetParagraphResponse> getParagraphsInVoting(@PathVariable Long chapterId) {
        GetParagraphResponse paragraphs = paragraphService.getInVotingParagraphs(chapterId);
        return ApiCsResponse.success(paragraphs);
    }

    @Operation(summary = "한줄 작성하기", description = "다음 한줄에 대한 내 의견 작성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "작성 성공",
                    content = @Content(schema = @Schema(implementation = ParagraphInVoting.class))),
            @ApiResponse(responseCode = "400", description = "회차가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ChapterNotFoundException.class))),
            @ApiResponse(responseCode = "400", description = "작가가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class))),
    })
    @PostMapping("/{chapterId}")
    public ResponseEntity<ApiCsResponse<ParagraphInVoting>> postParagraph(
            @PathVariable Long chapterId,
            @Validated @RequestBody PostParagraphReq req
    ) {
        Long authorId = 500L; // TODO: 토큰에서 author 정보 가져올 예정
        ParagraphInVoting paragraph = paragraphService.postParagraph(authorId, chapterId, req);
        ApiCsResponse<ParagraphInVoting> response = ApiCsResponse.success(paragraph);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "한줄 선택하기", description = "다음으로 추가될 한줄을 선정(메인 작가만 가능)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "작성 성공"),
            @ApiResponse(responseCode = "400", description = "회차가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ChapterNotFoundException.class))),
            @ApiResponse(responseCode = "406", description = "권한이 없습니다.",
                    content = @Content(schema = @Schema(implementation = MainAuthorIsNotMatchedException.class))),
    })
    @PatchMapping("/{novelId}/{chapterId}/{paragraphId}")
    public ResponseEntity<ApiCsResponse<Void>> selectParagraph(
            @PathVariable Long novelId, @PathVariable Long chapterId, @PathVariable Long paragraphId
    ) {
        Long authorId = 500L; // TODO: 토큰에서 author 정보 가져올 예정
        paragraphService.selectParagraph(authorId, novelId, chapterId, paragraphId);
        ApiCsResponse<Void> response = ApiCsResponse.success();
        URI redirectUri = URI.create("/paragraph/" + chapterId);
        return ResponseEntity.created(redirectUri).body(response);
    }

    @Operation(summary = "한줄 신고하기", description = "아직 기능 미확정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "작성 성공")
    })
    @PostMapping("/call/{paragraphId}") // 신고 하기
    public ResponseEntity<ApiCsResponse<Void>> reportParagraph(@PathVariable Long paragraphId) {
        Long authorId = 500L; // TODO: 토큰에서 author 정보 가져올 예정
        paragraphService.reportParagraph(authorId, paragraphId);
        ApiCsResponse<Void> response = ApiCsResponse.success();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}

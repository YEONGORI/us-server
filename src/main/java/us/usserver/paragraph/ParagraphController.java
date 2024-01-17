package us.usserver.paragraph;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import us.usserver.global.ApiCsResponse;
import us.usserver.paragraph.dto.ParagraphInVoting;
import us.usserver.paragraph.dto.PostParagraphReq;

import java.net.URI;
import java.util.List;

@ResponseBody
@RestController
@RequestMapping("/paragraph")
@RequiredArgsConstructor
public class ParagraphController {
    private final ParagraphService paragraphService;

    // TODO: ? 왜 썻는지 Object가 필요한지 고민하기
    @GetMapping("/{chapterId}/voting")
    public ResponseEntity<ApiCsResponse<?>> getParagraphsInVoting(@PathVariable Long chapterId) {
        List<ParagraphInVoting> paragraphs = paragraphService.getInVotingParagraphs(chapterId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(paragraphs)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{chapterId}")
    public ResponseEntity<ApiCsResponse<?>> postParagraph(
            @PathVariable Long chapterId,
            @Validated @RequestBody PostParagraphReq req
    ) {
        Long authorId = 0L; // TODO: 토큰에서 author 정보 가져올 예정
        ParagraphInVoting paragraph = paragraphService.postParagraph(authorId, chapterId, req);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(paragraph)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{novelId}/{chapterId}/{paragraphId}")
    public ResponseEntity<ApiCsResponse<?>> selectParagraph(
            @PathVariable Long novelId, @PathVariable Long chapterId, @PathVariable Long paragraphId
    ) {
        Long authorId = 0L; // TODO: 토큰에서 author 정보 가져올 예정
        paragraphService.selectParagraph(authorId, novelId, chapterId, paragraphId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(null)
                .build();
        URI redirectUri = URI.create("/paragraph/" + chapterId);

        return ResponseEntity.created(redirectUri).body(response);
    }

    @PostMapping("/{paragraphId}") // 신고 하기
    public ResponseEntity<ApiCsResponse<?>> reportParagraph(@PathVariable Long paragraphId) {
        Long authorId = 0L; // TODO: 토큰에서 author 정보 가져올 예정
        paragraphService.reportParagraph(authorId, paragraphId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}

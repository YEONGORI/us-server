package us.usserver.paragraph;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import us.usserver.ApiResponse;
import us.usserver.paragraph.dto.ParagraphInfo;
import us.usserver.paragraph.dto.ParagraphUnSelected;
import us.usserver.paragraph.dto.PostParagraphReq;

import java.net.URI;

@ResponseBody
@RestController
@RequestMapping("/paragraph")
@RequiredArgsConstructor
public class ParagraphController {
    private final ParagraphService paragraphService;

    @GetMapping("/{chapterId}")
    public ResponseEntity<ApiResponse<?>> getParagraphs(@PathVariable Long chapterId) {
        Long authorId = 0L; // TODO: 토큰에서 author 정보 가져올 예정
        ParagraphInfo paragraphs = paragraphService.getParagraphs(authorId, chapterId);

        ApiResponse<Object> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(paragraphs)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{chapterId}")
    public ResponseEntity<ApiResponse<?>> postParagraph(
            @PathVariable Long chapterId,
            @Validated @RequestBody PostParagraphReq req
    ) {
        Long authorId = 0L; // TODO: 토큰에서 author 정보 가져올 예정
        ParagraphUnSelected paragraph = paragraphService.postParagraph(authorId, chapterId, req);

        ApiResponse<Object> response = ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(paragraph)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{novelId}/{chapterId}/{paragraphId}")
    public ResponseEntity<ApiResponse<?>> selectParagraph(
            @PathVariable Long novelId, @PathVariable Long chapterId, @PathVariable Long paragraphId
    ) {
        Long authorId = 0L; // TODO: 토큰에서 author 정보 가져올 예정
        paragraphService.selectParagraph(authorId, novelId, chapterId, paragraphId);

        ApiResponse<Object> response = ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.created(URI.create("http://localhost:8080/paragraph/" + chapterId)).body(response);
    }
}

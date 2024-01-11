package us.usserver.note;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import us.usserver.bookshelf.dto.NovelPreview;
import us.usserver.global.ApiCsResponse;
import us.usserver.note.dto.ParagraphPreview;

import java.util.List;

@ResponseBody
@RestController
@RequestMapping("/notebook")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    @GetMapping("/viewed") // TODO: 내가 쓴 글
    public ResponseEntity<ApiCsResponse<?>> wroteParagraphs() {
        Long authorId = 0L;
        List<ParagraphPreview> paragraphPreviews = noteService.wroteParagraphs(authorId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(paragraphPreviews)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/voted") // TODO: 내가 투표한 글
    public ResponseEntity<ApiCsResponse<?>> votedParagraphs() {
        Long authorId = 0L;
        List<ParagraphPreview> paragraphPreviews = noteService.votedParagraphs(authorId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(paragraphPreviews)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/liked") // TODO: 내가 좋아요한 글
    public ResponseEntity<ApiCsResponse<?>> likedNovels() {
        Long authorId = 0L;
        List<ParagraphPreview> paragraphPreviews = noteService.likedParagraphs(authorId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(paragraphPreviews)
                .build();
        return ResponseEntity.ok(response);
    }
}

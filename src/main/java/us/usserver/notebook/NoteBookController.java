package us.usserver.notebook;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import us.usserver.bookshelf.dto.NovelPreview;
import us.usserver.global.ApiCsResponse;

import java.util.List;

@ResponseBody
@RestController
@RequestMapping("/bookshelf")
@RequiredArgsConstructor
public class NoteBookController {
    private final NoteBookService noteBookService;

    @GetMapping("/viewed") // 내가 최근에 본 소설
    public ResponseEntity<ApiCsResponse<?>> recentViewedNovels() {
        Long authorId = 0L; // TODO: Token 으로 교체 예정
        List<NovelPreview> novelPreviews = noteBookService.recentViewedNovels(authorId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(novelPreviews)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/created") // 내가 생성한 소설
    public ResponseEntity<ApiCsResponse<?>> createdNovels() {
        Long authorId = 0L; // TODO: Token 으로 교체 예정
        List<NovelPreview> novelPreviews = noteBookService.createdNovels(authorId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(novelPreviews)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/joined") // 내가 참여한 소설
    public ResponseEntity<ApiCsResponse<?>> joinedNovels() {
        Long authorId = 0L; // TODO: Token 으로 교체 예정
        List<NovelPreview> novelPreviews = noteBookService.joinedNovels(authorId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(novelPreviews)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/liked") // 내가 좋아요 한 소설
    public ResponseEntity<ApiCsResponse<?>> likedNovels() {
        Long authorId = 0L; // TODO: Token 으로 교체 예정
        List<NovelPreview> novelPreviews = noteBookService.likedNovels(authorId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(novelPreviews)
                .build();
        return ResponseEntity.ok(response);
    }

    //-------------------글-----------------//
    // TODO: 내가 쓴 글

    // TODO: 내가 투표한 글

    // TODO: 내가 좋아요 한 글

    // TODO: 내가 쓴 댓글
}

package us.usserver.bookshelf;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.usserver.bookshelf.dto.NovelPreview;
import us.usserver.global.ApiCsResponse;

import java.util.List;

@ResponseBody
@RestController
@RequestMapping("/bookshelf")
@RequiredArgsConstructor
public class BookshelfController {
    private final BookshelfService bookshelfService;

    @GetMapping("/viewed") // 내가 최근에 본 소설
    public ResponseEntity<ApiCsResponse<?>> recentViewedNovels() {
        Long authorId = 0L; // TODO: Token 으로 교체 예정
        List<NovelPreview> novelPreviews = bookshelfService.recentViewedNovels(authorId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(novelPreviews)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/viewed/{novelId}") // 내가 최근에 본 소설
    public ResponseEntity<ApiCsResponse<?>> deleteRecentViewedNovels(@PathVariable Long novelId) {
        Long authorId = 0L; // TODO: Token 으로 교체 예정
        bookshelfService.deleteRecentViewedNovels(authorId, novelId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message(HttpStatus.NO_CONTENT.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/created") // 내가 생성한 소설
    public ResponseEntity<ApiCsResponse<?>> createdNovels() {
        Long authorId = 0L; // TODO: Token 으로 교체 예정
        List<NovelPreview> novelPreviews = bookshelfService.createdNovels(authorId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(novelPreviews)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/created/{novelId}") // 내가 생성한 소설
    public ResponseEntity<ApiCsResponse<?>> deleteCreatedNovels(@PathVariable Long novelId) {
        Long authorId = 0L; // TODO: Token 으로 교체 예정
        bookshelfService.deleteCreatedNovels(authorId, novelId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message(HttpStatus.NO_CONTENT.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/joined") // 내가 참여한 소설
    public ResponseEntity<ApiCsResponse<?>> joinedNovels() {
        Long authorId = 0L; // TODO: Token 으로 교체 예정
        List<NovelPreview> novelPreviews = bookshelfService.joinedNovels(authorId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(novelPreviews)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/joined/{novelId}") // 내가 참여한 소설
    public ResponseEntity<ApiCsResponse<?>> deleteJoinedNovels(@PathVariable Long novelId) {
        Long authorId = 0L; // TODO: Token 으로 교체 예정
        bookshelfService.deleteJoinedNovels(authorId, novelId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message(HttpStatus.NO_CONTENT.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/liked") // 내가 좋아요 한 소설
    public ResponseEntity<ApiCsResponse<?>> likedNovels() {
        Long authorId = 0L; // TODO: Token 으로 교체 예정
        List<NovelPreview> novelPreviews = bookshelfService.likedNovels(authorId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(novelPreviews)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/liked/{novelId}") // 내가 좋아요 한 소설
    public ResponseEntity<ApiCsResponse<?>> deleteLikedNovels(@PathVariable Long novelId) {
        Long authorId = 0L; // TODO: Token 으로 교체 예정
        bookshelfService.deleteLikedNovels(authorId, novelId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message(HttpStatus.NO_CONTENT.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}

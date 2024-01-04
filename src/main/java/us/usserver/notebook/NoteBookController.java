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
@RequestMapping("/notebook")
@RequiredArgsConstructor
public class NoteBookController {
//    private final NoteBookService noteBookService;

    @GetMapping("/viewed") // TODO: 내가 쓴 글
    public ResponseEntity<ApiCsResponse<?>> recentViewedNovels() {
//        Long authorId = 0L; // TODO: Token 으로 교체 예정
//        List<NovelPreview> novelPreviews = noteBookService.recentViewedNovels(authorId);
//
//        ApiCsResponse<Object> response = ApiCsResponse.builder()
//                .status(HttpStatus.OK.value())
//                .message(HttpStatus.OK.getReasonPhrase())
//                .data(novelPreviews)
//                .build();
//        return ResponseEntity.ok(response);
        return null;
    }

    @GetMapping("/joined") // TODO: 내가 투표한 글
    public ResponseEntity<ApiCsResponse<?>> joinedNovels() {
//        Long authorId = 0L; // TODO: Token 으로 교체 예정
//        List<NovelPreview> novelPreviews = noteBookService.joinedNovels(authorId);
//
//        ApiCsResponse<Object> response = ApiCsResponse.builder()
//                .status(HttpStatus.OK.value())
//                .message(HttpStatus.OK.getReasonPhrase())
//                .data(novelPreviews)
//                .build();
//        return ResponseEntity.ok(response);
        return null;
    }

}

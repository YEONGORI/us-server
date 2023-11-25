package us.usserver.chapter;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.usserver.ApiResponse;
import us.usserver.chapter.dto.ChaptersOfNovel;

import java.util.List;

@ResponseBody
@RestController
@RequestMapping("/chapter")
@RequiredArgsConstructor
public class ChapterController {
    private final ChapterService chapterService;

    @GetMapping("/{novelId}")
    public ResponseEntity<ApiResponse<?>> getChapters(@PathVariable Long novelId) {
        List<ChaptersOfNovel> chapters = chapterService.getChaptersOfNovel(novelId);
        ApiResponse<Object> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(chapters)
                .build();
        return ResponseEntity.ok(response);
    }

    // TODO: 이전 회차의 권한 설정을 가져올 수 있는 기능을 제공 해야 하는데, 이 권한을 저장 하는 엔티티가 없어서 생성 해야함
    @PostMapping("/{novelId}")
    public ResponseEntity<ApiResponse<?>> createChapter(
            @PathVariable Long novelId
    ) {
        Long authorId = 0L; // TODO: 토큰에서 가져올 예정
        
        chapterService.createChapter(novelId, authorId);
        ApiResponse<Object> response = ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}

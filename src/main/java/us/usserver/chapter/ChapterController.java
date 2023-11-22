package us.usserver.chapter;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.usserver.ApiResponse;
import us.usserver.chapter.dto.ChapterDetailResponse;
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

    @GetMapping("/{novelId}/{chapterId}")
    public ResponseEntity<ApiResponse<?>> getChapterDetail(
            @PathVariable Long novelId, @PathVariable Long chapterId) {
        ChapterDetailResponse detail = chapterService.getChapterDetail(novelId, chapterId);
        ApiResponse<Object> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(detail)
                .build();
        return ResponseEntity.ok(response);
    }
}

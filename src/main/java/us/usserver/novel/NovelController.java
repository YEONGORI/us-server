package us.usserver.novel;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.usserver.ApiResponse;
import us.usserver.novel.dto.DetailInfoResponse;
import us.usserver.novel.dto.NovelInfoResponse;

@ResponseBody
@RestController
@RequestMapping("/novel")
@RequiredArgsConstructor
public class NovelController {
    private final NovelService novelService;

    @GetMapping("/{novelId}")
    public ResponseEntity<ApiResponse<?>> getNovelInfo(@PathVariable Long novelId) {
        NovelInfoResponse novelInfo = novelService.getNovelInfo(novelId);
        ApiResponse<Object> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(novelInfo)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{novelId}/detail")
    public ResponseEntity<ApiResponse<?>> getNovelDetailInfo(@PathVariable Long novelId) {
        DetailInfoResponse detailInfo = novelService.getNovelDetailInfo(novelId);
        ApiResponse<Object> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(detailInfo)
                .build();
        return ResponseEntity.ok(response);
    }
}

package us.usserver.novel;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import us.usserver.global.ApiResponse;
import us.usserver.novel.dto.AuthorDescription;
import us.usserver.novel.dto.DetailInfoResponse;
import us.usserver.novel.dto.NovelInfoResponse;
import us.usserver.novel.dto.NovelSynopsis;

import java.net.URI;

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

    @PatchMapping("/{novelId}/synopsis")
    public ResponseEntity<ApiResponse<?>> modifyNovelSynopsis(
            @PathVariable Long novelId,
            @Validated @RequestBody NovelSynopsis req
    ) {
        Long authorId = 0L; // TODO: 토큰에서 author 정보 가져올 예정
        NovelSynopsis synopsis = novelService.modifyNovelSynopsis(novelId, authorId, req);

        ApiResponse<Object> response = ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(synopsis)
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{novelId}/author-description")
    public ResponseEntity<ApiResponse<?>> modifyAuthorDescription(
            @PathVariable Long novelId,
            @Validated @RequestBody AuthorDescription req
    ) {
        Long authorId = 0L; // TODO: 토큰에서 author 정보 가져올 예정
        AuthorDescription description = novelService.modifyAuthorDescription(novelId, authorId, req);

        ApiResponse<Object> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(description)
                .build();
        return ResponseEntity.ok(response);
    }
}

package us.usserver.novel;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.usserver.global.ApiResponse;
import us.usserver.novel.dto.*;

@ResponseBody
@RestController
@RequestMapping("/novel")
@RequiredArgsConstructor
public class NovelController {
    private final NovelService novelService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createNovel(@Valid @RequestBody CreateNovelReq createNovelReq) {
        Novel novel = novelService.createNovel(createNovelReq);
        ApiResponse<Object> response = ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(novel)
                .build();
        return ResponseEntity.ok(response);
    }

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

    @GetMapping("/main")
    //TODO: 추후에 security+jwt 적용시 URL 변경 예정
    public ResponseEntity<ApiResponse<?>> getHomeNovelListInfo() {
        Long authorId = 1L;

        HomeNovelListResponse homeNovelList = novelService.homeNovelInfo(authorId);
        ApiResponse<Object> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(homeNovelList)
                .build();
        return ResponseEntity.ok(response);
    }

    //TODO: 경로 고민중..
    @GetMapping("/main/more")
    public ResponseEntity<ApiResponse<?>> moreNovel(@Valid MoreInfoOfNovel novelMoreDto) {
        NovelPageInfoResponse novelPageInfoResponse = novelService.moreNovel(novelMoreDto);
        ApiResponse<Object> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(novelPageInfoResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> searchNovel(@Valid SearchNovelReq searchNovelReq) {
        NovelPageInfoResponse novelPageInfoResponse = novelService.searchNovel(searchNovelReq);
        ApiResponse<Object> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(novelPageInfoResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search-keyword")
    public ResponseEntity<ApiResponse<?>> getSearchWord() {
        SearchKeywordResponse searchWordResponse = novelService.searchKeyword();
        ApiResponse<Object> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(searchWordResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/search-keyword")
    public ResponseEntity<ApiResponse<?>> deleteAllSearchWord() {
        novelService.deleteSearchKeyword();
        ApiResponse<Object> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}

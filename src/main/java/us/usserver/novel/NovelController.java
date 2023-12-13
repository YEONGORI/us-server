package us.usserver.novel;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.usserver.global.ApiCsResponse;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.novel.dto.*;

@ResponseBody
@RestController
@RequestMapping("/novel")
@RequiredArgsConstructor
public class NovelController {
    private final NovelService novelService;

    //TODO: Swagger 공통적인 ApiResponse 적용 방법 찾아보기!

    @Operation(summary = "소설 생성", description = "작가가 소설을 생성하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "파티 생성 성공",
            content = @Content(schema = @Schema(implementation = Novel.class))),
            @ApiResponse(responseCode = "400", description = "작가가 존재하지 않습니다.",
            content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class)))
    })
    @PostMapping
    public ResponseEntity<ApiCsResponse<?>> createNovel(@Valid @RequestBody CreateNovelReq createNovelReq) {
        Novel novel = novelService.createNovel(createNovelReq);
        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(novel)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{novelId}")
    public ResponseEntity<ApiCsResponse<?>> getNovelInfo(@PathVariable Long novelId) {
        NovelInfoResponse novelInfo = novelService.getNovelInfo(novelId);
        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(novelInfo)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{novelId}/detail")
    public ResponseEntity<ApiCsResponse<?>> getNovelDetailInfo(@PathVariable Long novelId) {
        DetailInfoResponse detailInfo = novelService.getNovelDetailInfo(novelId);
        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(detailInfo)
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "우스 메인 홈", description = "메인 페이지 소설을 불러오는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "소설 메인 페이지 load 성공",
            content = @Content(schema = @Schema(implementation = HomeNovelListResponse.class))),
            @ApiResponse(responseCode = "400", description = "작가가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class)))
    })
    @GetMapping("/main")
    //TODO: 추후에 security+jwt 적용시 URL 변경 예정
    public ResponseEntity<ApiCsResponse<?>> getHomeNovelListInfo() {
        HomeNovelListResponse homeNovelList = novelService.homeNovelInfo();
        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(homeNovelList)
                .build();
        return ResponseEntity.ok(response);
    }

    //TODO: 경로 고민중..
    @Operation(summary = "소설 더보기", description = "실시간 업데이트, 신작 모아보기 더보기 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "소설 더보기 load 성공",
                    content = @Content(schema = @Schema(implementation = NovelPageInfoResponse.class)))
    })
    @GetMapping("/main/more")
    public ResponseEntity<ApiCsResponse<?>> moreNovel(@Valid MoreInfoOfNovel moreInfoOfNovel) {
        NovelPageInfoResponse novelPageInfoResponse = novelService.moreNovel(moreInfoOfNovel);
        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(novelPageInfoResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "읽은 소설 더보기", description = "내가 읽었던 소설 더보기 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "읽은 소설 더보기 laod 성공",
                    content = @Content(schema = @Schema(implementation = NovelPageInfoResponse.class)))
    })
    @GetMapping("/main/more/read")
    public ResponseEntity<ApiCsResponse<?>> readNovel(@Valid ReadInfoOfNovel readInfoOfNovel) {
        NovelPageInfoResponse novelPageInfoResponse = novelService.readMoreNovel(readInfoOfNovel);
        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(novelPageInfoResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "소설 검색", description = "사용자 소설 검색 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "소설 검색 성공",
            content = @Content(schema = @Schema(implementation = NovelPageInfoResponse.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<ApiCsResponse<?>> searchNovel(@Valid SearchNovelReq searchNovelReq) {
        NovelPageInfoResponse novelPageInfoResponse = novelService.searchNovel(searchNovelReq);
        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(novelPageInfoResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "검색 페이지 Keyword", description = "인기검색어, 최근 검색어 목록 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색 Keyword load 성공",
            content = @Content(schema = @Schema(implementation = SearchKeywordResponse.class)))
    })
    @GetMapping("/search-keyword")
    public ResponseEntity<ApiCsResponse<?>> getSearchWord() {
        SearchKeywordResponse searchWordResponse = novelService.searchKeyword();
        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(searchWordResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "검색 페이지 Keyword Delete", description = "인기 검색어, 최근 검색어 목록 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색 Keyword Delete 성공")
    })
    @DeleteMapping("/search-keyword")
    public ResponseEntity<ApiCsResponse<?>> deleteAllSearchWord() {
        novelService.deleteSearchKeyword();
        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}

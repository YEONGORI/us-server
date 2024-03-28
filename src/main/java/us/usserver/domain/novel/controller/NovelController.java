package us.usserver.domain.novel.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import us.usserver.domain.member.entity.Member;
import us.usserver.domain.novel.dto.*;
import us.usserver.domain.novel.service.NovelService;
import us.usserver.global.response.ApiCsResponse;

import java.net.URI;

@Tag(name = "소설 API")
@ResponseBody
@RestController
@RequestMapping("/novel")
@RequiredArgsConstructor
public class NovelController {
    private final NovelService novelService;

    @Operation(summary = "소설 생성", description = "작가가 소설을 생성하는 API")
    @ApiResponse(responseCode = "201", description = "파티 생성 성공", content = @Content(schema = @Schema(implementation = NovelInfo.class)))
    @PostMapping
    public ResponseEntity<ApiCsResponse<NovelInfo>> createNovel(
            @AuthenticationPrincipal Long memberId,
            @Valid @RequestBody NovelBlueprint novelBlueprint
    ) {
        NovelInfo novelInfo = novelService.createNovel(memberId, novelBlueprint);

        ApiCsResponse<NovelInfo> response = ApiCsResponse.success(novelInfo);
        return ResponseEntity.created(URI.create("")).body(response);
    }

    @Operation(summary = "소설 정보 조회", description = "소설 기본 정보 조회")
    @ApiResponse(responseCode = "200", description = "소설 메인 페이지 조회",
            content = @Content(schema = @Schema(implementation = NovelDetailInfo.class)))
    @GetMapping("/{novelId}")
    public ApiCsResponse<NovelInfo> getNovelInfo(@PathVariable Long novelId) {
        NovelInfo novelInfo = novelService.getNovelInfo(novelId);
        return ApiCsResponse.success(novelInfo);
    }

    @Operation(summary = "소설 상세 정보 조회", description = "소설 상세 정보 조회(오른쪽 탭)")
    @ApiResponse(responseCode = "200", description = "소설 상세 페이지 조회",
            content = @Content(schema = @Schema(implementation = NovelDetailInfo.class)))
    @GetMapping("/{novelId}/detail")
    public ApiCsResponse<NovelDetailInfo> getNovelDetailInfo(@PathVariable Long novelId) {
        NovelDetailInfo detailInfo = novelService.getNovelDetailInfo(novelId);
        return ApiCsResponse.success(detailInfo);
    }

    @Operation(summary = "소설 줄거리 수정", description = "소설 줄거리 수정하기")
    @ApiResponse(responseCode = "201", description = "줄거리 수정 성공",
            content = @Content(schema = @Schema(implementation = NovelSynopsis.class)))
    @PatchMapping("/{novelId}/synopsis")
    public ResponseEntity<ApiCsResponse<String>> modifyNovelSynopsis(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long novelId,
            @Validated @RequestBody NovelSynopsis req
    ) {
        String synopsis = novelService.modifyNovelSynopsis(novelId, memberId, req.getSynopsis());
        ApiCsResponse<String> response = ApiCsResponse.success(synopsis);
        return ResponseEntity.created(URI.create("")).body(response);
    }

    @Operation(summary = "우스 메인 홈", description = "메인 페이지 소설을 불러오는 API")
    @ApiResponse(responseCode = "200", description = "소설 메인 페이지 load 성공",
            content = @Content(schema = @Schema(implementation = MainPageResponse.class)))
    @GetMapping("/main")
    public ApiCsResponse<MainPageResponse> getHomeNovelListInfo(
            @AuthenticationPrincipal Long memberId
    ) {
        MainPageResponse homeNovelList = novelService.getMainPage(memberId);
        return ApiCsResponse.success(homeNovelList);
    }

    @Operation(summary = "소설 더보기", description = "실시간 업데이트, 신작 모아보기 더보기 API")
    @ApiResponse(responseCode = "200", description = "소설 더보기 load 성공",
            content = @Content(schema = @Schema(implementation = NovelPageInfoResponse.class)))
    @GetMapping("/main/more")
    public ApiCsResponse<MoreNovelResponse> getMoreNovels(
            @AuthenticationPrincipal Long memberId,
            @Valid MoreNovelRequest moreNovelRequest
    ) {
        MoreNovelResponse moreNovels = novelService.getMoreNovels(memberId, moreNovelRequest);
        return ApiCsResponse.success(moreNovels);
    }

    @Operation(summary = "읽은 소설 더보기", description = "내가 읽었던 소설 더보기 API")
    @ApiResponse(responseCode = "200", description = "읽은 소설 더보기 laod 성공",
            content = @Content(schema = @Schema(implementation = NovelPageInfoResponse.class)))
    @GetMapping("/main/more/read")
    public ApiCsResponse<MoreNovelResponse> readNovel(
            @AuthenticationPrincipal Long memberId
    ) {
        MoreNovelResponse moreNovelResponse = novelService.readMoreNovel(memberId);
        return ApiCsResponse.success(moreNovelResponse);

    }

    @Operation(summary = "소설 검색", description = "사용자 소설 검색 API")
    @ApiResponse(responseCode = "200", description = "소설 검색 성공",
            content = @Content(schema = @Schema(implementation = NovelPageInfoResponse.class)))
    @GetMapping("/search")
    public ApiCsResponse<NovelPageInfoResponse> searchNovel(
            @AuthenticationPrincipal Long memberId,
            @Valid SearchNovelReq searchNovelReq
    ) {
        NovelPageInfoResponse novelPageInfoResponse = novelService.searchNovel(memberId, searchNovelReq);
        return ApiCsResponse.success(novelPageInfoResponse);
    }

    @Operation(summary = "작가 소개 수정", description = "메인 작가 소개글 수정하기")
    @ApiResponse(responseCode = "200", description = "소설 검색 성공",
            content = @Content(schema = @Schema(implementation = AuthorDescription.class)))
    @PatchMapping("/{novelId}/author-description")
    public ApiCsResponse<AuthorDescription> modifyAuthorDescription(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long novelId,
            @Validated @RequestBody AuthorDescription req
    ) {
        AuthorDescription description = novelService.modifyAuthorDescription(novelId, memberId, req);
        return ApiCsResponse.success(description);
    }

    @Operation(summary = "검색 페이지 Keyword", description = "인기검색어, 최근 검색어 목록 조회 API")
    @ApiResponse(responseCode = "200", description = "검색 Keyword load 성공",
            content = @Content(schema = @Schema(implementation = SearchKeywordResponse.class)))
    @GetMapping("/search-keyword")
    public ApiCsResponse<SearchKeywordResponse> getSearchWord(@AuthenticationPrincipal Member member) {
        SearchKeywordResponse searchWordResponse = novelService.searchKeyword(member);
        return ApiCsResponse.success(searchWordResponse);
    }

    
    @Operation(summary = "검색 페이지 Keyword Delete", description = "인기 검색어, 최근 검색어 목록 삭제 API")
    @ApiResponse(responseCode = "200", description = "검색 Keyword Delete 성공")
    @DeleteMapping("/search-keyword")
    public ApiCsResponse<Void> deleteAllSearchWord(@AuthenticationPrincipal Member member) {
        novelService.deleteSearchKeyword(member);
        return ApiCsResponse.success();
    }
}

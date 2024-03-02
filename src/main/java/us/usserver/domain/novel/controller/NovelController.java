package us.usserver.domain.novel.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import us.usserver.domain.member.entity.Member;
import us.usserver.domain.novel.dto.AuthorDescription;
import us.usserver.domain.novel.dto.CreateNovelReq;
import us.usserver.domain.novel.dto.HomeNovelListResponse;
import us.usserver.domain.novel.dto.MoreInfoOfNovel;
import us.usserver.domain.novel.dto.NovelDetailInfo;
import us.usserver.domain.novel.dto.NovelInfo;
import us.usserver.domain.novel.dto.NovelPageInfoResponse;
import us.usserver.domain.novel.dto.NovelSynopsis;
import us.usserver.domain.novel.dto.ReadInfoOfNovel;
import us.usserver.domain.novel.dto.SearchKeywordResponse;
import us.usserver.domain.novel.dto.SearchNovelReq;
import us.usserver.domain.novel.service.NovelService;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.global.exception.MainAuthorIsNotMatchedException;
import us.usserver.global.exception.NovelNotFoundException;
import us.usserver.global.response.ApiCsResponse;

@Tag(name = "소설 API")
@ResponseBody
@RestController
@RequestMapping("/novel")
@RequiredArgsConstructor
public class NovelController {
    //TODO: Swagger 공통적인 UsApiResponse 적용 방법 찾아보기!
    private final NovelService novelService;

    @Operation(summary = "소설 생성", description = "작가가 소설을 생성하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "파티 생성 성공", content = @Content(schema = @Schema(implementation = NovelInfo.class))),
            @ApiResponse(responseCode = "400", description = "작가가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class)))
    })
    @PostMapping
    public ApiCsResponse<NovelInfo> createNovel(@AuthenticationPrincipal Member member, @Valid @RequestBody CreateNovelReq createNovelReq) {
        NovelInfo novelInfo = novelService.createNovel(member, createNovelReq);
        return ApiCsResponse.success(novelInfo);
    }

    @Operation(summary = "소설 정보 조회", description = "소설 기본 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "소설 메인 페이지 조회",
                    content = @Content(schema = @Schema(implementation = NovelDetailInfo.class))),
            @ApiResponse(responseCode = "400", description = "소설이 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = NovelNotFoundException.class)))
    })
    @GetMapping("/{novelId}")
    public ApiCsResponse<NovelInfo> getNovelInfo(@PathVariable Long novelId) {
        NovelInfo novelInfo = novelService.getNovelInfo(novelId);
        return ApiCsResponse.success(novelInfo);
    }

    @Operation(summary = "소설 상세 정보 조회", description = "소설 상세 정보 조회(오른쪽 탭)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "소설 상세 페이지 조회",
                    content = @Content(schema = @Schema(implementation = NovelDetailInfo.class))),
            @ApiResponse(responseCode = "400", description = "소설이 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = NovelNotFoundException.class)))
    })
    @GetMapping("/{novelId}/detail")
    public ApiCsResponse<NovelDetailInfo> getNovelDetailInfo(@PathVariable Long novelId) {
        NovelDetailInfo detailInfo = novelService.getNovelDetailInfo(novelId);
        return ApiCsResponse.success(detailInfo);
    }

    @Operation(summary = "소설 줄거리 수정", description = "소설 줄거리 수정하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "줄거리 수정 성공",
                    content = @Content(schema = @Schema(implementation = NovelSynopsis.class))),
            @ApiResponse(responseCode = "400", description = "소설이 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = NovelNotFoundException.class))),
            @ApiResponse(responseCode = "406", description = "메인 작가가 아닙니다.",
                    content = @Content(schema = @Schema(implementation = MainAuthorIsNotMatchedException.class)))
    })
    @PatchMapping("/{novelId}/synopsis")
    public ResponseEntity<ApiCsResponse<String>> modifyNovelSynopsis(
            @PathVariable Long novelId,
            @Validated @RequestBody NovelSynopsis req
    ) {
        Long authorId = 500L; // TODO: 토큰에서 author 정보 가져올 예정
        String synopsis = novelService.modifyNovelSynopsis(novelId, authorId, req.getSynopsis());
        ApiCsResponse<String> response = ApiCsResponse.success(synopsis);
        return ResponseEntity.created(URI.create("")).body(response);
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
    public ApiCsResponse<HomeNovelListResponse> getHomeNovelListInfo(@AuthenticationPrincipal Member member) {
        HomeNovelListResponse homeNovelList = novelService.homeNovelInfo(member);
        return ApiCsResponse.success(homeNovelList);
    }

    //TODO: 경로 고민중..
    @Operation(summary = "소설 더보기", description = "실시간 업데이트, 신작 모아보기 더보기 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "소설 더보기 load 성공",
                    content = @Content(schema = @Schema(implementation = NovelPageInfoResponse.class)))
    })
    @GetMapping("/main/more")
    public ApiCsResponse<NovelPageInfoResponse> moreNovel(@Valid MoreInfoOfNovel moreInfoOfNovel) {
        NovelPageInfoResponse novelPageInfoResponse = novelService.moreNovel(moreInfoOfNovel);
        return ApiCsResponse.success(novelPageInfoResponse);
    }

    @Operation(summary = "읽은 소설 더보기", description = "내가 읽었던 소설 더보기 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "읽은 소설 더보기 laod 성공",
                    content = @Content(schema = @Schema(implementation = NovelPageInfoResponse.class)))
    })
    @GetMapping("/main/more/read")
    public ApiCsResponse<NovelPageInfoResponse> readNovel(@AuthenticationPrincipal Member member,
                                                      @Valid ReadInfoOfNovel readInfoOfNovel) {
        NovelPageInfoResponse novelPageInfoResponse = novelService.readMoreNovel(member, readInfoOfNovel);
        return ApiCsResponse.success(novelPageInfoResponse);
    }

    @Operation(summary = "소설 검색", description = "사용자 소설 검색 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "소설 검색 성공",
            content = @Content(schema = @Schema(implementation = NovelPageInfoResponse.class)))
    })
    @GetMapping("/search")
    public ApiCsResponse<NovelPageInfoResponse> searchNovel(@AuthenticationPrincipal Member member, @Valid SearchNovelReq searchNovelReq) {
        NovelPageInfoResponse novelPageInfoResponse = novelService.searchNovel(member, searchNovelReq);
        return ApiCsResponse.success(novelPageInfoResponse);
    }

    @Operation(summary = "작가 소개 수정", description = "메인 작가 소개글 수정하ㅣㄱ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "소설 검색 성공",
                    content = @Content(schema = @Schema(implementation = AuthorDescription.class)))
    })
    @PatchMapping("/{novelId}/author-description")
    public ApiCsResponse<AuthorDescription> modifyAuthorDescription(
            @PathVariable Long novelId,
            @Validated @RequestBody AuthorDescription req
    ) {
        Long authorId = 500L; // TODO: 토큰에서 author 정보 가져올 예정
        AuthorDescription description = novelService.modifyAuthorDescription(novelId, authorId, req);
        return ApiCsResponse.success(description);
    }

    @Operation(summary = "검색 페이지 Keyword", description = "인기검색어, 최근 검색어 목록 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색 Keyword load 성공",
            content = @Content(schema = @Schema(implementation = SearchKeywordResponse.class)))
    })
    @GetMapping("/search-keyword")
    public ApiCsResponse<SearchKeywordResponse> getSearchWord(@AuthenticationPrincipal Member member) {
        SearchKeywordResponse searchWordResponse = novelService.searchKeyword(member);
        return ApiCsResponse.success(searchWordResponse);
    }

    
    @Operation(summary = "검색 페이지 Keyword Delete", description = "인기 검색어, 최근 검색어 목록 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색 Keyword Delete 성공")
    })
    @DeleteMapping("/search-keyword")
    public ApiCsResponse<Void> deleteAllSearchWord(@AuthenticationPrincipal Member member) {
        novelService.deleteSearchKeyword(member);
        return ApiCsResponse.success();
    }
}

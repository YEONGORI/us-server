package us.usserver.domain.chapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import us.usserver.domain.chapter.dto.ChapterDetailInfo;
import us.usserver.domain.chapter.service.ChapterService;
import us.usserver.global.response.exception.AuthorNotFoundException;
import us.usserver.global.response.exception.MainAuthorIsNotMatchedException;
import us.usserver.global.response.ApiCsResponse;

@Tag(name = "회차 API")
@ResponseBody
@RestController
@RequestMapping("/chapter")
@RequiredArgsConstructor
public class ChapterController {
    private final ChapterService chapterService;

    @Operation(summary = "소설의 m(1~n)화 보기", description = "전지적독자시점 1화 보기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "m화 읽기 성공",
                    content = @Content(schema = @Schema(implementation = ChapterDetailInfo.class))),
            @ApiResponse(
                    responseCode = "400", description = "작가 혹은 소설 정보가 유효하지 않습니다..",
                    content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class))
            )
    })
    @GetMapping("/{novelId}/{chapterId}")
    public ApiCsResponse<ChapterDetailInfo> getChapterDetailInfo(
            @PathVariable Long novelId,
            @PathVariable Long chapterId
    ) {
        Long authorId = 500L; // TODO 토큰으로 교체 예정
        ChapterDetailInfo chapterDetailInfo = chapterService.getChapterDetailInfo(novelId, authorId, chapterId);
        return ApiCsResponse.success(chapterDetailInfo);
    }

    // TODO: 이전 회차의 권한 설정을 가져올 수 있는 기능을 제공 해야 하는데, 이 권한을 저장 하는 엔티티가 없어서 생성 해야함
    @Operation(summary = "소설의 m(1~n)화 생성하기", description = "전지적독자시점 2화 생성하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "m화 생성 성공",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(
                    responseCode = "400", description = "작가 혹은 소설 정보가 유효하지 않습니다..",
                    content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class))
            ),
            @ApiResponse(
                    responseCode = "406", description = "메인 작가만 생성할 수 있습니다.",
                    content = @Content(schema = @Schema(implementation = MainAuthorIsNotMatchedException.class))
            )
    })
    @PostMapping("/{novelId}")
    public ApiCsResponse<Void> createChapter(
            @PathVariable Long novelId
    ) {
        Long authorId = 500L; // TODO: 토큰에서 가져올 예정
        chapterService.createChapter(novelId, authorId);
        return ApiCsResponse.success(); // TODO : created 복구
    }
}

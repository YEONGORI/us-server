package us.usserver.chapter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.usserver.chapter.dto.ChapterDetailInfo;
import us.usserver.global.ApiCsResponse;
import us.usserver.global.exception.AuthorNotFoundException;


@Tag(name = "회차 API")
@ResponseBody
@RestController
@RequestMapping("/chapter")
@RequiredArgsConstructor
public class ChapterController {
    private final ChapterService chapterService;

    @Operation(summary = "소설의 m(1~n)화 보기", description = "전지적독자시점 1화 보기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "m화 읽기 성공"),
            @ApiResponse(
                    responseCode = "400", description = "작가 혹은 소설 정보가 유효하지 않습니다..",
                    content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class))
            )
    })
    @GetMapping("/{novelId}/{chapterId}")
    public ResponseEntity<ApiCsResponse<?>> getChapterDetailInfo(
            @PathVariable Long novelId,
            @PathVariable Long chapterId
    ) {
        Long authorId = 0L; // TODO 토큰으로 교체 예정
        ChapterDetailInfo chapterDetailInfo = chapterService.getChapterDetailInfo(novelId, authorId, chapterId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(chapterDetailInfo)
                .build();
        return ResponseEntity.ok(response);
    }

    // TODO: 이전 회차의 권한 설정을 가져올 수 있는 기능을 제공 해야 하는데, 이 권한을 저장 하는 엔티티가 없어서 생성 해야함
    @Operation(summary = "소설의 m(1~n)화 생성하기", description = "전지적독자시점 2화 생성하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "m화 생성 성공"),
            @ApiResponse(
                    responseCode = "400", description = "작가 혹은 소설 정보가 유효하지 않습니다..",
                    content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class))
            ),
            @ApiResponse(
                    responseCode = "406", description = "메인 작가만 생성할 수 있습니다.",
                    content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class))
            )
    })
    @PostMapping("/{novelId}")
    public ResponseEntity<ApiCsResponse<?>> createChapter(
            @PathVariable Long novelId
    ) {
        Long authorId = 0L; // TODO: 토큰에서 가져올 예정
        
        chapterService.createChapter(novelId, authorId);
        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}

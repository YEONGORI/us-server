package us.usserver.domain.author.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import us.usserver.domain.author.dto.res.BookshelfDefaultResponse;
import us.usserver.domain.author.service.BookshelfService;
import us.usserver.global.response.exception.AuthorNotFoundException;
import us.usserver.global.response.ApiCsResponse;

@Tag(name = "보관함(소설) API")
@ResponseBody
@RestController
@RequestMapping("/bookshelf")
@RequiredArgsConstructor
public class BookshelfController {
    private final BookshelfService bookshelfService;

    @Operation(summary = "최근에 본 소설 조회", description = "보관함 내 최근에 본 소설을 조회하는 기능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "최근에 본 소설 조회 성공",
                    content = @Content(schema = @Schema(implementation = BookshelfDefaultResponse.class))),
            @ApiResponse(
                    responseCode = "400", description = "작가가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class)))
    })
    @GetMapping("/viewed") // 내가 최근에 본 소설
    public ApiCsResponse<BookshelfDefaultResponse> recentViewedNovels() {
        Long authorId = 500L; // TODO: Token 으로 교체 예정
        BookshelfDefaultResponse bookshelfDefaultResponse = bookshelfService.recentViewedNovels(authorId);
        return ApiCsResponse.success(bookshelfDefaultResponse);
    }

    @DeleteMapping("/viewed/{novelId}") // 내가 최근에 본 소설 삭제
    public ApiCsResponse<Void> deleteRecentViewedNovels(@PathVariable Long readNovelId) {
        Long authorId = 500L; // TODO: Token 으로 교체 예정
        bookshelfService.deleteRecentViewedNovels(authorId, readNovelId);
        return ApiCsResponse.success();
    }

    @Operation(summary = "내가 생성한 소설 조회", description = "보관함 내 내가 생성한 소설을 조회하는 기능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내가 생성한 소설 조회 성공",
                    content = @Content(schema = @Schema(implementation = BookshelfDefaultResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400", description = "작가가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class))
            )
    })
    @GetMapping("/created") // 내가 생성한 소설
    public ApiCsResponse<BookshelfDefaultResponse> createdNovels() {
        Long authorId = 500L; // TODO: Token 으로 교체 예정
        BookshelfDefaultResponse bookshelfDefaultResponse = bookshelfService.createdNovels(authorId);
        return ApiCsResponse.success(bookshelfDefaultResponse);
    }

    @DeleteMapping("/created/{novelId}") // 내가 생성한 소설 삭제
    public ApiCsResponse<Void> deleteCreatedNovels(@PathVariable Long novelId) {
        Long authorId = 500L; // TODO: Token 으로 교체 예정
        bookshelfService.deleteCreatedNovels(authorId, novelId);
        return ApiCsResponse.success();
    }


    @Operation(summary = "내가 참여한 소설 조회", description = "보관함 내 내가 참여한 소설을 조회하는 기능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내가 참여한 소설 조회 성공",
                    content = @Content(schema = @Schema(implementation = BookshelfDefaultResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400", description = "작가가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class))
            )
    })
    @GetMapping("/joined") // 내가 참여한 소설
    public ApiCsResponse<BookshelfDefaultResponse> joinedNovels() {
        Long authorId = 500L; // TODO: Token 으로 교체 예정
        BookshelfDefaultResponse bookshelfDefaultResponse = bookshelfService.joinedNovels(authorId);
        return ApiCsResponse.success(bookshelfDefaultResponse);
    }

    @DeleteMapping("/joined/{novelId}") // 내가 참여한 소설 삭제
    public ApiCsResponse<Void> deleteJoinedNovels(@PathVariable Long novelId) {
        Long authorId = 500L; // TODO: Token 으로 교체 예정
        bookshelfService.deleteJoinedNovels(authorId, novelId);
        return ApiCsResponse.success();
    }

    @Operation(summary = "내가 좋아요한 소설 조회", description = "보관함 내 내가 좋아요한 소설을 조회하는 기능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내가 좋아요한 소설 조회 성공",
                    content = @Content(schema = @Schema(implementation = BookshelfDefaultResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400", description = "작가가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class))
            )
    })
    @GetMapping("/liked") // 내가 좋아요 한 소설
    public ApiCsResponse<BookshelfDefaultResponse> likedNovels() {
        Long authorId = 500L; // TODO: Token 으로 교체 예정
        BookshelfDefaultResponse bookshelfDefaultResponse = bookshelfService.likedNovels(authorId);
        return ApiCsResponse.success(bookshelfDefaultResponse);
    }

    @DeleteMapping("/liked/{novelId}") // 내가 좋아요 한 소설 삭제
    public ApiCsResponse<Void> deleteLikedNovels(@PathVariable Long novelId) {
        Long authorId = 500L; // TODO: Token 으로 교체 예정
        bookshelfService.deleteLikedNovels(authorId, novelId);
        return ApiCsResponse.success();
    }
}

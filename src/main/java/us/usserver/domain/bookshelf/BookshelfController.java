package us.usserver.domain.bookshelf;

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
import us.usserver.domain.bookshelf.dto.BookshelfDefaultResponse;
import us.usserver.domain.bookshelf.service.BookshelfService;
import us.usserver.global.ApiCsResponse;
import us.usserver.global.exception.AuthorNotFoundException;

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
    public ResponseEntity<ApiCsResponse<?>> recentViewedNovels() {
        Long authorId = 500L; // TODO: Token 으로 교체 예정
        BookshelfDefaultResponse bookshelfDefaultResponse = bookshelfService.recentViewedNovels(authorId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(bookshelfDefaultResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/viewed/{novelId}") // 내가 최근에 본 소설 삭제
    public ResponseEntity<ApiCsResponse<?>> deleteRecentViewedNovels(@PathVariable Long novelId) {
        Long authorId = 500L; // TODO: Token 으로 교체 예정
        bookshelfService.deleteRecentViewedNovels(authorId, novelId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message(HttpStatus.NO_CONTENT.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
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
    public ResponseEntity<ApiCsResponse<?>> createdNovels() {
        Long authorId = 500L; // TODO: Token 으로 교체 예정
        BookshelfDefaultResponse bookshelfDefaultResponse = bookshelfService.createdNovels(authorId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(bookshelfDefaultResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/created/{novelId}") // 내가 생성한 소설 삭제
    public ResponseEntity<ApiCsResponse<?>> deleteCreatedNovels(@PathVariable Long novelId) {
        Long authorId = 500L; // TODO: Token 으로 교체 예정
        bookshelfService.deleteCreatedNovels(authorId, novelId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message(HttpStatus.NO_CONTENT.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
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
    public ResponseEntity<ApiCsResponse<?>> joinedNovels() {
        Long authorId = 500L; // TODO: Token 으로 교체 예정
        BookshelfDefaultResponse bookshelfDefaultResponse = bookshelfService.joinedNovels(authorId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(bookshelfDefaultResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/joined/{novelId}") // 내가 참여한 소설 삭제
    public ResponseEntity<ApiCsResponse<?>> deleteJoinedNovels(@PathVariable Long novelId) {
        Long authorId = 500L; // TODO: Token 으로 교체 예정
        bookshelfService.deleteJoinedNovels(authorId, novelId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message(HttpStatus.NO_CONTENT.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
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
    public ResponseEntity<ApiCsResponse<?>> likedNovels() {
        Long authorId = 500L; // TODO: Token 으로 교체 예정
        BookshelfDefaultResponse bookshelfDefaultResponse = bookshelfService.likedNovels(authorId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(bookshelfDefaultResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/liked/{novelId}") // 내가 좋아요 한 소설 삭제
    public ResponseEntity<ApiCsResponse<?>> deleteLikedNovels(@PathVariable Long novelId) {
        Long authorId = 500L; // TODO: Token 으로 교체 예정
        bookshelfService.deleteLikedNovels(authorId, novelId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message(HttpStatus.NO_CONTENT.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}

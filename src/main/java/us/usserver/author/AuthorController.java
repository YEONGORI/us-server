package us.usserver.author;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import us.usserver.author.dto.UpdateAuthorReq;
import us.usserver.global.ApiCsResponse;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.member.Member;


@RequiredArgsConstructor
@RequestMapping("/author")
@RestController
public class AuthorController {
    private final AuthorService authorService;

    @Operation(summary = "사용자 정보 수정", description = "사용자 정보를 수정하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 정보 업데이트 성공"),
            @ApiResponse(responseCode = "400", description = "작가가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class)))
    })
    @PatchMapping
    public ResponseEntity<ApiCsResponse<?>> updateAuthor(@AuthenticationPrincipal Member member,
                                                         @ModelAttribute UpdateAuthorReq updateAuthorReq) {
        authorService.updateAuthor(member.getId(), updateAuthorReq);
        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}
package us.usserver.domain.author.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import us.usserver.domain.author.dto.req.FontSizeReq;
import us.usserver.domain.author.dto.req.ParagraphSpaceReq;
import us.usserver.domain.author.dto.req.UpdateAuthorReq;
import us.usserver.domain.author.service.AuthorService;
import us.usserver.domain.member.entity.Member;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.global.exception.FontSizeOutOfRangeException;
import us.usserver.global.response.ApiCsResponse;

@Tag(name = "사용자 정보 API")
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
    public ApiCsResponse<Void> updateAuthor(@AuthenticationPrincipal Member member,
                                                         @ModelAttribute UpdateAuthorReq updateAuthorReq) {
        authorService.updateAuthor(member.getId(), updateAuthorReq);
        return ApiCsResponse.success();
    }

    @Operation(summary = "글씨 크기 수정", description = "글씨 크기 조정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "글씨 크기 업데이트 성공"),
            @ApiResponse(responseCode = "400", description = "1 ~ 30 범위.",
                    content = @Content(schema = @Schema(implementation = FontSizeOutOfRangeException.class)))
    })
    @PatchMapping("/fontsize")
    public ApiCsResponse<Void> changeFontSize(@Validated @RequestBody FontSizeReq req) {
        Long authorId = 500L;
        authorService.changeFontSize(authorId, req.getFontSize());
        return ApiCsResponse.success();
    }

    @Operation(summary = "단란 간격 수정", description = "단락 간격 조정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "글씨 크기 업데이트 성공"),
            @ApiResponse(responseCode = "400", description = "1 ~ 30 범위.",
                    content = @Content(schema = @Schema(implementation = FontSizeOutOfRangeException.class)))
    })
    @PatchMapping("/paragraph-space")
    public ApiCsResponse<Void> changeParagraphSpace(@Validated @RequestBody ParagraphSpaceReq req) {
        Long authorId = 500L;
        authorService.changeParagraphSpace(authorId, req.getParagraphSpace());
        return ApiCsResponse.success();
    }
}
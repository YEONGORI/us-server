package us.usserver.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import us.usserver.domain.member.entity.Member;
import us.usserver.domain.member.service.MemberService;
import us.usserver.global.ApiCsResponse;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.domain.member.service.TokenProvider;
import us.usserver.domain.member.dto.req.JoinMemberRequest;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @Operation(summary = "회원 가입", description = "사용자가 회원가입을 하여 Member, Author Entity를 생성하는 API")
    @ApiResponse(responseCode = "201", description = "회원가입 성공",
            content = @Content(schema = @Schema(implementation = Long.class)))
    @PostMapping("/join")
    public ResponseEntity<ApiCsResponse<?>> joinMember(@Valid @RequestBody JoinMemberRequest joinMemberRequest) {
        Long memberId = memberService.join(joinMemberRequest);
        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data("localhost:8080/novel/main")
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "로그 아웃", description = "사용자 로그아웃 API")
    @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    @PostMapping("/logout")
    public ResponseEntity<ApiCsResponse<?>> logoutMember(HttpServletRequest request) {
        String accessToken = tokenProvider.extractToken(request, "AccessToken");
        memberService.logout(accessToken);
        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "회원 탈퇴", description = "사용자 회원 탈퇴 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원탈퇴 성공"),
            @ApiResponse(responseCode = "400", description = "작가가 없습니다", content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class)))
    })
    @DeleteMapping("/withdraw")
    public ResponseEntity<ApiCsResponse<?>> withdrawMember(@AuthenticationPrincipal Member member) {
        memberService.withdraw(member);
        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }
}
package us.usserver.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import us.usserver.domain.member.dto.req.JoinMemberRequest;
import us.usserver.domain.member.entity.Member;
import us.usserver.domain.member.service.MemberService;
import us.usserver.domain.member.service.TokenProvider;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.global.response.ApiCsResponse;

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
    public ApiCsResponse<?> joinMember(@Valid @RequestBody JoinMemberRequest joinMemberRequest) {
        Long memberId = memberService.join(joinMemberRequest);
        return ApiCsResponse.success("localhost:8080/novel/main");
    }

    @Operation(summary = "로그 아웃", description = "사용자 로그아웃 API")
    @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    @PostMapping("/logout")
    public ApiCsResponse<Void> logoutMember(HttpServletRequest request) {
        String accessToken = tokenProvider.extractToken(request, "AccessToken");
        memberService.logout(accessToken);
        return ApiCsResponse.success();
    }

    @Operation(summary = "회원 탈퇴", description = "사용자 회원 탈퇴 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원탈퇴 성공"),
            @ApiResponse(responseCode = "400", description = "작가가 없습니다", content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class)))
    })
    @DeleteMapping("/withdraw")
    public ApiCsResponse<Void> withdrawMember(@AuthenticationPrincipal Member member) {
        memberService.withdraw(member);
        return ApiCsResponse.success();
    }
}
package us.usserver.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.usserver.global.ApiCsResponse;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.global.jwt.TokenProvider;
import us.usserver.member.dto.JoinMemberReq;
import us.usserver.novel.Novel;

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
    public ResponseEntity<ApiCsResponse<?>> joinMember(@Valid @RequestBody JoinMemberReq joinMemberReq) {
        Long memberId = memberService.join(joinMemberReq);
        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(memberId)
                .build();
        return ResponseEntity.ok(response);
    }

}
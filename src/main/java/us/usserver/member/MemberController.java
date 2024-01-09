package us.usserver.member;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.usserver.global.ApiCsResponse;
import us.usserver.global.jwt.TokenProvider;
import us.usserver.member.dto.JoinMemberReq;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

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
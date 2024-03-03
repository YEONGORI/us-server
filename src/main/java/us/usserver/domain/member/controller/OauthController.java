package us.usserver.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import us.usserver.domain.member.dto.LoginDto;
import us.usserver.domain.member.dto.MemberInfoDto;
import us.usserver.domain.member.dto.parameter.GoogleParams;
import us.usserver.domain.member.dto.parameter.KakaoParams;
import us.usserver.domain.member.dto.req.OauthRequest;
import us.usserver.domain.member.service.OauthService;
import us.usserver.domain.member.service.TokenProvider;
import us.usserver.global.response.ApiCsResponse;
import us.usserver.global.response.exception.AuthorNotFoundException;
import us.usserver.global.response.exception.BaseException;
import us.usserver.global.response.exception.ErrorCode;
import us.usserver.global.response.exception.TokenInvalidException;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/oauth2")
@RestController
public class OauthController {
    private final TokenProvider tokenProvider;
    private final OauthService oauthService;

    @Operation(summary = "카카오 소셜 로그인", description = "로그인 API \n\n" +
            "인증 Code를 전달받아서 Social Server와 통신하며 사용자 정보를 받아오는 로직 \n\n \n\n" +
            "로직 설명 \n\n" +
            "1. Social 인증 Code를 전달받는다. \n\n" +
            "2. 인증 Code를 활용하여 accessToken 요청 URI를 작성한뒤 social server에 요청을 보내 accessToken을 받는다. \n\n" +
            "3. 받은 accessToken을 활용하여 사용자 정보 요청 URI를 작성한 뒤 요청을 보낸다. \n\n" +
            "4. 사용자 정보를 DB에서 찾아 신규, 기존 유저인지 판별 후 해당하는 값들을 response \n\n" +
            "※ 신규 유저는 userId와 role(GUEST)를 기존 유저는 userId와 role(USER), accessToken, refreshToken을 발급하여 Response ※"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = LoginDto.class))),
            @ApiResponse(responseCode = "404", description = "작가 NOT FOUND", content = @Content(schema = @Schema(implementation = AuthorNotFoundException.class))),
            @ApiResponse(responseCode = "401", description = "invalid social token", content = @Content(schema = @Schema(implementation = TokenInvalidException.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<LoginDto> socialLogin(@RequestBody OauthRequest oauthRequest) {
        log.debug("넘겨받은 kakao 인증키 :: " + oauthRequest.getCode());

        MemberInfoDto memberInfoDto;
        switch (oauthRequest.getOauthProvider()) {
            case KAKAO -> memberInfoDto = oauthService.getMemberByOauthLogin(new KakaoParams(oauthRequest.getCode()));
            case GOOGLE -> memberInfoDto = oauthService.getMemberByOauthLogin(new GoogleParams(oauthRequest.getCode()));
            default -> throw new BaseException(ErrorCode.UNSUPPORTED_SOCIAL_PROVIDER);
        }
//        if (oauthRequest.getOauthProvider() == OauthProvider.KAKAO) {
//            memberInfoDto = oauthService.getMemberByOauthLogin(new KakaoParams(oauthRequest.getCode()));
//        } else if (oauthRequest.getOauthProvider() == OauthProvider.NAVER) {
//            memberInfoDto = oauthService.getMemberByOauthLogin(new NaverParams(oauthRequest.getCode(), oauthRequest.getState()));
//        } else if (oauthRequest.getOauthProvider() == OauthProvider.GOOGLE){
//            memberInfoDto = oauthService.getMemberByOauthLogin(new GoogleParams(oauthRequest.getCode()));
//        } else {
//            memberInfoDto = oauthService.getMemberByOauthLogin(new AppleParams(oauthRequest.getCode()));
//        }

        //응답 헤더 생성
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(tokenProvider.getAccessHeader(), memberInfoDto.accessToken());
        httpHeaders.add(tokenProvider.getRefreshHeader(), memberInfoDto.refreshToken());
        return ResponseEntity.ok().headers(httpHeaders).body(new LoginDto(null));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "토큰 재발급 API"),
            @ApiResponse(responseCode = "400", description = "토큰을 찾을 수 없습니다",
                    content = @Content(schema = @Schema(implementation = TokenInvalidException.class)))
    })
    @GetMapping("/renew-token")
    public ApiCsResponse<Void> renewToken(HttpServletRequest request, HttpServletResponse servletResponse) {
        String refreshToken = tokenProvider.extractToken(request, "RefreshToken");
        String accessToken = tokenProvider.renewToken(refreshToken);
        servletResponse.addHeader(tokenProvider.getAccessHeader(), "Bearer " + accessToken);
        return ApiCsResponse.success();
    }
}
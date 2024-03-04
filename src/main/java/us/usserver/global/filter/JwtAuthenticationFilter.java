package us.usserver.global.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import us.usserver.domain.member.entity.Member;
import us.usserver.domain.member.service.TokenProvider;
import us.usserver.global.EntityFacade;
import us.usserver.global.response.exception.BaseException;
import us.usserver.global.response.exception.ErrorCode;
import us.usserver.global.response.exception.ExceptionMessage;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final EntityFacade entityFacade;

    /**
     * 인증 하지 않는 페이지
     */
    private static final String[] whitelist = {
            "/", "/login**", "/oauth**", "/member/join",
            "/resources/**", "/favicon.ico", //resource
            "/swagger-ui/**", "/api-docs/**", //swagger
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return PatternMatchUtils.simpleMatch(whitelist, request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = tokenProvider.extractToken(request, "AccessToken");
        if (accessToken == null) {
            log.error(ExceptionMessage.TOKEN_NOT_FOUND);
            throw new BaseException(ErrorCode.TOKEN_NOT_FOUND);
        }

        //TODO: redis blacklist에 accessToken 값이 있는지(로그아웃을 한 사용자인지) 체크
        DecodedJWT decodedJWT = tokenProvider.isTokenValid(accessToken);
        Long id = decodedJWT.getClaim("id").asLong();
        Member member = entityFacade.getMember(id);
        Authentication authentication = new UsernamePasswordAuthenticationToken(member, null, Collections.singleton(new SimpleGrantedAuthority(member.getRole().toString())));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        doFilter(request, response, filterChain);
    }
}
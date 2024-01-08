package us.usserver.global.oauth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import us.usserver.author.Author;
import us.usserver.author.AuthorRepository;
import us.usserver.global.EntityService;
import us.usserver.global.ExceptionMessage;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.global.jwt.TokenProvider;
import us.usserver.global.oauth.CustomOauth2User;
import us.usserver.member.Member;
import us.usserver.member.memberEnum.Role;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;
    private final AuthorRepository authorRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("Oauth2 Login Success!");
        Member member = ((CustomOauth2User) authentication.getPrincipal()).getMember();

        //TODO: url 수정
        UriComponentsBuilder redirectURLBuilder = UriComponentsBuilder.fromUriString("http://localhost:8080/oauth2/login")
                .queryParam("email", member.getEmail())
                .queryParam("socialType", member.getSocialType())
                .queryParam("socialId", member.getSocialId())
                .queryParam("role", member.getRole())

                ;

        if (member.getRole().equals(Role.USER)) {
            Optional<Author> authorByMemberId = authorRepository.getAuthorByMemberId(member.getId());
            if (authorByMemberId.isEmpty()) {
                throw new AuthorNotFoundException(ExceptionMessage.Author_NOT_FOUND);
            }
            String accessToken = tokenProvider.createAccessToken(authorByMemberId.get());
            String refreshToken = tokenProvider.createRefreshToken(authorByMemberId.get());

            tokenProvider.updateRefreshToken(String.valueOf(authorByMemberId.get().getId()), refreshToken);

            redirectURLBuilder
                    .queryParam("accessToken", "Bearer " + accessToken)
                    .queryParam("refreshToken", "Bearer" + refreshToken);
        }

        String redirectURL = redirectURLBuilder
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, redirectURL);
    }
}
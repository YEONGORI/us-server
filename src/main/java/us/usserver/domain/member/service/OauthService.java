package us.usserver.domain.member.service;

import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import us.usserver.domain.author.entity.Author;
import us.usserver.domain.author.repository.AuthorRepository;
import us.usserver.domain.member.constant.Gender;
import us.usserver.domain.member.constant.Role;
import us.usserver.domain.member.dto.MemberInfoDto;
import us.usserver.domain.member.dto.member.OauthMember;
import us.usserver.domain.member.dto.parameter.OauthParams;
import us.usserver.domain.member.entity.Member;
import us.usserver.domain.member.repository.MemberRepository;
import us.usserver.global.response.exception.BaseException;
import us.usserver.global.response.exception.ErrorCode;

@Slf4j
@RequiredArgsConstructor
@Component
public class OauthService {

    private final RequestOauthInfoService requestOauthInfoService;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final AuthorRepository authorRepository;
    public final String[] NICKNAME = {"하늘", "구름", "숲속", "들판", "우주", "바다", "물결", "별빛", "소망", "노을",
            "노래", "햇살", "연목", "바람", "동산", "미지", "모험", "탐험"};
    public MemberInfoDto getMemberByOauthLogin(OauthParams oauthParams) {
        log.debug("------- Oauth 로그인 시도 -------");

        //인증 파라미터 객체를 이용하여 해당 enum클래스에 해당하는 메소드 수행
        OauthMember request = requestOauthInfoService.request(oauthParams);
        log.debug("전달받은 유저정보:" + request.getEmail());

        //기존유저인지 신규유저인기 체크
        Optional<Member> bySocialTypeAndSocialId = memberRepository.findByOauthProviderAndSocialId(request.getOauthProvider(), request.getSocialId());

        //기존유저
        if (bySocialTypeAndSocialId.isPresent()) {
            Member member = bySocialTypeAndSocialId.get();
            Author author = authorRepository.getAuthorByMemberId(member.getId())
                    .orElseThrow(() -> new BaseException(ErrorCode.AUTHOR_NOT_FOUND));
            String accessToken = tokenProvider.createAccessToken(author);
            String refreshToken = tokenProvider.createRefreshToken(author);

            return new MemberInfoDto(member.getId(), member.getRole(), accessToken, refreshToken);
        }
        //신규유저
        else {
            //TODO: member와 author 자동 생성 해야함!
            Random random = new Random();
            Member newMember = Member.builder()
                    .socialId(request.getSocialId())
                    .oauthProvider(request.getOauthProvider())
                    .email(request.getEmail())
                    .age(-1)
                    .gender(Gender.UNKNOWN)
                    .role(Role.USER)
                    .build();
            Member savedMember = memberRepository.save(newMember);

            Author newAuthor = Author.builder()
                    .nickname(NICKNAME[random.nextInt(18)] + random.nextInt(0, 999))
                    .member(savedMember)
                    .build();
            Author savedAuthor = authorRepository.save(newAuthor);
            String accessToken = tokenProvider.createAccessToken(savedAuthor);
            String refreshToken = tokenProvider.createRefreshToken(savedAuthor);

            return new MemberInfoDto(newMember.getId(), newMember.getRole(), accessToken, refreshToken);
        }
    }
}

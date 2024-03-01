package us.usserver.domain.member.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.usserver.domain.author.entity.Author;
import us.usserver.domain.author.repository.AuthorRepository;
import us.usserver.domain.member.constant.Role;
import us.usserver.global.ExceptionMessage;
import us.usserver.global.utils.RedisUtils;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.global.exception.MemberNotFoundException;
import us.usserver.domain.member.entity.Member;
import us.usserver.domain.member.repository.MemberRepository;
import us.usserver.domain.member.dto.req.JoinMemberRequest;

import java.time.LocalDateTime;
import java.util.Random;

import static us.usserver.global.ExceptionMessage.*;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MemberServiceImpl implements MemberService {
    private final RedisUtils redisUtils;
    private final MemberRepository memberRepository;
    private final AuthorRepository authorRepository;
    private final TokenProvider tokenProvider;

    public final String[] NICKNAME = {"하늘", "구름", "숲속", "들판", "우주", "바다", "물결", "별빛", "소망", "노을",
            "노래", "햇살", "연목", "바람", "동산", "미지", "모험", "탐험"};

    @Override
    public Long join(JoinMemberRequest joinMemberRequest) {
        Member member = Member.builder()
                .socialId(joinMemberRequest.getSocialId())
                .oauthProvider(joinMemberRequest.getOauthProvider())
                .email(joinMemberRequest.getEmail())
                .age(LocalDateTime.now().getYear() - joinMemberRequest.getBirthday().getYear())
                .gender(joinMemberRequest.getGender())
                .role(Role.USER)
                .build();
        Member savedMember = memberRepository.save(member);

        Random random = new Random();

        Author author = Author.builder()
                .nickname(NICKNAME[random.nextInt(18)] + random.nextInt(100, 999))
                .introduction("반갑습니다.")
                .member(member)
                .build();
        authorRepository.save(author);
        return savedMember.getId();
    }

    @Override
    public void logout(String accessToken) {
        DecodedJWT decodedJWT = tokenProvider.isTokenValid(accessToken);
        String id = decodedJWT.getClaim("id").asString();

        Long expiration = tokenProvider.getExpiration(accessToken);

        if (redisUtils.getData(id) != null) {
            redisUtils.deleteData(id);
        }
        redisUtils.setDateExpire(accessToken, "logout", expiration);
    }

    @Override
    public void withdraw(Member member) {
        Author author = authorRepository.getAuthorByMemberId(member.getId()).orElseThrow(() -> new AuthorNotFoundException(ExceptionMessage.AUTHOR_NOT_FOUND));
        authorRepository.deleteById(author.getId());
        memberRepository.deleteById(member.getId());
    }

    @Override
    public Member getMyInfo(String socialId) {
        return memberRepository.findBySocialId(socialId).orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));
    }
}
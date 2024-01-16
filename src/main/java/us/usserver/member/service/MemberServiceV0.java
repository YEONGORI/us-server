package us.usserver.member.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.usserver.author.Author;
import us.usserver.author.AuthorRepository;
import us.usserver.global.EntityService;
import us.usserver.global.ExceptionMessage;
import us.usserver.global.RedisUtils;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.global.exception.MemberNotFoundException;
import us.usserver.global.jwt.TokenProvider;
import us.usserver.member.Member;
import us.usserver.member.MemberRepository;
import us.usserver.member.MemberService;
import us.usserver.member.dto.JoinMemberReq;
import us.usserver.member.memberEnum.Role;

import java.time.LocalDateTime;
import java.util.Random;

import static us.usserver.global.ExceptionMessage.*;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MemberServiceV0 implements MemberService {
    private final RedisUtils redisUtils;
    private final EntityService entityService;
    private final MemberRepository memberRepository;
    private final AuthorRepository authorRepository;
    private final TokenProvider tokenProvider;

    public final String[] NICKNAME = {"하늘", "구름", "숲속", "들판", "우주", "바다", "물결", "별빛", "소망", "노을",
            "노래", "햇살", "연목", "바람", "동산", "미지", "모험", "탐험"};
    @Override
    public Long join(JoinMemberReq joinMemberReq) {
        Member member = Member.builder()
                .socialId(joinMemberReq.getSocialId())
                .socialType(joinMemberReq.getSocialType())
                .email(joinMemberReq.getEmail())
                .age(LocalDateTime.now().getYear() - joinMemberReq.getBirthday().getYear())
                .gender(joinMemberReq.getGender())
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
        Author author = authorRepository.getAuthorByMemberId(member.getId()).orElseThrow(() -> new AuthorNotFoundException(ExceptionMessage.Author_NOT_FOUND));
        authorRepository.deleteById(author.getId());
        memberRepository.deleteById(member.getId());
    }

    @Override
    public Member getMyInfo(String socialId) {
        return memberRepository.findBySocialId(socialId).orElseThrow(() -> new MemberNotFoundException(Member_NOT_FOUND));
    }
}
package us.usserver.member.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.usserver.author.Author;
import us.usserver.author.AuthorRepository;
import us.usserver.global.EntityService;
import us.usserver.global.RedisUtils;
import us.usserver.global.exception.MemberNotFoundException;
import us.usserver.global.jwt.TokenProvider;
import us.usserver.member.Member;
import us.usserver.member.MemberRepository;
import us.usserver.member.MemberService;
import us.usserver.member.dto.JoinMemberReq;
import us.usserver.member.memberEnum.Role;

import java.time.LocalDateTime;
import java.util.List;
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

    public final String[] NICKNAME = {"신비로운 여행자", "초월한 모험가", "시공의 탐험가", "미지의 선장", "묘한 세계관",
            "비상한 모험인", "창의적 모험가", "세계를 누비는 자", "우주를 담은자", "어느 별에서 온 모험가"};
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
                .nickname(NICKNAME[random.nextInt(10)] + random.nextInt(10000, 100000))
                .introduction("반갑습니다.")
                .member(member)
                .build();
        authorRepository.save(author);
        return savedMember.getId();
    }

    @Override
    public Member getMyInfo(String socialId) {
        return memberRepository.findBySocialId(socialId).orElseThrow(() -> new MemberNotFoundException(Member_NOT_FOUND));
    }

}
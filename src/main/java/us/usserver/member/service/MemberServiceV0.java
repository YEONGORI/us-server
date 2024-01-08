package us.usserver.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import us.usserver.global.ExceptionMessage;
import us.usserver.global.exception.MemberNotFoundException;
import us.usserver.member.Member;
import us.usserver.member.MemberRepository;
import us.usserver.member.MemberService;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberServiceV0 implements MemberService {
    private final MemberRepository memberRepository;
    @Override
    public Member getMyInfo(String socialId) {
        Optional<Member> bySocialId = memberRepository.findBySocialId(socialId);

        if (bySocialId.isEmpty()) {
            throw new MemberNotFoundException(ExceptionMessage.Member_NOT_FOUND);
        }

        return bySocialId.get();
    }
}

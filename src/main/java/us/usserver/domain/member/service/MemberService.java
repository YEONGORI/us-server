package us.usserver.domain.member.service;

import org.springframework.stereotype.Service;
import us.usserver.domain.member.entity.Member;
import us.usserver.domain.member.dto.req.JoinMemberRequest;

@Service
public interface MemberService {
    Long join(JoinMemberRequest joinMemberRequest);
    void logout(String accessToken);
    void withdraw(Member member);
    Member getMyInfo(String socialId);

}
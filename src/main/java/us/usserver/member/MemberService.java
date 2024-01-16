package us.usserver.member;

import org.springframework.stereotype.Service;
import us.usserver.member.dto.JoinMemberReq;

@Service
public interface MemberService {
    Long join(JoinMemberReq joinMemberReq);
    void logout(String accessToken);
    void withdraw(Member member);
    Member getMyInfo(String socialId);

}
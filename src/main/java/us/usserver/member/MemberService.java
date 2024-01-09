package us.usserver.member;

import org.springframework.stereotype.Service;
import us.usserver.member.dto.JoinMemberReq;

@Service
public interface MemberService {
    Long join(JoinMemberReq joinMemberReq);
    Member getMyInfo(String socialId);
}
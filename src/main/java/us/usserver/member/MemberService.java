package us.usserver.member;

import org.springframework.stereotype.Service;

@Service
public interface MemberService {
    Member getMyInfo(String socialId);
}

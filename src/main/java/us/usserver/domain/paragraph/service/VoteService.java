package us.usserver.domain.paragraph.service;

import org.springframework.stereotype.Service;
import us.usserver.domain.member.entity.Member;

@Service
public interface VoteService {
    void voting(Member member, Long paragraphId);

    void unvoting(Member member, Long voteId);
}

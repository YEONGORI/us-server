package us.usserver.domain.paragraph.service;

import org.springframework.stereotype.Service;

@Service
public interface VoteService {
    void voting(Long paragraphId, Long authorId);

    void unvoting(Long voteId, Long authorId);
}

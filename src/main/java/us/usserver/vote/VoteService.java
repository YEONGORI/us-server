package us.usserver.vote;

import org.springframework.stereotype.Service;

@Service
public interface VoteService {
    void voting(Long paragraphId, Long authorId);

    void unvoting(Long voteId, Long authorId);
}

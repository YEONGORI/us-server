package us.usserver.vote;

import org.springframework.stereotype.Service;

@Service
public interface VoteService {
    Vote voting(Long paragraphId, Long authorId);

    void unvoting(Long voteId, Long authorId);
}

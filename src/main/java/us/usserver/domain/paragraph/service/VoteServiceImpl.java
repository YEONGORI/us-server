package us.usserver.domain.paragraph.service;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.domain.author.entity.Author;
import us.usserver.domain.member.entity.Member;
import us.usserver.domain.paragraph.entity.Paragraph;
import us.usserver.domain.paragraph.entity.Vote;
import us.usserver.domain.paragraph.repository.VoteRepository;
import us.usserver.global.EntityFacade;
import us.usserver.global.response.exception.BaseException;
import us.usserver.global.response.exception.ErrorCode;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {
    private final EntityFacade entityFacade;
    private final VoteRepository voteRepository;

    @Override
    public void voting(Long memberId, Long paragraphId) {
        Paragraph paragraph = entityFacade.getParagraph(paragraphId);
        Author author = entityFacade.getAuthorByMemberId(memberId);

        List<Vote> allByAuthor = voteRepository.findAllByAuthor(author);
        if (allByAuthor.stream()
                .filter(vote -> Objects.equals(vote.getParagraph().getChapter().getId(), paragraph.getChapter().getId()))
                .anyMatch(vote ->
                        vote.getParagraph().getSequence() == paragraph.getSequence())
        ) {
            throw new BaseException(ErrorCode.VOTE_ONLY_ONE_PARAGRAPH);
        }

        Vote vote = Vote.builder().author(author).paragraph(paragraph).build();
        paragraph.addVote(vote);
        voteRepository.save(vote);
    }

    @Override
    public void unvoting(Long memberId, Long voteId) {
        Vote vote = entityFacade.getVote(voteId);
        Author author = entityFacade.getAuthorByMemberId(memberId);

        if (!vote.getAuthor().getId().equals(author.getId())) {
            throw new BaseException(ErrorCode.AUTHOR_NOT_AUTHORIZED);
        }

        vote.getParagraph().removeVote(vote);
        voteRepository.delete(vote);
    }
}

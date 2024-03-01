package us.usserver.domain.paragraph.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.domain.author.entity.Author;
import us.usserver.domain.paragraph.entity.Paragraph;
import us.usserver.domain.paragraph.entity.Vote;
import us.usserver.domain.paragraph.repository.VoteJpaRepository;
import us.usserver.global.EntityFacade;
import us.usserver.global.ExceptionMessage;
import us.usserver.global.exception.AuthorNotAuthorizedException;
import us.usserver.global.exception.DuplicatedVoteException;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class VoteServiceV0 implements VoteService {
    private final EntityFacade entityFacade;
    private final VoteJpaRepository voteJpaRepository;

    @Override
    public void voting(Long paragraphId, Long authorId) {
        Paragraph paragraph = entityFacade.getParagraph(paragraphId);
        Author author = entityFacade.getAuthor(authorId);

        List<Vote> allByAuthor = voteJpaRepository.findAllByAuthor(author);
        if (allByAuthor.stream()
                .filter(vote -> Objects.equals(vote.getParagraph().getChapter().getId(), paragraph.getChapter().getId()))
                .anyMatch(vote ->
                        vote.getParagraph().getSequence() == paragraph.getSequence())
        ) {
            throw new DuplicatedVoteException(ExceptionMessage.VOTE_ONLY_ONE_PARAGRAPH);
        }

        Vote vote = Vote.builder()
                .paragraph(paragraph)
                .author(author)
                .build();

        voteJpaRepository.save(vote);
    }

    @Override
    public void unvoting(Long voteId, Long authorId) {
        Vote vote = entityFacade.getVote(voteId);
        Author author = entityFacade.getAuthor(authorId);

        if (!vote.getAuthor().getId().equals(author.getId())) {
            throw new AuthorNotAuthorizedException(ExceptionMessage.AUTHOR_NOT_AUTHORIZED);
        }

        voteJpaRepository.delete(vote);
    }
}

package us.usserver.vote.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.author.Author;
import us.usserver.global.EntityService;
import us.usserver.global.ExceptionMessage;
import us.usserver.global.exception.AuthorNotAuthorizedException;
import us.usserver.global.exception.DuplicatedVoteException;
import us.usserver.paragraph.Paragraph;
import us.usserver.vote.Vote;
import us.usserver.vote.VoteRepository;
import us.usserver.vote.VoteService;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class VoteServiceV0 implements VoteService {
    private final EntityService entityService;
    private final VoteRepository voteRepository;

    @Override
    public void voting(Long paragraphId, Long authorId) {
        Paragraph paragraph = entityService.getParagraph(paragraphId);
        Author author = entityService.getAuthor(authorId);

        List<Vote> allByAuthor = voteRepository.findAllByAuthor(author);
        if (allByAuthor.stream()
                .filter(vote -> Objects.equals(vote.getParagraph().getChapter().getId(), paragraph.getChapter().getId()))
                .anyMatch(vote ->
                        vote.getParagraph().getSequence() == paragraph.getSequence())
        ) {
            throw new DuplicatedVoteException(ExceptionMessage.Vote_Only_One_Paragraph);
        }

        Vote vote = Vote.builder()
                .paragraph(paragraph)
                .author(author)
                .build();

        voteRepository.save(vote);
    }

    @Override
    public void unvoting(Long voteId, Long authorId) {
        Vote vote = entityService.getVote(voteId);
        Author author = entityService.getAuthor(authorId);

        if (!vote.getAuthor().getId().equals(author.getId())) {
            throw new AuthorNotAuthorizedException(ExceptionMessage.Author_NOT_AUTHORIZED);
        }

        voteRepository.delete(vote);
    }
}

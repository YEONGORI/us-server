package us.usserver.vote.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import us.usserver.author.Author;
import us.usserver.global.exception.VoteNotFoundException;
import us.usserver.paragraph.Paragraph;
import us.usserver.vote.Vote;
import us.usserver.vote.VoteRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VoteRepositoryImpl implements VoteRepository {
    private final VoteJpaRepository voteJpaRepository;

    @Override
    public Vote getVoteById(Long voteId) {
        return null;
    }

    @Override
    public List<Vote> findAllByAuthor(Author author) {
        return voteJpaRepository.findAllByAuthor(author);
    }

    @Override
    public Integer countAllByParagraph(Paragraph paragraph) {
        return null;
    }
}

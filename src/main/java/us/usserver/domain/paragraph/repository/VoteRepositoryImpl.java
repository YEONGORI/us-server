package us.usserver.domain.paragraph.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import us.usserver.domain.author.entity.Author;
import us.usserver.domain.paragraph.entity.Paragraph;
import us.usserver.domain.paragraph.entity.Vote;

import java.util.List;

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

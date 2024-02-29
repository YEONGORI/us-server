package us.usserver.domain.paragraph.repository;

import org.springframework.stereotype.Repository;
import us.usserver.domain.member.entity.Author;
import us.usserver.domain.paragraph.entity.Paragraph;
import us.usserver.domain.paragraph.entity.Vote;

import java.util.List;

@Repository
public interface VoteRepository {
    Vote getVoteById(Long voteId);

    List<Vote> findAllByAuthor(Author author);

    Integer countAllByParagraph(Paragraph paragraph);
}

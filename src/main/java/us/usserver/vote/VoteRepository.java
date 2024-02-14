package us.usserver.vote;

import org.springframework.stereotype.Repository;
import us.usserver.author.Author;
import us.usserver.paragraph.Paragraph;

import java.util.List;

@Repository
public interface VoteRepository {
    Vote getVoteById(Long voteId);

    List<Vote> findAllByAuthor(Author author);

    Integer countAllByParagraph(Paragraph paragraph);
}

package us.usserver.vote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.usserver.author.Author;
import us.usserver.paragraph.Paragraph;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> getVoteById(Long voteId);

    List<Vote> findAllByAuthor(Author author);

    int countAllByParagraph(Paragraph paragraph);
}

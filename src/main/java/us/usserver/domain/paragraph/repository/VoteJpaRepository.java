package us.usserver.domain.paragraph.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.usserver.domain.member.entity.Author;
import us.usserver.domain.paragraph.entity.Paragraph;
import us.usserver.domain.paragraph.entity.Vote;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteJpaRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> getVoteById(Long voteId);

    List<Vote> findAllByAuthor(Author author);

    int countAllByParagraph(Paragraph paragraph);
}

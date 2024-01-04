package us.usserver.vote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.usserver.paragraph.Paragraph;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    int countAllByParagraph(Paragraph paragraph);
}

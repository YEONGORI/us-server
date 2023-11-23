package us.usserver.like.paragraph;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.usserver.paragraph.Paragraph;

import java.util.List;

@Repository
public interface ParagraphLikeRepository extends JpaRepository<ParagraphLike, Long> {
    List<ParagraphLike> findAllByParagraph(Paragraph paragraph);
    Integer countAllByParagraph(Paragraph paragraph);
}

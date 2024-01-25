package us.usserver.like.paragraph;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.usserver.author.Author;
import us.usserver.like.novel.NovelLike;
import us.usserver.novel.Novel;
import us.usserver.paragraph.Paragraph;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParagraphLikeRepository extends JpaRepository<ParagraphLike, Long> {
    List<ParagraphLike> findAllByParagraph(Paragraph paragraph);

    List<ParagraphLike> findAllByAuthor(Author author);

    Optional<ParagraphLike> findFirstByParagraphAndAuthor(Paragraph paragraph, Author author);
}

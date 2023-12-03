package us.usserver.paragraph;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.usserver.chapter.Chapter;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParagraphRepository extends JpaRepository<Paragraph, Long> {
    Optional<Paragraph> getParagraphById(Long paragraphId);

    List<Paragraph> findAllByChapter(Chapter chapter);

    Integer countParagraphsByChapter(Chapter chapter);
}

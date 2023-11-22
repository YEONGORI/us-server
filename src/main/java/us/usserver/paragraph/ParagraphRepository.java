package us.usserver.paragraph;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.usserver.chapter.Chapter;

import java.util.List;

@Repository
public interface ParagraphRepository extends JpaRepository<Paragraph, Long> {
    List<Paragraph> findAllByChapter(Chapter chapter);
}

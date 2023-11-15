package us.usserver.chapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.usserver.novel.Novel;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    List<Chapter> findAllByNovel(Novel novel);

    Optional<Chapter> getChapterById(Long chapterId);

    Integer getChapter
}

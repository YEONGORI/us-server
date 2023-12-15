package us.usserver.score;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import us.usserver.author.Author;
import us.usserver.chapter.Chapter;

import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    Optional<Score> findScoreByAuthorAndChapter(Author author, Chapter chapter);

    @Query("SELECT AVG(s.score) FROM Score s WHERE s.chapter = :chapter")
    double findAverageScoreByChapter(Chapter chapter);
}

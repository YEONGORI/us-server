package us.usserver.novel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NovelRepository extends JpaRepository<Novel, Long> {
    Optional<Novel> getNovelById(Long id);

    @Query("select n from Novel n " +
            "order by n.todayHit desc " +
            "limit 6")
    List<Novel> getRealTimeNovels();

    @Query("select n from Novel n " +
            "where n.createdAt BETWEEN DATE_ADD(NOW(), INTERVAL -14 DAY ) AND NOW() " +
            "order by n.title asc " +
            "limit 6")
    List<Novel> getNewNovels();
}

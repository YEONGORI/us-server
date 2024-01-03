package us.usserver.like.novel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.usserver.novel.Novel;

import java.util.List;

@Repository
public interface NovelLikeRepository extends JpaRepository<NovelLike, Long> {
    List<NovelLike> findAllByNovel(Novel novel);

}

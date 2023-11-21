package us.usserver.like.novel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NovelLikeRepository extends JpaRepository<NovelLike, Long> {
}

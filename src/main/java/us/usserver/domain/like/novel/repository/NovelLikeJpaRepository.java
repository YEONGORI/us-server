package us.usserver.domain.like.novel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.usserver.domain.member.entity.Author;
import us.usserver.domain.like.novel.NovelLike;
import us.usserver.domain.novel.Novel;

import java.util.List;
import java.util.Optional;

@Repository
public interface NovelLikeJpaRepository extends JpaRepository<NovelLike, Long> {
    List<NovelLike> findAllByNovel(Novel novel);

    List<NovelLike> findAllByAuthor(Author author);

    Optional<NovelLike> findFirstByNovelAndAuthor(Novel novel, Author author);

    Integer countAllByNovel(Novel novel);
}

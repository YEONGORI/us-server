package us.usserver.like.novel.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.usserver.author.Author;
import us.usserver.like.novel.NovelLike;
import us.usserver.novel.Novel;

import java.util.List;
import java.util.Optional;

public interface NovelLikeJpaRepository extends JpaRepository<NovelLike, Long> {
    List<NovelLike> findAllByNovel(Novel novel);

    List<NovelLike> findAllByAuthor(Author author);

    Optional<NovelLike> findFirstByNovelAndAuthor(Novel novel, Author author);

    Integer countAllByNovel(Novel novel);
}

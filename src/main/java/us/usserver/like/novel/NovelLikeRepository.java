package us.usserver.like.novel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.usserver.author.Author;
import us.usserver.novel.Novel;

import java.util.List;
import java.util.Optional;

@Repository
public interface NovelLikeRepository extends JpaRepository<NovelLike, Long> {
    List<NovelLike> findAllByNovel(Novel novel);

    List<NovelLike> findAllByAuthor(Author author);

    Optional<NovelLike> findFirstByNovelAndAuthor(Novel novel, Author author);
}

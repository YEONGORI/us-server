package us.usserver.like.novel;

import us.usserver.author.Author;
import us.usserver.novel.Novel;

import java.util.List;
import java.util.Optional;

public interface NovelLikeRepository {
    NovelLike save(NovelLike novelLike);

    void delete(NovelLike novelLike);

    Integer countAllByNovel(Novel novel);

    Optional<NovelLike> findAnyByNovelAndAuthor(Novel novel, Author author);

    List<NovelLike> findAllByAuthor(Author author);
}

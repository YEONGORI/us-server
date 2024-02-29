package us.usserver.domain.like.novel.repository;

import us.usserver.domain.member.entity.Author;
import us.usserver.domain.like.novel.NovelLike;
import us.usserver.domain.novel.Novel;

import java.util.List;
import java.util.Optional;

public interface NovelLikeRepository {
    NovelLike save(NovelLike novelLike);

    void delete(NovelLike novelLike);

    Integer countAllByNovel(Novel novel);

    Optional<NovelLike> findAnyByNovelAndAuthor(Novel novel, Author author);

    List<NovelLike> findAllByAuthor(Author author);
}

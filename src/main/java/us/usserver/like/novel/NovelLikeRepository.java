package us.usserver.like.novel;

import us.usserver.author.Author;
import us.usserver.novel.Novel;

public interface NovelLikeRepository {
    NovelLike save(NovelLike novelLike);
    void delete(NovelLike novelLike);
    Integer countAllByNovel(Novel novel);
    NovelLike findAnyByNovelAndAuthor(Novel novel, Author author);
}

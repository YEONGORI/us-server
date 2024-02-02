package us.usserver.comment;

import us.usserver.chapter.Chapter;
import us.usserver.novel.Novel;

import java.util.List;

public interface CommentRepository {

    Comment save(Comment comment);

    void delete(Comment cOmment);

    Integer countByChapter(Chapter chapter);

    List<Comment> getTop3CommentOfChapter(Chapter chapter);
}

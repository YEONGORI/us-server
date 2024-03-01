package us.usserver.domain.comment.repository;

import us.usserver.domain.chapter.entity.Chapter;
import us.usserver.domain.comment.entity.Comment;

import java.util.List;

public interface CommentRepository {

    Comment save(Comment comment);

    void delete(Comment cOmment);

    Integer countByChapter(Chapter chapter);

    List<Comment> getTop3CommentOfChapter(Chapter chapter);
}

package us.usserver.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import us.usserver.author.Author;
import us.usserver.chapter.Chapter;
import us.usserver.comment.Comment;
import us.usserver.novel.Novel;

import java.util.List;
import java.util.Optional;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> getCommentById(Long commentId);

    Integer countAllByNovel(Novel novel);

    Integer countAllByChapter(Chapter chapter);

    List<Comment> findAllByNovel(Novel novel);

    List<Comment> findAllByChapter(Chapter chapter);

    List<Comment> findAllByAuthor(Author author);
}

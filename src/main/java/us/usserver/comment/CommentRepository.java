package us.usserver.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import us.usserver.author.Author;
import us.usserver.chapter.Chapter;
import us.usserver.novel.Novel;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> getCommentById(Long commentId);

    Integer countAllByNovel(Novel novel);

    Integer countAllByChapter(Chapter chapter);

    List<Comment> findAllByNovel(Novel novel);

    List<Comment> findAllByChapter(Chapter chapter);

    List<Comment> findAllByAuthor(Author author);
}

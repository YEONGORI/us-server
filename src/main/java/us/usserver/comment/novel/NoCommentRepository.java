package us.usserver.comment.novel;

import org.springframework.data.jpa.repository.JpaRepository;
import us.usserver.novel.Novel;

import java.util.List;

public interface NoCommentRepository extends JpaRepository<NoComment, Long> {
    Integer countAllByNovel(Novel novel);

    List<NoComment> getAllByNovel(Novel novel);
}

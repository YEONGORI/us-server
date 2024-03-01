package us.usserver.domain.comment.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import us.usserver.domain.chapter.entity.Chapter;
import us.usserver.domain.comment.entity.Comment;

import java.util.List;

import static us.usserver.chapter.QChapter.chapter;
import static us.usserver.comment.QComment.*;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {
    private final JPAQueryFactory queryFactory;
    private final CommentJpaRepository commentJpaRepository;

    @Override
    public Comment save(Comment comment) {
        return commentJpaRepository.save(comment);
    }

    @Override
    public void delete(Comment comment) {
        commentJpaRepository.delete(comment);
    }

    @Override
    public Integer countByChapter(Chapter chapter) {
        Integer commentCnt = commentJpaRepository.countAllByChapter(chapter);
        return commentCnt == null ? 0 : commentCnt;
    }

    @Override
    public List<Comment> getTop3CommentOfChapter(Chapter chapter) {
        return queryFactory
                .select(comment)
                .from(comment)
                .where(eqChapterId(chapter.getId()))
                .orderBy(commentLikes())
                .limit(3)
                .fetch();

    }

    private BooleanExpression eqChapterId(Long chapterId) {
        return  chapter.id.eq(chapterId);
    }

    public OrderSpecifier<?> commentLikes() {
        return new OrderSpecifier<>(Order.DESC, comment.commentLikes.size());
    }
}

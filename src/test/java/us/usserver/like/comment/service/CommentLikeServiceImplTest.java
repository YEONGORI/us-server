package us.usserver.like.comment.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import us.usserver.domain.like.comment.service.CommentLikeServiceImpl;

@SpringBootTest
class CommentLikeServiceImplTest {
    @Autowired
    private CommentLikeServiceImpl commentLikeService;

    @Test
    @DisplayName("좋아요 로직 테스트")
    void postLike() {
        // given

        // when
//        commentLikeService.postLike();

        // then
    }

    @Test
    @DisplayName("좋아요 취소 테스트")
    void deleteLike() {
    }
}
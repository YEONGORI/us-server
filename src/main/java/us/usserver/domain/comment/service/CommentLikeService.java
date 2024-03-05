package us.usserver.domain.comment.service;

import org.springframework.stereotype.Service;

@Service
public interface CommentLikeService {
    void postLike(Long commentId, Long authorId);
    void deleteLike(Long commentId, Long authorId);
}

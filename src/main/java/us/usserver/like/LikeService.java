package us.usserver.like;

import org.springframework.stereotype.Service;
import us.usserver.commentLike.CommentLike;
import us.usserver.like.novel.NovelLike;

@Service
public interface LikeService {
    void q(CommentLike commentLike);

    void q(NovelLike novelLike);
}

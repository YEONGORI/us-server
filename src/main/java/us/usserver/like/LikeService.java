package us.usserver.like;

import org.springframework.stereotype.Service;
import us.usserver.like.comment.ChCommentLike;
import us.usserver.like.novel.NovelLike;
import us.usserver.like.paragraph.ParagraphLike;

@Service
public interface LikeService {
    void setNovelLike(Long novelId);

    void setParagraphLike(Long paragraphId);

    void setChatperCommentLike(Long chCommentId);


}

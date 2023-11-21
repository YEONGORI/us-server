package us.usserver.like.novel;

import org.springframework.stereotype.Service;

@Service
public interface NovelLikeService {
    void setNovelLike(Long novelId, Long authorId);
}

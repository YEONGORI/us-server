package us.usserver.like.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import us.usserver.chapter.ChapterRepository;
import us.usserver.global.ExceptionMessage;
import us.usserver.global.NovelNotFoundException;
import us.usserver.like.LikeService;
import us.usserver.novel.Novel;
import us.usserver.novel.NovelRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeServiceV0 implements LikeService {
    private final NovelRepository novelRepository;
    private final ChapterRepository chapterRepository;

    @Override
    public void setNovelLike(Long novelId) {
        Optional<Novel> novelById = novelRepository.getNovelById(novelId);
        if (novelById.isEmpty()) {
            throw new NovelNotFoundException(ExceptionMessage.Novel_NOT_FOUND);
        }

        Novel novel = novelById.get();


    }

    @Override
    public void setParagraphLike(Long paragraphId) {

    }

    @Override
    public void setChatperCommentLike(Long chCommentId) {

    }
}

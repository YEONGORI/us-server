package us.usserver.like.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import us.usserver.author.Author;
import us.usserver.author.AuthorRepository;
import us.usserver.chapter.ChapterRepository;
import us.usserver.global.ExceptionMessage;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.global.exception.NovelNotFoundException;
import us.usserver.like.Like;
import us.usserver.like.LikeRepository;
import us.usserver.like.LikeService;
import us.usserver.like.novel.NovelLike;
import us.usserver.novel.Novel;
import us.usserver.novel.NovelRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeServiceV0 implements LikeService {
    private final NovelRepository novelRepository;
    private final AuthorRepository authorRepository;
    private final LikeRepository likeRepository;
    private final ChapterRepository chapterRepository;

    @Override
    public void setNovelLike(Long novelId, Long authorId) {
        Optional<Novel> novelById = novelRepository.getNovelById(novelId);
        Optional<Author> authorById = authorRepository.getAuthorById(authorId);
        if (novelById.isEmpty()) {
            throw new NovelNotFoundException(ExceptionMessage.Novel_NOT_FOUND);
        }
        if (authorById.isEmpty()) {
            throw new AuthorNotFoundException(ExceptionMessage.Author_NOT_FOUND);
        }

        Novel novel = novelById.get();
        Author author = authorById.get();
    }

    @Override
    public void setParagraphLike(Long paragraphId) {

    }

    @Override
    public void setChatperCommentLike(Long chCommentId) {

    }
}

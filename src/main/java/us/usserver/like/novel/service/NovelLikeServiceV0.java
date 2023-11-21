package us.usserver.like.novel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import us.usserver.author.Author;
import us.usserver.author.AuthorRepository;
import us.usserver.chapter.ChapterRepository;
import us.usserver.global.ExceptionMessage;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.global.exception.NovelNotFoundException;
import us.usserver.like.novel.NovelLike;
import us.usserver.like.novel.NovelLikeRepository;
import us.usserver.like.novel.NovelLikeService;
import us.usserver.novel.Novel;
import us.usserver.novel.NovelRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NovelLikeServiceV0 implements NovelLikeService {
    private final NovelRepository novelRepository;
    private final AuthorRepository authorRepository;
    private final NovelLikeRepository novelLikeRepository;

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
        NovelLike novelLike = NovelLike
                .builder()
                .novel(novel)
                .author(author)
                .build();
        novelLikeRepository.save(novelLike);
    }

}

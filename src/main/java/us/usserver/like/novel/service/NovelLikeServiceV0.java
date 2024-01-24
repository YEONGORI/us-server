package us.usserver.like.novel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.author.Author;
import us.usserver.global.EntityService;
import us.usserver.global.exception.DuplicatedLikeException;
import us.usserver.like.novel.NovelLike;
import us.usserver.like.novel.NovelLikeRepository;
import us.usserver.like.novel.repository.NovelLikeJpaRepository;
import us.usserver.like.novel.NovelLikeService;
import us.usserver.novel.Novel;

@Service
@Transactional
@RequiredArgsConstructor
public class NovelLikeServiceV0 implements NovelLikeService {
    private final EntityService entityService;
    private final NovelLikeJpaRepository novelLikeJpaRepository;
    private final NovelLikeRepository novelLikeRepository;

    @Override
    @CacheEvict(cacheNames = "novelLikeCnt", key = "#novelId")
    public void setNovelLike(Long novelId, Long authorId) {
        Novel novel = entityService.getNovel(novelId);
        Author author = entityService.getAuthor(authorId);

        if (novelLikeRepository.findAnyByNovelAndAuthor(novel, author) == null) {
            throw new DuplicatedLikeException("NOVEL_LIKE");
        }

        NovelLike novelLike = NovelLike
                .builder()
                .novel(novel)
                .author(author)
                .build();
        novelLikeRepository.save(novelLike);
    }

    @Override
    @CacheEvict(cacheNames = "novelLikeCnt", key = "#novelId")
    public void deleteNovelLike(Long novelId, Long authorId) {
        Novel novel = entityService.getNovel(novelId);
        Author author = entityService.getAuthor(authorId);

        NovelLike novelLike = novelLikeRepository.findAnyByNovelAndAuthor(novel, author);
        if (novelLike != null) {
            novelLikeRepository.delete(novelLike);
        }
    }
}

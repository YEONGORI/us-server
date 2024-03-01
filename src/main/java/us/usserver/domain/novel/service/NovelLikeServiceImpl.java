package us.usserver.domain.novel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.domain.novel.repository.NovelLikeRepository;
import us.usserver.domain.author.entity.Author;
import us.usserver.domain.novel.entity.Novel;
import us.usserver.global.EntityFacade;
import us.usserver.global.ExceptionMessage;
import us.usserver.global.exception.DuplicatedLikeException;
import us.usserver.domain.novel.entity.NovelLike;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class NovelLikeServiceImpl implements NovelLikeService {
    private final EntityFacade entityFacade;
    private final NovelLikeRepository novelLikeRepository;

    @Override
//    @CacheEvict(cacheNames = "novelLikeCnt", key = "#novelId")
    public void setNovelLike(Long novelId, Long authorId) {
        Novel novel = entityFacade.getNovel(novelId);
        Author author = entityFacade.getAuthor(authorId);

        if (novelLikeRepository.findFirstByNovelAndAuthor(novel, author).isPresent()) {
            throw new DuplicatedLikeException(ExceptionMessage.LIKE_DUPLICATED);
        }

        NovelLike novelLike = NovelLike
                .builder()
                .novel(novel)
                .author(author)
                .build();
        novelLikeRepository.save(novelLike);
    }

    @Override
//    @CacheEvict(cacheNames = "novelLikeCnt", key = "#novelId")
    public void deleteNovelLike(Long novelId, Long authorId) {
        Novel novel = entityFacade.getNovel(novelId);
        Author author = entityFacade.getAuthor(authorId);

        Optional<NovelLike> novelLike = novelLikeRepository.findFirstByNovelAndAuthor(novel, author);
        novelLike.ifPresent(novelLikeRepository::delete);
    }
}

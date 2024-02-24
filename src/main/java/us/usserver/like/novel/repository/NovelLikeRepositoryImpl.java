package us.usserver.like.novel.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import us.usserver.author.Author;
import us.usserver.like.novel.NovelLike;
import us.usserver.like.novel.NovelLikeRepository;
import us.usserver.novel.Novel;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NovelLikeRepositoryImpl implements NovelLikeRepository {
    private final NovelLikeJpaRepository novelLikeJpaRepository;

    @Override
    public NovelLike save(NovelLike novelLike) {
        return novelLikeJpaRepository.save(novelLike);
    }

    @Override
    public void delete(NovelLike novelLike) {
        novelLikeJpaRepository.delete(novelLike);
    }

    @Override
//    @Cacheable(cacheNames = "novelLikeCnt", key = "#novel.id")
    public Integer countAllByNovel(Novel novel) {
        return novelLikeJpaRepository.countAllByNovel(novel);
    }

    @Override
    public Optional<NovelLike> findAnyByNovelAndAuthor(Novel novel, Author author) {
        return novelLikeJpaRepository.findFirstByNovelAndAuthor(novel, author);
    }

    @Override
    public List<NovelLike> findAllByAuthor(Author author) {
        return novelLikeJpaRepository.findAllByAuthor(author);
    }
}

package us.usserver.domain.author.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.domain.author.entity.ReadNovel;
import us.usserver.domain.novel.repository.NovelLikeRepository;
import us.usserver.domain.author.entity.Author;
import us.usserver.domain.authority.entity.Authority;
import us.usserver.domain.authority.repository.AuthorityRepository;
import us.usserver.domain.author.dto.res.BookshelfDefaultResponse;
import us.usserver.domain.author.dto.NovelPreview;
import us.usserver.global.EntityFacade;
import us.usserver.domain.novel.entity.NovelLike;
import us.usserver.domain.novel.entity.Novel;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookshelfServiceImpl implements BookshelfService {
    private final EntityFacade entityFacade;
    private final AuthorityRepository authorityRepository;
    private final NovelLikeRepository novelLikeRepository;

    @Override
    public BookshelfDefaultResponse recentViewedNovels(Long authorId) {
        Author author = entityFacade.getAuthor(authorId);
        Set<ReadNovel> readNovels = author.getReadNovels();

        List<NovelPreview> novelPreviews = readNovels.stream()
                .map(ReadNovel::getNovel)
                .map(novel -> NovelPreview.fromNovel(novel, getTotalJoinedAuthor(novel), getShortcuts(novel)))
                .toList();

        return BookshelfDefaultResponse.builder().novelPreviews(novelPreviews).count(readNovels.size()).build();
    }

    @Override
    public void deleteRecentViewedNovels(Long authorId, Long readNovelId) {
        Author author = entityFacade.getAuthor(authorId);
        ReadNovel readNovel = entityFacade.getReadNovel(readNovelId);
        author.deleteReadNovel(readNovel);
    }

    @Override
    public BookshelfDefaultResponse createdNovels(Long authorId) {
        Author author = entityFacade.getAuthor(authorId);

        List<Novel> createdNovels = author.getCreatedNovels();
        List<NovelPreview> novelPreviews = createdNovels.stream()
                .map(novel -> NovelPreview.fromNovel(
                        novel,
                        getTotalJoinedAuthor(novel),
                        getShortcuts(novel)
                )).toList();

        return BookshelfDefaultResponse.builder().novelPreviews(novelPreviews).build();
    }

    @Override
    public void deleteCreatedNovels(Long authorId, Long novelId) {
    }

    @Override
    public BookshelfDefaultResponse joinedNovels(Long authorId) {
        Author author = entityFacade.getAuthor(authorId);

        List<Authority> authorities = authorityRepository.findAllByAuthor(author);
        List<NovelPreview> novelPreviews = authorities.stream()
                .map(authority -> NovelPreview.fromNovel(
                        authority.getNovel(),
                        getTotalJoinedAuthor(authority.getNovel()),
                        getShortcuts(authority.getNovel())
                )).toList();

        return BookshelfDefaultResponse.builder().novelPreviews(novelPreviews).build();
    }

    @Override
    public void deleteJoinedNovels(Long authorId, Long novelId) {

    }

    @Override
    public BookshelfDefaultResponse likedNovels(Long authorId) {
        Author author = entityFacade.getAuthor(authorId);

        List<NovelLike> novelLikes = novelLikeRepository.findAllByAuthor(author);
        List<NovelPreview> novelPreviews = novelLikes.stream()
                .map(likedNovel -> NovelPreview.fromNovel(
                        likedNovel.getNovel(),
                        getTotalJoinedAuthor(likedNovel.getNovel()),
                        getShortcuts(likedNovel.getNovel())
                )).toList();

        return BookshelfDefaultResponse.builder().novelPreviews(novelPreviews).build();
    }

    @Override
    public void deleteLikedNovels(Long authorId, Long novelId) {
        Author author = entityFacade.getAuthor(authorId);
        Novel novel = entityFacade.getNovel(novelId);

        Optional<NovelLike> novelLike = novelLikeRepository.findFirstByNovelAndAuthor(novel, author);
        novelLike.ifPresent(novelLikeRepository::delete);
    }

    private Integer getTotalJoinedAuthor(Novel novel) {
        return authorityRepository.countAllByNovel(novel);
    }

    private String getShortcuts(Novel novel) { // TODO: 이후 URL에 따라 수정
        return "http://localhost:8080/novel/" + novel.getId();
    }
}

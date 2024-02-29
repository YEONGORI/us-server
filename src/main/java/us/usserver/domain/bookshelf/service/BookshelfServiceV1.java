package us.usserver.domain.bookshelf.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.domain.member.entity.Author;
import us.usserver.domain.authority.Authority;
import us.usserver.domain.authority.repository.AuthorityRepository;
import us.usserver.domain.bookshelf.dto.BookshelfDefaultResponse;
import us.usserver.domain.bookshelf.dto.NovelPreview;
import us.usserver.global.EntityService;
import us.usserver.domain.like.novel.NovelLike;
import us.usserver.domain.like.novel.repository.NovelLikeRepository;
import us.usserver.domain.novel.Novel;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookshelfServiceV1 implements BookshelfService {
    private final EntityService entityService;
    private final AuthorityRepository authorityRepository;
    private final NovelLikeRepository novelLikeRepository;

    @Override
    public BookshelfDefaultResponse recentViewedNovels(Long authorId) {
        Author author = entityService.getAuthor(authorId);
        List<Novel> viewedNovels = author.getViewedNovels();

        List<NovelPreview> novelPreviews = viewedNovels.stream()
                .map(novel -> NovelPreview.fromNovel(
                        novel,
                        getTotalJoinedAuthor(novel),
                        getShortcuts(novel)
                )).toList();

        return BookshelfDefaultResponse.builder().novelPreviews(novelPreviews).build();
    }

    @Override
    public void deleteRecentViewedNovels(Long authorId, Long novelId) {
        Author author = entityService.getAuthor(authorId);
        Novel novel = entityService.getNovel(novelId);

        author.getViewedNovels().remove(novel);
    }

    @Override
    public BookshelfDefaultResponse createdNovels(Long authorId) {
        Author author = entityService.getAuthor(authorId);

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
        Author author = entityService.getAuthor(authorId);

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
        Author author = entityService.getAuthor(authorId);

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
        Author author = entityService.getAuthor(authorId);
        Novel novel = entityService.getNovel(novelId);

        Optional<NovelLike> novelLike = novelLikeRepository.findAnyByNovelAndAuthor(novel, author);
        novelLike.ifPresent(novelLikeRepository::delete);
    }

    private Integer getTotalJoinedAuthor(Novel novel) {
        return authorityRepository.countAllByNovel(novel);
    }

    private String getShortcuts(Novel novel) { // TODO: 이후 URL에 따라 수정
        return "http://localhost:8080/novel/" + novel.getId();
    }
}

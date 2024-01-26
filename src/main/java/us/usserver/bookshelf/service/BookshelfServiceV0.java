package us.usserver.bookshelf.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.author.Author;
import us.usserver.authority.Authority;
import us.usserver.authority.AuthorityRepository;
import us.usserver.bookshelf.BookshelfService;
import us.usserver.bookshelf.dto.NovelPreview;
import us.usserver.global.EntityService;
import us.usserver.like.novel.NovelLike;
import us.usserver.like.novel.NovelLikeRepository;
import us.usserver.novel.Novel;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookshelfServiceV0 {
    private final EntityService entityService;
    private final AuthorityRepository authorityRepository;
    private final NovelLikeRepository novelLikeRepository;

//    @Override
    public List<NovelPreview> recentViewedNovels(Long authorId) {
        Author author = entityService.getAuthor(authorId);
        List<Novel> viewedNovels = author.getViewedNovels();

        return viewedNovels.stream()
                .map(novel -> NovelPreview.fromNovel(
                        novel,
                        getTotalJoinedAuthor(novel),
                        getShortcuts(novel)
                )).toList();
    }

//    @Override
    public void deleteRecentViewedNovels(Long authorId, Long novelId) {

    }

//    @Override
    public List<NovelPreview> createdNovels(Long authorId) {
        Author author = entityService.getAuthor(authorId);

        List<Novel> createdNovels = author.getCreatedNovels();
        return createdNovels.stream()
                .map(novel -> NovelPreview.fromNovel(
                        novel,
                        getTotalJoinedAuthor(novel),
                        getShortcuts(novel)
                )).toList();
    }

//    @Override
    public void deleteCreatedNovels(Long authorId, Long novelId) {

    }

//    @Override
    public List<NovelPreview> joinedNovels(Long authorId) {
        Author author = entityService.getAuthor(authorId);

        List<Authority> authorities = authorityRepository.findAllByAuthor(author);
        return authorities.stream()
                .map(authority -> NovelPreview.fromNovel(
                        authority.getNovel(),
                        getTotalJoinedAuthor(authority.getNovel()),
                        getShortcuts(authority.getNovel())
                )).toList();
    }

//    @Override
    public void deleteJoinedNovels(Long authorId, Long novelId) {

    }

//    @Override
    public List<NovelPreview> likedNovels(Long authorId) {
        Author author = entityService.getAuthor(authorId);

        List<NovelLike> novelLikes = novelLikeRepository.findAllByAuthor(author);
        return novelLikes.stream()
                .map(likedNovel -> NovelPreview.fromNovel(
                        likedNovel.getNovel(),
                        getTotalJoinedAuthor(likedNovel.getNovel()),
                        getShortcuts(likedNovel.getNovel())
                )).toList();
    }

//    @Override
    public void deleteLikedNovels(Long authorId, Long novelId) {

    }

    private Integer getTotalJoinedAuthor(Novel novel) {
        return authorityRepository.countAllByNovel(novel);
    }

    private String getShortcuts(Novel novel) { // TODO: 이후 URL에 따라 수정
        return "http://localhost:8080/novel/" + novel.getId();
    }
}

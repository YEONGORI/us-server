package us.usserver.domain.bookshelf.service;

import org.springframework.stereotype.Service;
import us.usserver.domain.bookshelf.dto.BookshelfDefaultResponse;

@Service
public interface BookshelfService {
    BookshelfDefaultResponse recentViewedNovels(Long authorId);
    void deleteRecentViewedNovels(Long authorId, Long novelId);

    BookshelfDefaultResponse createdNovels(Long authorId);
    void deleteCreatedNovels(Long authorId, Long novelId);

    BookshelfDefaultResponse joinedNovels(Long authorId);
    void deleteJoinedNovels(Long authorId, Long novelId);

    BookshelfDefaultResponse likedNovels(Long authorId);
    void deleteLikedNovels(Long authorId, Long novelId);
}

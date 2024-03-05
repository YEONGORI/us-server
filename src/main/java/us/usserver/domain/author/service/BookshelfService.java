package us.usserver.domain.author.service;

import org.springframework.stereotype.Service;
import us.usserver.domain.author.dto.res.BookshelfDefaultResponse;

@Service
public interface BookshelfService {
    BookshelfDefaultResponse recentViewedNovels(Long authorId);
    void deleteRecentViewedNovels(Long authorId, Long readNovelId);

    BookshelfDefaultResponse createdNovels(Long authorId);
    void deleteCreatedNovels(Long authorId, Long novelId);

    BookshelfDefaultResponse joinedNovels(Long authorId);
    void deleteJoinedNovels(Long authorId, Long novelId);

    BookshelfDefaultResponse likedNovels(Long authorId);
    void deleteLikedNovels(Long authorId, Long novelId);
}

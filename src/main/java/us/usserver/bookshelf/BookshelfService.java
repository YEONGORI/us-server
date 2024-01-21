package us.usserver.bookshelf;

import org.springframework.stereotype.Service;
import us.usserver.bookshelf.dto.BookshelfDefaultResponse;
import us.usserver.bookshelf.dto.NovelPreview;

import java.util.List;

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

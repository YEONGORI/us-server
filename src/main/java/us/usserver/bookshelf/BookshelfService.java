package us.usserver.bookshelf;

import org.springframework.stereotype.Service;
import us.usserver.bookshelf.dto.NovelPreview;

import java.util.List;

@Service
public interface BookshelfService {
    List<NovelPreview> recentViewedNovels(Long authorId);
    void deleteRecentViewedNovels(Long authorId, Long novelId);

    List<NovelPreview> createdNovels(Long authorId);
    void deleteCreatedNovels(Long authorId, Long novelId);

    List<NovelPreview> joinedNovels(Long authorId);
    void deleteJoinedNovels(Long authorId, Long novelId);

    List<NovelPreview> likedNovels(Long authorId);
    void deleteLikedNovels(Long authorId, Long novelId);
}

package us.usserver.bookshelf;

import org.springframework.stereotype.Service;
import us.usserver.bookshelf.dto.NovelPreview;

import java.util.List;

@Service
public interface BookshelfService {
    List<NovelPreview> recentViewedNovels(Long authorId);

    List<NovelPreview> createdNovels(Long authorId);

    List<NovelPreview> joinedNovels(Long authorId);

    List<NovelPreview> likedNovels(Long authorId);
}

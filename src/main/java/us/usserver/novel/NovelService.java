package us.usserver.novel;

import org.springframework.stereotype.Service;
import us.usserver.novel.dto.AuthorDescription;
import us.usserver.novel.dto.DetailInfoResponse;
import us.usserver.novel.dto.NovelInfoResponse;
import us.usserver.novel.dto.NovelSynopsis;

@Service
public interface NovelService {
    NovelInfoResponse getNovelInfo(Long novelId);

    DetailInfoResponse getNovelDetailInfo(Long novelId);

    NovelSynopsis modifyNovelSynopsis(Long novelId, Long authorId, NovelSynopsis req);

    AuthorDescription modifyAuthorDescription(Long novelId, Long authorId, AuthorDescription req);
}

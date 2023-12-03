package us.usserver.novel;

import org.springframework.stereotype.Service;
import us.usserver.novel.dto.DetailInfoResponse;
import us.usserver.novel.dto.NovelCreateDto;
import us.usserver.novel.dto.NovelInfoResponse;

@Service
public interface NovelService {
    NovelInfoResponse getNovelInfo(Long novelId);

    DetailInfoResponse getNovelDetailInfo(Long novelId);

    Novel createNovel(NovelCreateDto novelCreateDto);
}

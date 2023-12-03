package us.usserver.novel;

import org.springframework.stereotype.Service;
import us.usserver.novel.dto.*;

@Service
public interface NovelService {
    NovelInfoResponse getNovelInfo(Long novelId);

    DetailInfoResponse getNovelDetailInfo(Long novelId);

    Novel createNovel(CreateNovelReq createNovelReq);

    HomeNovelListResponse homeNovelInfo(Long memberId);

    NovelPageInfoResponse moreNovel(MoreInfoOfNovel novelMoreDto);

    NovelPageInfoResponse searchNovel(SearchNovelReq searchNovelReq);

    SearchKeywordResponse searchKeyword();

    void deleteSearchKeyword();
}

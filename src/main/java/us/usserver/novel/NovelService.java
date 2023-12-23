package us.usserver.novel;

import org.springframework.stereotype.Service;
import us.usserver.novel.dto.AuthorDescription;
import us.usserver.novel.dto.NovelDetailInfo;
import us.usserver.novel.dto.NovelInfo;
import us.usserver.novel.dto.NovelSynopsis;
import us.usserver.novel.dto.*;

@Service
public interface NovelService {
    NovelInfo getNovelInfo(Long novelId);

    NovelDetailInfo getNovelDetailInfo(Long novelId);

    NovelSynopsis modifyNovelSynopsis(Long novelId, Long authorId, NovelSynopsis req);

    AuthorDescription modifyAuthorDescription(Long novelId, Long authorId, AuthorDescription req);

    Novel createNovel(CreateNovelReq createNovelReq);

    HomeNovelListResponse homeNovelInfo();

    NovelPageInfoResponse moreNovel(MoreInfoOfNovel moreInfoOfNovel);

    NovelPageInfoResponse readMoreNovel(ReadInfoOfNovel readInfoOfNovel);

    NovelPageInfoResponse searchNovel(SearchNovelReq searchNovelReq);

    SearchKeywordResponse searchKeyword();

    void deleteSearchKeyword();
}

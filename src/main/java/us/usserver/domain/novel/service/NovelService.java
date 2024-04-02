package us.usserver.domain.novel.service;

import org.springframework.stereotype.Service;
import us.usserver.domain.novel.dto.*;
import us.usserver.domain.novel.dto.req.MoreNovelReq;
import us.usserver.domain.novel.dto.req.NovelBlueprint;
import us.usserver.domain.novel.dto.req.SearchKeyword;
import us.usserver.domain.novel.dto.res.MainPageRes;
import us.usserver.domain.novel.dto.res.MoreNovelRes;
import us.usserver.domain.novel.dto.res.SearchNovelRes;
import us.usserver.domain.novel.dto.res.SearchPageRes;


@Service
public interface NovelService {
    NovelInfo getNovelInfo(Long novelId);

    NovelDetailInfo getNovelDetailInfo(Long novelId);

    String modifyNovelSynopsis(Long novelId, Long memberId, String synopsis);

    AuthorDescription modifyAuthorDescription(Long novelId, Long memberId, AuthorDescription description);

    NovelInfo createNovel(Long memberId, NovelBlueprint novelBlueprint);

    MainPageRes getMainPage(Long memberId);

    MoreNovelRes getMoreNovels(Long memberId, MoreNovelReq moreNovelReq);

    MoreNovelRes readMoreNovel(Long memberId);
}

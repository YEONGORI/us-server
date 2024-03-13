package us.usserver.domain.novel.service;

import org.springframework.stereotype.Service;
import us.usserver.domain.member.entity.Member;
import us.usserver.domain.novel.dto.*;


@Service
public interface NovelService {
    NovelInfo getNovelInfo(Long novelId);

    NovelDetailInfo getNovelDetailInfo(Long novelId);

    String modifyNovelSynopsis(Long novelId, Long memberId, String synopsis);

    AuthorDescription modifyAuthorDescription(Long novelId, Long memberId, AuthorDescription description);

    NovelInfo createNovel(Long memberId, NovelBlueprint novelBlueprint);

    MainPageResponse getMainPage(Long memberId);

    MoreNovelResponse getMoreNovels(Long memberId, MoreNovelRequest moreNovelRequest);

    MoreNovelResponse readMoreNovel(Long memberId);

    NovelPageInfoResponse searchNovel(Long memberId, SearchNovelReq searchNovelReq);

    SearchKeywordResponse searchKeyword(Member member);

    void deleteSearchKeyword(Member member);
}

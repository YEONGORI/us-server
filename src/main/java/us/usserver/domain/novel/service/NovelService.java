package us.usserver.domain.novel.service;

import org.springframework.stereotype.Service;
import us.usserver.domain.member.entity.Member;
import us.usserver.domain.novel.dto.*;


@Service
public interface NovelService {
    NovelInfo getNovelInfo(java.lang.Long novelId);

    NovelDetailInfo getNovelDetailInfo(java.lang.Long novelId);

    String modifyNovelSynopsis(java.lang.Long novelId, java.lang.Long authorId, String synopsis);

    AuthorDescription modifyAuthorDescription(java.lang.Long novelId, java.lang.Long authorId, AuthorDescription description);

    NovelInfo createNovel(Member member, NovelBlueprint novelBlueprint);

    MainPageResponse getMainPage(Member member);

    MoreNovelResponse getMoreNovels(Member member, MoreNovelRequest moreNovelRequest);

    MoreNovelResponse readMoreNovel(Member member);

    NovelPageInfoResponse searchNovel(Member member, SearchNovelReq searchNovelReq);

    SearchKeywordResponse searchKeyword(Member member);

    void deleteSearchKeyword(Member member);
}

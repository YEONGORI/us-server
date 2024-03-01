package us.usserver.domain.novel.service;

import org.springframework.stereotype.Service;
import us.usserver.domain.member.entity.Member;
import us.usserver.domain.novel.dto.*;


@Service
public interface NovelService {
    NovelInfo getNovelInfo(Long novelId);

    NovelDetailInfo getNovelDetailInfo(Long novelId);

    String modifyNovelSynopsis(Long novelId, Long authorId, String synopsis);

    AuthorDescription modifyAuthorDescription(Long novelId, Long authorId, AuthorDescription description);

    NovelInfo createNovel(Member member, CreateNovelReq createNovelReq);

    MainPageResponse getMainPage(Member member);

    NovelPageInfoResponse getMoreNovels(MoreInfoOfNovel moreInfoOfNovel);

    NovelPageInfoResponse readMoreNovel(Member member, ReadInfoOfNovel readInfoOfNovel);

    NovelPageInfoResponse searchNovel(Member member, SearchNovelReq searchNovelReq);

    SearchKeywordResponse searchKeyword(Member member);

    void deleteSearchKeyword(Member member);
}

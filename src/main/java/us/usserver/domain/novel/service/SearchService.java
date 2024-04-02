package us.usserver.domain.novel.service;

import org.springframework.stereotype.Service;
import us.usserver.domain.novel.dto.req.SearchKeyword;
import us.usserver.domain.novel.dto.res.SearchNovelRes;
import us.usserver.domain.novel.dto.res.SearchPageRes;

@Service
public interface SearchService {
    SearchNovelRes searchNovel(Long memberId, SearchKeyword searchKeyword);

    SearchPageRes getSearchPage(Long memberId);

    void deleteSearchKeyword(Long memberId);
}

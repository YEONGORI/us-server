package us.usserver.novel.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import us.usserver.novel.Novel;
import us.usserver.novel.dto.MoreInfoOfNovel;
import us.usserver.novel.dto.SearchNovelReq;

public interface NovelCustomRepository {
    Slice<Novel> searchNovelList(SearchNovelReq searchNovelReq, Pageable pageable);
    Slice<Novel> moreNovelList(MoreInfoOfNovel novelMoreDto, Pageable pageable);
}

package us.usserver.novel;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;
import us.usserver.novel.Novel;
import us.usserver.novel.dto.MoreInfoOfNovel;
import us.usserver.novel.dto.SearchNovelReq;

@Repository
public interface NovelRepository {
    Slice<Novel> searchNovelList(SearchNovelReq searchNovelReq, Pageable pageable);
    Slice<Novel> moreNovelList(MoreInfoOfNovel novelMoreDto, Pageable pageable);
    Novel save(Novel novel);
}

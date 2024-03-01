package us.usserver.domain.novel.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.NoRepositoryBean;
import us.usserver.domain.novel.entity.Novel;
import us.usserver.domain.novel.dto.MoreInfoOfNovel;
import us.usserver.domain.novel.dto.SearchNovelReq;

@NoRepositoryBean
public interface NovelRepositoryCustom {
    Slice<Novel> searchNovelList(SearchNovelReq searchNovelReq, Pageable pageable);
    Slice<Novel> moreNovelList(MoreInfoOfNovel novelMoreDto, Pageable pageable);
}

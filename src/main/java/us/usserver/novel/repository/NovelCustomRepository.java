package us.usserver.novel.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import us.usserver.novel.Novel;
import us.usserver.novel.dto.MoreInfoOfNovel;

public interface NovelCustomRepository {
    Slice<Novel> moreNovelList(MoreInfoOfNovel novelMoreDto, Pageable pageable);
}

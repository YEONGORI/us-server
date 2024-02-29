package us.usserver.domain.chapter.service;

import org.springframework.stereotype.Service;
import us.usserver.domain.chapter.dto.ChapterDetailInfo;
import us.usserver.domain.chapter.dto.ChapterInfo;
import us.usserver.domain.novel.Novel;

import java.util.List;

@Service
public interface ChapterService {

    List<ChapterInfo> getChaptersOfNovel(Novel novel);

    ChapterDetailInfo getChapterDetailInfo(Long novelId, Long authorId, Long chapterId);

    void createChapter(Long novelId, Long authorId);
}

package us.usserver.chapter;

import org.springframework.stereotype.Service;
import us.usserver.chapter.dto.ChapterDetailInfo;
import us.usserver.chapter.dto.ChapterInfo;
import us.usserver.novel.Novel;

import java.util.List;

@Service
public interface ChapterService {

    List<ChapterInfo> getChaptersOfNovel(Novel novel);

    ChapterDetailInfo getChapterDetailInfo(Long chapterId);

    void createChapter(Long novelId, Long authorId);
}

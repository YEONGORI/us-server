package us.usserver.chapter;

import org.springframework.stereotype.Service;
import us.usserver.chapter.dto.ChapterDetailRes;
import us.usserver.chapter.dto.ChaptersOfNovel;
import us.usserver.chapter.dto.CreateChapterReq;
import us.usserver.chapter.dto.CreateChatperRes;

import java.util.List;

@Service
public interface ChapterService {
    List<ChaptersOfNovel> getChaptersOfNovel(Long novelId);

    ChapterDetailRes getChapterDetail(Long novelId, Long chapterId);

    void createChapter(Long novelId, CreateChapterReq req);
}

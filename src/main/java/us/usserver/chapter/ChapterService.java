package us.usserver.chapter;

import org.springframework.stereotype.Service;
import us.usserver.chapter.dto.ChaptersOfNovel;

import java.util.List;

@Service
public interface ChapterService {
    List<ChaptersOfNovel> getChaptersOfNovel(Long novelId);

    void createChapter(Long novelId, Long authorId);
}

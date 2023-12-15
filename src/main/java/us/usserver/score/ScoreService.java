package us.usserver.score;

import org.springframework.stereotype.Service;
import us.usserver.chapter.Chapter;
import us.usserver.score.dto.PostScore;

@Service
public interface ScoreService {
    void postScore(Long chatperId, Long authorID, PostScore postScore);

    Double getChapterScore(Chapter chapter);
}

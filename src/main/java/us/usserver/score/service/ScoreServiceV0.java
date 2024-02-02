package us.usserver.score.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.author.Author;
import us.usserver.chapter.Chapter;
import us.usserver.global.EntityService;
import us.usserver.global.ExceptionMessage;
import us.usserver.global.exception.ScoreOutOfRangeException;
import us.usserver.score.Score;
import us.usserver.score.ScoreRepository;
import us.usserver.score.ScoreService;
import us.usserver.score.dto.PostScore;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ScoreServiceV0 implements ScoreService {
    private final EntityService entityService;
    private final ScoreRepository scoreRepository;

    @Override
    public void postScore(Long chapterId, Long authorID, PostScore postScore) {
        Author author = entityService.getAuthor(authorID);
        Chapter chapter = entityService.getChapter(chapterId);

        if (postScore.getScore() > 10 || postScore.getScore() < 1) {
            throw new ScoreOutOfRangeException(ExceptionMessage.Score_OUT_OF_RANGE);
        }
        Optional<Score> scoreByAuthorAndChapter = scoreRepository.findScoreByAuthorAndChapter(author, chapter);
        if (scoreByAuthorAndChapter.isEmpty()) {
            Score score = Score.builder()
                    .score(postScore.getScore())
                    .author(author)
                    .chapter(chapter)
                    .build();
            scoreRepository.save(score);
        }
    }

    @Override
    public Double getChapterScore(Chapter chapter) {
        return scoreRepository.findAverageScoreByChapter(chapter);
    }
}

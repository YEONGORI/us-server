package us.usserver.domain.chapter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.domain.author.entity.Author;
import us.usserver.domain.chapter.entity.Chapter;
import us.usserver.domain.chapter.entity.Score;
import us.usserver.domain.chapter.repository.ScoreRepository;
import us.usserver.global.EntityFacade;
import us.usserver.global.ExceptionMessage;
import us.usserver.global.exception.ScoreOutOfRangeException;
import us.usserver.domain.chapter.dto.PostScore;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {
    private final EntityFacade entityFacade;
    private final ScoreRepository scoreRepository;

    @Override
    public void postScore(Long chapterId, Long authorID, PostScore postScore) {
        Author author = entityFacade.getAuthor(authorID);
        Chapter chapter = entityFacade.getChapter(chapterId);

        if (postScore.getScore() > 10 || postScore.getScore() < 1) {
            throw new ScoreOutOfRangeException(ExceptionMessage.SCORE_OUT_OF_RANGE);
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

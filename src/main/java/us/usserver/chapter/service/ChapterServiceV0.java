package us.usserver.chapter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.author.Author;
import us.usserver.chapter.Chapter;
import us.usserver.chapter.ChapterRepository;
import us.usserver.chapter.ChapterService;
import us.usserver.chapter.chapterEnum.ChapterStatus;
import us.usserver.chapter.dto.ChapterDetailInfo;
import us.usserver.chapter.dto.ChapterInfo;
import us.usserver.comment.CommentRepository;
import us.usserver.global.EntityService;
import us.usserver.global.ExceptionMessage;
import us.usserver.global.exception.MainAuthorIsNotMatchedException;
import us.usserver.novel.Novel;
import us.usserver.paragraph.ParagraphService;
import us.usserver.paragraph.dto.ParagraphsOfChapter;
import us.usserver.score.ScoreRepository;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChapterServiceV0 implements ChapterService {
    private final EntityService entityService;
    private final ParagraphService paragraphService;

    private final ChapterRepository chapterRepository;
    private final ScoreRepository scoreRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<ChapterInfo> getChaptersOfNovel(Novel novel) {
        List<Chapter> chapters = chapterRepository.findAllByNovelOrderByPart(novel);

        return chapters.stream()
                .map(ChapterInfo::fromChapter)
                .toList();
    }

    @Override
    public ChapterDetailInfo getChapterDetailInfo(Long novelId, Long authorId, Long chapterId) {
        Author author = entityService.getAuthor(authorId);
        Chapter chapter = entityService.getChapter(chapterId);
        Novel novel = entityService.getNovel(novelId);
        ParagraphsOfChapter paragraphs = paragraphService.getParagraphs(authorId, chapterId);
        List<Chapter> chapters = chapterRepository.findAllByNovelOrderByPart(novel);
        Integer commentCnt = commentRepository.countAllByChapter(chapter);
        Double score = scoreRepository.findAverageScoreByChapter(chapter);

        Integer part = chapter.getPart();
        Integer prevPart = part - 1, nextPart = part + 1;
        if (part == chapters.size()) {
            nextPart = null;
        }

        author.getViewedNovels().add(novel);
        return ChapterDetailInfo.builder()
                .part(part)
                .title(chapter.getTitle())
                .status(chapter.getStatus())
                .score(score)
                .myParagraph(paragraphs.getMyParagraph())
                .bestParagraph(paragraphs.getBestParagraph())
                .selectedParagraphs(paragraphs.getSelectedParagraphs())
                .prevPart(prevPart)
                .nextPart(nextPart)
                .commentCnt(commentCnt)
                .build();
    }

    private static String createChapterUrl(Long novelId, Long chapterId) {
        return "http://localhost:8000/chapter/" + novelId + "/" + chapterId;
    }

    @Override
    public void createChapter(Long novelId, Long authorId) {
        Novel novel = entityService.getNovel(novelId);
        Author author = entityService.getAuthor(authorId);
        int curChapterPart = chapterRepository.countChapterByNovel(novel) + 1;

        if (!novel.getMainAuthor().getId().equals(author.getId())) {
            throw new MainAuthorIsNotMatchedException(ExceptionMessage.Main_Author_NOT_MATCHED);
        }

        Chapter chapter = Chapter.builder()
                .part(curChapterPart)
                .title(novel.getTitle() + " " + curChapterPart + "화")
                .status(ChapterStatus.IN_PROGRESS)
                .novel(novel)
                .build();

        novel.getChapters().add(chapter);
        chapterRepository.save(chapter);
    }
}

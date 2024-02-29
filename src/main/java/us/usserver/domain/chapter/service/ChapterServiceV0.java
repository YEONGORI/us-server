package us.usserver.domain.chapter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.domain.member.entity.Author;
import us.usserver.domain.chapter.entity.Chapter;
import us.usserver.domain.chapter.repository.ChapterRepository;
import us.usserver.domain.chapter.constant.ChapterStatus;
import us.usserver.domain.chapter.dto.ChapterDetailInfo;
import us.usserver.domain.chapter.dto.ChapterInfo;
import us.usserver.domain.comment.Comment;
import us.usserver.domain.comment.repository.CommentRepository;
import us.usserver.domain.comment.dto.CommentInfo;
import us.usserver.global.EntityService;
import us.usserver.global.ExceptionMessage;
import us.usserver.global.exception.MainAuthorIsNotMatchedException;
import us.usserver.domain.novel.Novel;
import us.usserver.domain.paragraph.service.ParagraphService;
import us.usserver.domain.paragraph.dto.ParagraphsOfChapter;
import us.usserver.domain.chapter.repository.ScoreRepository;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChapterServiceV0 implements ChapterService {
    private final EntityService entityService;
    private final ParagraphService paragraphService;

    private final ChapterRepository chapterRepository;
    private final CommentRepository commentRepository;
    private final ScoreRepository scoreRepository;

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
        Integer commentCnt = commentRepository.countByChapter(chapter);
        List<Comment> comments = commentRepository.getTop3CommentOfChapter(chapter);
        List<CommentInfo> commentInfos = comments.stream().map(comment -> CommentInfo.fromComment(comment, chapter.getTitle(), comment.getCommentLikes().size())).toList();
        Double score = scoreRepository.findAverageScoreByChapter(chapter);

        Integer part = chapter.getPart();
        Integer prevPart = part - 1, nextPart = part + 1;
        if (part == 1) {
            prevPart = null;
        }
        if (part == chapters.size()) {
            nextPart = null;
        }
        if (score == null) {
            score = 0.0;
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
                .fontSize(author.getFontSize())
                .paragraphSpace(author.getParagraphSpace())
                .bestComments(commentInfos)
                .build();
    }

    @Override
    public void createChapter(Long novelId, Long authorId) {
        Novel novel = entityService.getNovel(novelId);
        Author author = entityService.getAuthor(authorId);
        Integer curChapterPart = chapterRepository.countChapterByNovel(novel) + 1;

        if (!novel.getMainAuthor().getId().equals(author.getId())) {
            throw new MainAuthorIsNotMatchedException(ExceptionMessage.MAIN_AUTHOR_NOT_MATCHED);
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

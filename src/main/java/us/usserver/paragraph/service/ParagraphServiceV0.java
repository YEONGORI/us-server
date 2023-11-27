package us.usserver.paragraph.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.usserver.author.Author;
import us.usserver.author.AuthorRepository;
import us.usserver.chapter.Chapter;
import us.usserver.chapter.ChapterRepository;
import us.usserver.chapter.chapterEnum.ChapterStatus;
import us.usserver.global.ExceptionMessage;
import us.usserver.global.exception.*;
import us.usserver.like.paragraph.ParagraphLikeRepository;
import us.usserver.novel.Novel;
import us.usserver.novel.NovelRepository;
import us.usserver.paragraph.Paragraph;
import us.usserver.paragraph.ParagraphRepository;
import us.usserver.paragraph.ParagraphService;
import us.usserver.paragraph.dto.*;
import us.usserver.paragraph.paragraphEnum.ParagraphStatus;
import us.usserver.stake.StakeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParagraphServiceV0 implements ParagraphService {
    private final NovelRepository novelRepository;
    private final ChapterRepository chapterRepository;
    private final ParagraphRepository paragraphRepository;
    private final ParagraphLikeRepository paragraphLikeRepository;
    private final AuthorRepository authorRepository;

    private final StakeService stakeService;

    @Override
    public GetParagraphsRes getParagraphs(Long authorId, Long chapterId) {
        Author author = getAuthor(authorId);
        Chapter chapter = getChapter(chapterId);

        List<Paragraph> paragraphs = paragraphRepository.findAllByChapter(chapter);
        if (chapter.getStatus() == ChapterStatus.COMPLETED) {
            return getCompletedChParagraph(paragraphs);
        } else {
            return getInProgressChParagraph(paragraphs, author);
        }
    }

    private GetParagraphsRes getCompletedChParagraph(List<Paragraph> paragraphs) {
        List<ParagraphSelected> selectedParagraphs = paragraphs.stream()
                .filter(paragraph -> paragraph.getParagraphStatus() == ParagraphStatus.SELECTED)
                .map(ParagraphSelected::fromParagraph)
                .toList();

        return GetParagraphsRes.builder()
                .selectedParagraphs(selectedParagraphs)
                .myParagraph(null)
                .bestParagraph(null)
                .build();
    }

    private GetParagraphsRes getInProgressChParagraph(List<Paragraph> paragraphs, Author author) {
        List<ParagraphSelected> selectedParagraphs = new ArrayList<>();
        ParagraphInVoting myParagraph = null, bestParagraph = null;

        int maxLikeCount = 0, likeCount;
        for (Paragraph paragraph : paragraphs) {
            ParagraphStatus status = paragraph.getParagraphStatus();
            likeCount = paragraphLikeRepository.countAllByParagraph(paragraph);

            if (status == ParagraphStatus.IN_VOTING && // 내가 쓴 한줄
                            paragraph.getAuthor().getId().equals(author.getId())) {
                myParagraph = ParagraphInVoting.fromParagraph(paragraph, likeCount);
            } else if (status == ParagraphStatus.IN_VOTING && // 베스트 한줄
                            likeCount > maxLikeCount) {
                bestParagraph = ParagraphInVoting.fromParagraph(paragraph, likeCount);
            } else if (status == ParagraphStatus.SELECTED) { // 이미 선정된 한줄
                selectedParagraphs.add(ParagraphSelected.fromParagraph(paragraph));
            }
        }

        return GetParagraphsRes.builder()
                .selectedParagraphs(selectedParagraphs)
                .myParagraph(myParagraph)
                .bestParagraph(bestParagraph)
                .build();
    }

    @Override
    public List<ParagraphInVoting> getInVotingParagraphs(Long chapterId) {
        return null;
    }

    @Override
    public ParagraphInVoting postParagraph(Long authorId, Long chapterId, PostParagraphReq req) {
        Author author = getAuthor(authorId);
        Chapter chapter = getChapter(chapterId);
        int nextChapterCnt = paragraphRepository.countParagraphsByChapter(chapter) + 1;

        Paragraph paragraph = paragraphRepository.save(
                Paragraph.builder()
                        .content(req.getContent())
                        .order(nextChapterCnt)
                        .paragraphStatus(ParagraphStatus.UNSELECTED)
                        .chapter(chapter)
                        .author(author)
                        .build()
        );

        chapter.getParagraphs().add(paragraph);
        return ParagraphInVoting.builder()
                .content(paragraph.getContent())
                .order(paragraph.getOrder())
                .likeCnt(0)
                .status(paragraph.getParagraphStatus())
                .authorId(0L) // TODO: 이 부분은 아예 제거 할지 말지 고민중
                .authorName(paragraph.getAuthor().getNickname())
                .createdAt(paragraph.getCreatedAt())
                .updatedAt(paragraph.getUpdatedAt())
                .build();
    }

    @Override
    public void selectParagraph(Long authorId, Long novelId, Long chapterId, Long paragraphId) {
        Novel novel = getNovel(novelId);
        Chapter chapter = getChapter(chapterId);
        Paragraph paragraph = getParagraph(paragraphId);
        Author author = getAuthor(authorId);
        

        if (!novel.getAuthor().getId().equals(authorId)) {
            throw new MainAuthorIsNotMatchedException(ExceptionMessage.Main_Author_NOT_MATCHED);
        }
        if (!novel.getChapters().contains(chapter)) {
            throw new ChapterNotFoundException(ExceptionMessage.Chapter_NOT_FOUND);
        }
        if (!chapter.getParagraphs().contains(paragraph)) {
            throw new ParagraphNotFoundException(ExceptionMessage.Paragraph_NOT_FOUND);
        }

        paragraph.setParagraphStatus(ParagraphStatus.SELECTED);
        stakeService.setStakeInfoOfNovel(novel, author);

        // 선택 되지 않은 paragraph 들의 status 변경
        List<Paragraph> paragraphs = paragraphRepository.findAllByChapter(chapter);
        for (Paragraph para : paragraphs) {
            if (para.getParagraphStatus() == ParagraphStatus.IN_VOTING) {
                para.setParagraphStatus(ParagraphStatus.UNSELECTED);
            }
        }
    }

    private Novel getNovel(Long novelId) {
        Optional<Novel> novelById = novelRepository.getNovelById(novelId);
        if (novelById.isEmpty()) {
            throw new NovelNotFoundException(ExceptionMessage.Novel_NOT_FOUND);
        }
        return novelById.get();
    }

    private Paragraph getParagraph(Long paragraphId) {
        Optional<Paragraph> paragraphById = paragraphRepository.getParagraphById(paragraphId);
        if (paragraphById.isEmpty()) {
            throw new ParagraphNotFoundException(ExceptionMessage.Paragraph_NOT_FOUND);
        }
        return paragraphById.get();
    }

    private Author getAuthor(Long authorId) {
        Optional<Author> authorById = authorRepository.getAuthorById(authorId);
        if (authorById.isEmpty()) {
            throw new AuthorNotFoundException(ExceptionMessage.Author_NOT_FOUND);
        }
        return authorById.get();
    }

    private Chapter getChapter(Long chapterId) {
        Optional<Chapter> chapterById = chapterRepository.getChapterById(chapterId);
        if (chapterById.isEmpty()) {
            throw new ChapterNotFoundException(ExceptionMessage.Chapter_NOT_FOUND);
        }
        return chapterById.get();
    }
}

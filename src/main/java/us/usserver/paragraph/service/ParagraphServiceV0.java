package us.usserver.paragraph.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.usserver.author.Author;
import us.usserver.author.AuthorRepository;
import us.usserver.chapter.Chapter;
import us.usserver.chapter.ChapterRepository;
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
    public ParagraphsInfo getParagraphs(Long authorId, Long chapterId) {
        Author author = getAuthor(authorId);
        Chapter chapter = getChapter(chapterId);

        List<Paragraph> paragraphs = paragraphRepository.findAllByChapter(chapter);
        int nextChapterCnt = paragraphs.size();

        List<ParagraphSelected> selectedParagraph = new ArrayList<>();
        ParagraphInfo myParagraph = null, bestParagraph = null;

        int maxLikeCount = 0, likeCount;
        for (Paragraph paragraph : paragraphs) {
            ParagraphStatus status = paragraph.getParagraphStatus();
            likeCount = paragraphLikeRepository.countAllByParagraph(paragraph);

            if (status == ParagraphStatus.REGISTERED && // 내가 쓴 한줄
                            paragraph.getAuthor().getId().equals(author.getId())) {
                myParagraph = ParagraphInfo.fromParagraph(paragraph, likeCount);
            } else if (status == ParagraphStatus.REGISTERED && // 베스트 한줄
                            likeCount > maxLikeCount) {
                bestParagraph = ParagraphInfo.fromParagraph(paragraph, likeCount);
            } else if (status == ParagraphStatus.SELECTED) { // 이미 선정된 한줄
                selectedParagraph.add(ParagraphSelected.fromParagraph(paragraph));
            }

        }

        List<ParagraphSelected> selectedList = paragraphs.stream()
                .filter(paragraph -> paragraph.getParagraphStatus() == ParagraphStatus.SELECTED)
                .map(paragraph -> ParagraphSelected.builder()
                        .content(paragraph.getContent())
                        .order(nextChapterCnt)
                        .build()
                ).toList();

        List<ParagraphUnSelected> unSelectedList = paragraphs.stream()
                .filter(paragraph -> paragraph.getParagraphStatus() == ParagraphStatus.UNSELECTED)
                .map(paragraph -> ParagraphUnSelected.builder()
                        .content(paragraph.getContent())
                        .order(nextChapterCnt)
                        .likeCnt(paragraphLikeRepository.countAllByParagraph(paragraph))
                        .authorName(paragraph.getAuthor().getNickname())
                        .paragraphStatus(ParagraphStatus.UNSELECTED)
                        .build()
                ).toList();

        return ParagraphsInfo.builder()
                .selectedParagraphs(selectedParagraph)
                .myParagraph(myParagraph)
                .bestParagraph(bestParagraph)
                .build();
    }

    @Override
    public List<ParagraphInfo> getRegisteredParagraphs(Long authorId, Long chapterId) {
        return null;
    }


    @Override
    public ParagraphUnSelected postParagraph(Long authorId, Long chapterId, PostParagraphReq req) {
        Author author = getAuthor(authorId);
        Chapter chapter = getChapter(chapterId);
        Integer curChapterCnt = paragraphRepository.countParagraphsByChapter(chapter);

        Paragraph paragraph = paragraphRepository.save(
                Paragraph.builder()
                        .content(req.getContent())
                        .order(curChapterCnt + 1)
                        .paragraphStatus(ParagraphStatus.UNSELECTED)
                        .chapter(chapter)
                        .author(author)
                        .build()
        );

        chapter.getParagraphs().add(paragraph);

        return ParagraphUnSelected.builder()
                .content(paragraph.getContent())
                .likeCnt(0)
                .order(paragraph.getOrder())
                .authorName(paragraph.getAuthor().getNickname())
                .paragraphStatus(paragraph.getParagraphStatus())
                .build();
    }

    @Override
    public void selectParagraph(Long authorId, Long novelId, Long chapterId, Long paragraphId) {
        Novel novel = getNovel(novelId);
        Chapter chapter = getChapter(chapterId);
        Paragraph paragraph = getParagraph(paragraphId);
        Author author = getAuthor(authorId);
        
        // TODO: 선택되지 않은 paragraph들의 status 변경

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

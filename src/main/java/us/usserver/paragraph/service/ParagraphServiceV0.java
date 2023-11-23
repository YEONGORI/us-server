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
import us.usserver.paragraph.dto.ParagraphInfo;
import us.usserver.paragraph.dto.ParagraphSelected;
import us.usserver.paragraph.dto.ParagraphUnSelected;
import us.usserver.paragraph.dto.PostParagraphReq;
import us.usserver.paragraph.paragraphEnum.ParagraphStatus;

import java.util.List;
import java.util.Objects;
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

    @Override
    public ParagraphInfo getParagraphs(Long chapterId) {
        Chapter chapter = getChapter(chapterId);

        List<Paragraph> paragraphs = paragraphRepository.findAllByChapter(chapter);
        int nextChapterCnt = paragraphRepository.countParagraphsByChapter(chapter) + 1;

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

        return ParagraphInfo.builder()
                .selectedList(selectedList)
                .unSelectedList(unSelectedList)
                .build();
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

        return ParagraphUnSelected.builder()
                .content(paragraph.getContent())
                .likeCnt(paragraphLikeRepository.countAllByParagraph(paragraph))
                .order(paragraph.getOrder())
                .authorName(paragraph.getAuthor().getNickname())
                .paragraphStatus(paragraph.getParagraphStatus())
                .build();
    }

    @Override
    public void selectParagraph(Long authorId, Long novelId, Long chapterId, Long paragraphId) {
        Novel novel = getNovel(novelId);
        if (!novel.getAuthor().getId().equals(authorId)) {
            throw new MainAuthorIsNotMatchedException(ExceptionMessage.Main_Author_NOT_MATCHED);
        }

        Author author = getAuthor(authorId);
        Chapter chapter = getChapter(chapterId);
        Paragraph paragraph = getParagraph(paragraphId);
        paragraph.setParagraphStatus(ParagraphStatus.SELECTED);


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

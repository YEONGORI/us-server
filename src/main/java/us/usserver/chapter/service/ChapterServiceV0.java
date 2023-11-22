package us.usserver.chapter.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.usserver.author.Author;
import us.usserver.author.AuthorRepository;
import us.usserver.chapter.dto.CreateChapterReq;
import us.usserver.global.ExceptionMessage;
import us.usserver.chapter.Chapter;
import us.usserver.chapter.ChapterRepository;
import us.usserver.chapter.ChapterService;
import us.usserver.chapter.dto.ChapterDetailRes;
import us.usserver.chapter.dto.ChaptersOfNovel;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.global.exception.ChapterNotFoundException;
import us.usserver.global.exception.MainAuthorIsNotMatchedException;
import us.usserver.novel.Novel;
import us.usserver.novel.NovelRepository;
import us.usserver.global.exception.NovelNotFoundException;
import us.usserver.paragraph.dto.ParagraphInfo;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChapterServiceV0 implements ChapterService {
    private final NovelRepository novelRepository;
    private final ChapterRepository chapterRepository;
    private final AuthorRepository authorRepository;

    @Override
    public List<ChaptersOfNovel> getChaptersOfNovel(Long novelId) {
        Novel novel = getNovel(novelId);

        return chapterRepository.findAllByNovel(novel)
                .stream().map(chapter -> ChaptersOfNovel.builder()
                        .id(chapter.getId())
                        .title(chapter.getTitle())
                        .part(chapter.getPart())
                        .createdAt(chapter.getCreatedAt())
                        .updatedAt(chapter.getUpdatedAt())
                        .build())
                .toList();
    }


    @Override
    public ChapterDetailRes getChapterDetail(Long novelId, Long chapterId) {
        getNovel(novelId);
        Chapter chapter = getChapter(chapterId);

        List<ParagraphInfo> paragraphInfos = chapter.getParagraphs()
                .stream().map(paragraph -> ParagraphInfo.builder()
                        .id(paragraph.getId())
                        .number(paragraph.getNumber())
                        .content(paragraph.getContent())
                        .paragraphStatus(paragraph.getParagraphStatus())
                        .build())
                .toList();

        return ChapterDetailRes.builder()
                .id(chapter.getId())
                .title(chapter.getTitle())
                .part(chapter.getPart())
                .paragraphInfos(paragraphInfos)
                .build();
    }

    // TODO: 챕터(회차) 생성 시 제목은 받아야 할 듯.
    @Override
    public void createChapter(Long novelId, Long authorId) {
        Novel novel = getNovel(novelId);
        Author author = getAuthor(authorId);

        if (!novel.getAuthor().getId().equals(author.getId())) {
            throw new MainAuthorIsNotMatchedException(ExceptionMessage.Main_Author_NOT_MATCHED);
        }

        int curChapterPart = chapterRepository.countChapterByNovel(novel) + 1;

        Chapter chapter = Chapter.builder()
                .part(curChapterPart)
                .title(novel.getTitle() + " " + curChapterPart + "화")
                .novel(novel)
                .build();

        chapterRepository.save(chapter);
    }

    private Author getAuthor(Long authorId) {
        Optional<Author> authorById = authorRepository.getAuthorById(authorId);
        if (authorById.isEmpty()) {
            throw new AuthorNotFoundException(ExceptionMessage.Author_NOT_FOUND);
        }
        return authorById.get();
    }

    private Novel getNovel(Long novelId) {
        Optional<Novel> novelById = novelRepository.getNovelById(novelId);
        if (novelById.isEmpty()) {
            throw new NovelNotFoundException(ExceptionMessage.Novel_NOT_FOUND);
        }
        return novelById.get();
    }

    private Chapter getChapter(Long chapterId) {
        Optional<Chapter> chapterById = chapterRepository.getChapterById(chapterId);
        if (chapterById.isEmpty()) {
            throw new ChapterNotFoundException(ExceptionMessage.Chapter_NOT_FOUND);
        }
        return chapterById.get();
    }
}

package us.usserver.chapter.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.usserver.author.Author;
import us.usserver.author.AuthorRepository;
import us.usserver.chapter.Chapter;
import us.usserver.chapter.ChapterRepository;
import us.usserver.chapter.ChapterService;
import us.usserver.chapter.chapterEnum.ChapterStatus;
import us.usserver.chapter.dto.ChaptersOfNovel;
import us.usserver.global.EntityService;
import us.usserver.global.ExceptionMessage;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.global.exception.MainAuthorIsNotMatchedException;
import us.usserver.global.exception.NovelNotFoundException;
import us.usserver.novel.Novel;
import us.usserver.novel.NovelRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChapterServiceV0 implements ChapterService {
    private final EntityService entityService;
    private final NovelRepository novelRepository;
    private final ChapterRepository chapterRepository;
    private final AuthorRepository authorRepository;

    @Override
    public List<ChaptersOfNovel> getChaptersOfNovel(Long novelId) {
        Novel novel = entityService.getNovel(novelId);

        return chapterRepository.findAllByNovel(novel)
                .stream().map(chapter -> ChaptersOfNovel.builder()
                        .id(chapter.getId())
                        .title(chapter.getTitle())
                        .part(chapter.getPart())
                        .status(chapter.getStatus())
                        .createdAt(chapter.getCreatedAt())
                        .updatedAt(chapter.getUpdatedAt())
                        .build())
                .toList();
    }

    // TODO: 챕터(회차) 생성 시 제목은 받아야 할 듯.
    @Override
    public void createChapter(Long novelId, Long authorId) {
        Novel novel = entityService.getNovel(novelId);
        Author author = entityService.getAuthor(authorId);

        if (!novel.getAuthor().getId().equals(author.getId())) {
            throw new MainAuthorIsNotMatchedException(ExceptionMessage.Main_Author_NOT_MATCHED);
        }

        int curChapterPart = chapterRepository.countChapterByNovel(novel) + 1;

        Chapter chapter = Chapter.builder()
                .part(curChapterPart)
                .title(novel.getTitle() + " " + curChapterPart + "화")
                .status(ChapterStatus.IN_PROGRESS)
                .novel(novel)
                .build();

        chapterRepository.save(chapter);
    }
}

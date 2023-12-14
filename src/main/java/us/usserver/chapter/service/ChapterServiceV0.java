package us.usserver.chapter.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.usserver.author.Author;
import us.usserver.chapter.Chapter;
import us.usserver.chapter.ChapterRepository;
import us.usserver.chapter.ChapterService;
import us.usserver.chapter.dto.ChapterDetailInfo;
import us.usserver.chapter.chapterEnum.ChapterStatus;
import us.usserver.chapter.dto.ChapterInfo;
import us.usserver.global.EntityService;
import us.usserver.global.ExceptionMessage;
import us.usserver.global.exception.MainAuthorIsNotMatchedException;
import us.usserver.novel.Novel;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChapterServiceV0 implements ChapterService {
    private final EntityService entityService;
    private final ChapterRepository chapterRepository;

    @Override
    public List<ChapterInfo> getChaptersOfNovel(Novel novel) {
        List<Chapter> chapters = chapterRepository.findAllByNovel(novel);

        return chapters.stream()
                .map(ChapterInfo::fromChapter)
                .toList();
    }

    @Override
    public ChapterDetailInfo getChapterDetailInfo(Long chapterId) {
        return null;
    }

    // TODO: 챕터(회차) 생성 시 제목은 받아야 할 듯.
    @Override
    public void createChapter(Long novelId, Long authorId) {
        Novel novel = entityService.getNovel(novelId);
        Author author = entityService.getAuthor(authorId);

        if (!novel.getMainAuthor().getId().equals(author.getId())) {
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

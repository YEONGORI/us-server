package us.usserver.paragraph.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.usserver.author.Author;
import us.usserver.authority.Authority;
import us.usserver.authority.AuthorityRepository;
import us.usserver.chapter.Chapter;
import us.usserver.chapter.ChapterRepository;
import us.usserver.chapter.chapterEnum.ChapterStatus;
import us.usserver.global.EntityService;
import us.usserver.global.ExceptionMessage;
import us.usserver.global.exception.ChapterNotFoundException;
import us.usserver.global.exception.MainAuthorIsNotMatchedException;
import us.usserver.global.exception.ParagraphNotFoundException;
import us.usserver.like.paragraph.ParagraphLikeRepository;
import us.usserver.novel.Novel;
import us.usserver.paragraph.Paragraph;
import us.usserver.paragraph.ParagraphRepository;
import us.usserver.paragraph.ParagraphService;
import us.usserver.paragraph.dto.GetParagraphsRes;
import us.usserver.paragraph.dto.ParagraphInVoting;
import us.usserver.paragraph.dto.ParagraphSelected;
import us.usserver.paragraph.dto.PostParagraphReq;
import us.usserver.paragraph.paragraphEnum.ParagraphStatus;
import us.usserver.stake.StakeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ParagraphServiceV0 implements ParagraphService {
    private final EntityService entityService;
    private final ParagraphRepository paragraphRepository;
    private final ParagraphLikeRepository paragraphLikeRepository;
    private final AuthorityRepository authorityRepository;

    private final StakeService stakeService;

    @Override
    public GetParagraphsRes getParagraphs(Long authorId, Long chapterId) {
        Author author = entityService.getAuthor(authorId);
        Chapter chapter = entityService.getChapter(chapterId);

        List<Paragraph> paragraphs = paragraphRepository.findAllByChapter(chapter);
        if (paragraphs.isEmpty()) {
            return getInitialChParagraph();
        } else if (chapter.getStatus() == ChapterStatus.COMPLETED) {
            return getCompletedChParagraph(paragraphs);
        } else {
            return getInProgressChParagraph(paragraphs, author);
        }
    }

    private GetParagraphsRes getInitialChParagraph() {
        return GetParagraphsRes.builder()
                .selectedParagraphs(null)
                .myParagraph(null)
                .bestParagraph(null)
                .build();
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

        int maxLikeCount = 0, likeCount; // TODO: 이부분 수정 필요git p
        for (Paragraph paragraph : paragraphs) {
            ParagraphStatus status = paragraph.getParagraphStatus();
            likeCount = paragraphLikeRepository.countAllByParagraph(paragraph);

            if (status == ParagraphStatus.IN_VOTING && // 내가 쓴 한줄
                            paragraph.getAuthor().getId().equals(author.getId())) {
                myParagraph = ParagraphInVoting.fromParagraph(paragraph, likeCount);
            }
            if (status == ParagraphStatus.IN_VOTING && // 베스트 한줄
                            likeCount >= maxLikeCount) {
                bestParagraph = ParagraphInVoting.fromParagraph(paragraph, likeCount);
                maxLikeCount = likeCount;
            }
            if (status == ParagraphStatus.SELECTED) { // 이미 선정된 한줄
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
        Chapter chapter = entityService.getChapter(chapterId);
        List<Paragraph> paragraphs = paragraphRepository.findAllByChapter(chapter);

        return paragraphs.stream().filter(paragraph -> paragraph.getParagraphStatus().equals(ParagraphStatus.IN_VOTING))
                .map(paragraph -> ParagraphInVoting.fromParagraph(paragraph, paragraphLikeRepository.countAllByParagraph(paragraph)))
                .toList();
    }

    @Override
    public ParagraphInVoting postParagraph(Long authorId, Long chapterId, PostParagraphReq req) {
        Author author = entityService.getAuthor(authorId);
        Chapter chapter = entityService.getChapter(chapterId);
        int nextChapterCnt = paragraphRepository.countParagraphsByChapter(chapter) + 1;

        Paragraph paragraph = paragraphRepository.save(
                Paragraph.builder()
                        .content(req.getContent())
                        .sequence(nextChapterCnt)
                        .paragraphStatus(ParagraphStatus.UNSELECTED)
                        .chapter(chapter)
                        .author(author)
                        .build()
        );

        chapter.getParagraphs().add(paragraph);
        return ParagraphInVoting.builder()
                .content(paragraph.getContent())
                .sequence(paragraph.getSequence())
                .likeCnt(0)
                .status(paragraph.getParagraphStatus())
                .authorId(0L) // TODO: 이 부분은 보안 상 아예 제거 할지 말지 고민중
                .authorName(paragraph.getAuthor().getNickname())
                .createdAt(paragraph.getCreatedAt())
                .updatedAt(paragraph.getUpdatedAt())
                .build();
    }

    @Override
    public void selectParagraph(Long authorId, Long novelId, Long chapterId, Long paragraphId) {
        Novel novel = entityService.getNovel(novelId);
        Chapter chapter = entityService.getChapter(chapterId);
        Paragraph paragraph = entityService.getParagraph(paragraphId);
        Author author = entityService.getAuthor(authorId);

        if (!novel.getMainAuthor().getId().equals(authorId)) {
            throw new MainAuthorIsNotMatchedException(ExceptionMessage.Main_Author_NOT_MATCHED);
        }
//        if (novel.getChapters().stream().noneMatch(ch -> ch.getId().equals(chapterId))) {
//            throw new ChapterNotFoundException(ExceptionMessage.Chapter_NOT_FOUND);
//        }
        if (!novel.getChapters().contains(chapter)) {
            throw new ChapterNotFoundException(ExceptionMessage.Chapter_NOT_FOUND);
        }
        if (!chapter.getParagraphs().contains(paragraph)) {
            throw new ParagraphNotFoundException(ExceptionMessage.Paragraph_NOT_FOUND);
        }

        addAuthority(author, novel);

        paragraph.setParagraphStatus(ParagraphStatus.SELECTED);
        stakeService.setStakeInfoOfNovel(novel);

        // 선택 되지 않은 paragraph 들의 status 변경
        List<Paragraph> paragraphs = paragraphRepository.findAllByChapter(chapter);
        for (Paragraph para : paragraphs) {
            if (para.getParagraphStatus() == ParagraphStatus.IN_VOTING) {
                para.setParagraphStatus(ParagraphStatus.UNSELECTED);
            }
        }
    }

    private void addAuthority(Author author, Novel novel) {
        List<Authority> authorities = authorityRepository.findAllByAuthor(author);
        boolean isAuthorized = authorities.stream().anyMatch(authority -> Objects.equals(authority.getNovel().getId(), novel.getId()));

        if (!isAuthorized) {
            Authority authority = new Authority();
            author.addAuthorNovel(authority);
            authority.takeNovel(novel);
            authorityRepository.save(authority);
        }
    }
}

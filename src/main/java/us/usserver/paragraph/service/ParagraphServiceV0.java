package us.usserver.paragraph.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.author.Author;
import us.usserver.authority.Authority;
import us.usserver.authority.AuthorityRepository;
import us.usserver.chapter.Chapter;
import us.usserver.chapter.chapterEnum.ChapterStatus;
import us.usserver.global.EntityService;
import us.usserver.global.ExceptionMessage;
import us.usserver.global.exception.ChapterNotFoundException;
import us.usserver.global.exception.ParagraphLengthOutOfRangeException;
import us.usserver.global.exception.MainAuthorIsNotMatchedException;
import us.usserver.global.exception.ParagraphNotFoundException;
import us.usserver.like.paragraph.ParagraphLike;
import us.usserver.like.paragraph.ParagraphLikeRepository;
import us.usserver.paragraph.dto.*;
import us.usserver.vote.VoteRepository;
import us.usserver.novel.Novel;
import us.usserver.paragraph.Paragraph;
import us.usserver.paragraph.ParagraphRepository;
import us.usserver.paragraph.ParagraphService;
import us.usserver.paragraph.paragraphEnum.ParagraphStatus;
import us.usserver.stake.StakeService;

import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ParagraphServiceV0 implements ParagraphService {
    private final EntityService entityService;
    private final StakeService stakeService;

    private final ParagraphRepository paragraphRepository;
    private final VoteRepository voteRepository;
    private final AuthorityRepository authorityRepository;
    private final ParagraphLikeRepository paragraphLikeRepository;


    @Override
    public ParagraphsOfChapter getParagraphs(Long authorId, Long chapterId) {
        Author author = entityService.getAuthor(authorId);
        Chapter chapter = entityService.getChapter(chapterId);

        List<Paragraph> paragraphs = paragraphRepository.findAllByChapter(chapter);
        if (paragraphs.isEmpty()) {
            return getInitialChParagraph();
        } else if (chapter.getStatus() == ChapterStatus.COMPLETED) {
            return getCompletedChParagraph(paragraphs, author);
        } else {
            return getInProgressChParagraph(paragraphs, author);
        }
    }

    @Override
    public GetParagraphResponse getInVotingParagraphs(Long chapterId) {
        Chapter chapter = entityService.getChapter(chapterId);
        List<Paragraph> paragraphs = paragraphRepository.findAllByChapter(chapter);

        List<ParagraphInVoting> paragraphInVotings = paragraphs.stream().filter(paragraph -> paragraph.getParagraphStatus().equals(ParagraphStatus.IN_VOTING))
                .map(paragraph -> ParagraphInVoting.fromParagraph(paragraph, voteRepository.countAllByParagraph(paragraph)))
                .toList();

        return GetParagraphResponse.builder().paragraphInVotings(paragraphInVotings).build();
    }

    @Override
    public ParagraphInVoting postParagraph(Long authorId, Long chapterId, PostParagraphReq req) {
        Author author = entityService.getAuthor(authorId);
        Chapter chapter = entityService.getChapter(chapterId);
        int nextChapterCnt = paragraphRepository.countParagraphsByChapter(chapter) + 1;

        if (req.getContent().length() > 300 || req.getContent().length() < 50) {
            throw new ParagraphLengthOutOfRangeException(ExceptionMessage.Paragraph_Length_OUT_OF_RANGE);
        }

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
                .voteCnt(0)
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
        if (!novel.getChapters().contains(chapter)) {
            throw new ChapterNotFoundException(ExceptionMessage.Chapter_NOT_FOUND);
        }
        if (!chapter.getParagraphs().contains(paragraph)) {
            throw new ParagraphNotFoundException(ExceptionMessage.Paragraph_NOT_FOUND);
        }

        addAuthority(author, novel);

        paragraph.setParagraphStatusForTest(ParagraphStatus.SELECTED);
        stakeService.setStakeInfoOfNovel(novel);

        // 선택 되지 않은 paragraph 들의 status 변경
        List<Paragraph> paragraphs = paragraphRepository.findAllByChapter(chapter);
        for (Paragraph p : paragraphs) {
            if (p.getParagraphStatus() == ParagraphStatus.IN_VOTING) {
                p.setParagraphStatusForTest(ParagraphStatus.UNSELECTED);
            }
        }
    }

    @Override
    public void reportParagraph(Long authorId, Long paragraphId) {
        Author author = entityService.getAuthor(authorId);
        Paragraph paragraph = entityService.getParagraph(paragraphId);


    }

    private ParagraphsOfChapter getInitialChParagraph() {
        return ParagraphsOfChapter.builder()
                .selectedParagraphs(Collections.emptyList())
                .myParagraph(null)
                .bestParagraph(null)
                .build();
    }

    private ParagraphsOfChapter getCompletedChParagraph(List<Paragraph> paragraphs, Author author) {
        List<ParagraphSelected> selectedParagraphs = paragraphs.stream()
                .filter(paragraph -> paragraph.getParagraphStatus() == ParagraphStatus.SELECTED)
                .map(paragraph -> ParagraphSelected.fromParagraph(paragraph, isLikedParagraph(paragraph, author)))
                .toList();
        return ParagraphsOfChapter.builder()
                .selectedParagraphs(selectedParagraphs)
                .myParagraph(null)
                .bestParagraph(null)
                .build();
    }

    private ParagraphsOfChapter getInProgressChParagraph(List<Paragraph> paragraphs, Author author) {
        List<ParagraphSelected> selectedParagraphs = new ArrayList<>();
        ParagraphInVoting myParagraph = null, bestParagraph = null;

        int maxVoteCnt = 0, voteCnt;
        for (Paragraph paragraph : paragraphs) {
            ParagraphStatus status = paragraph.getParagraphStatus();
            voteCnt = voteRepository.countAllByParagraph(paragraph);

            if (status == ParagraphStatus.IN_VOTING && // 내가 쓴 한줄
                            paragraph.getAuthor().getId().equals(author.getId())) {
                myParagraph = ParagraphInVoting.fromParagraph(paragraph, voteCnt);
            }
            if (status == ParagraphStatus.IN_VOTING && // 베스트 한줄
                            voteCnt > maxVoteCnt) {
                bestParagraph = ParagraphInVoting.fromParagraph(paragraph, voteCnt);
                maxVoteCnt = voteCnt;
            }
            if (status == ParagraphStatus.SELECTED) { // 이미 선정된 한줄
                selectedParagraphs.add(ParagraphSelected.fromParagraph(paragraph, isLikedParagraph(paragraph, author)));
            }
        }

        return ParagraphsOfChapter.builder()
                .selectedParagraphs(selectedParagraphs)
                .myParagraph(myParagraph)
                .bestParagraph(bestParagraph)
                .build();
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

    private boolean isLikedParagraph(Paragraph paragraph, Author author) {
        Optional<ParagraphLike> paragraphLike = paragraphLikeRepository.findByParagraphAndAuthor(paragraph, author);
        return paragraphLike.isPresent();
    }
}

package us.usserver.paragraph.service;

import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import us.usserver.author.Author;
import us.usserver.author.AuthorMother;
import us.usserver.chapter.Chapter;
import us.usserver.chapter.ChapterMother;
import us.usserver.chapter.chapterEnum.ChapterStatus;
import us.usserver.global.EntityService;
import us.usserver.like.paragraph.ParagraphLike;
import us.usserver.like.paragraph.ParagraphLikeRepository;
import us.usserver.novel.Novel;
import us.usserver.novel.NovelMother;
import us.usserver.novel.novelEnum.AgeRating;
import us.usserver.novel.novelEnum.Genre;
import us.usserver.novel.novelEnum.Hashtag;
import us.usserver.paragraph.Paragraph;
import us.usserver.paragraph.ParagraphMother;
import us.usserver.paragraph.ParagraphRepository;
import us.usserver.paragraph.dto.GetParagraphsRes;
import us.usserver.paragraph.dto.ParagraphInVoting;
import us.usserver.paragraph.dto.PostParagraphReq;
import us.usserver.paragraph.paragraphEnum.ParagraphStatus;
import us.usserver.stake.StakeService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
class ParagraphServiceV0Test {
    @Autowired
    private ParagraphServiceV0 paragraphServiceV0;
    @Autowired
    private EntityService entityService;
    @Autowired
    private ParagraphRepository paragraphRepository;
    @Autowired
    private ParagraphLikeRepository paragraphLikeRepository;
    @Autowired
    private StakeService stakeService;

    private Author author;
    private Novel novel;
    private Chapter chapter1;
    private Chapter chapter2;
    private Chapter chapter3;
    private Paragraph paragraph1;
    private Paragraph paragraph2;

    @BeforeEach
    public void setUp() {
        author = AuthorMother.generateAuthor();
        novel = NovelMother.generateNovel(author);
        chapter1 = ChapterMother.generateChapter(novel);
        chapter2 = ChapterMother.generateChapter(novel);
        chapter3 = ChapterMother.generateChapter(novel);

        paragraph1 = ParagraphMother.generateParagraph(author, chapter1);
        paragraph2 = ParagraphMother.generateParagraph(author, chapter1);

        Mockito.lenient().when(entityService.getAuthor(anyLong())).thenReturn(author);
        Mockito.lenient().when(entityService.getNovel(anyLong())).thenReturn(novel);
        Mockito.lenient().when(entityService.getChapter(1L)).thenReturn(chapter1);
        Mockito.lenient().when(entityService.getChapter(2L)).thenReturn(chapter2);
        Mockito.lenient().when(entityService.getChapter(3L)).thenReturn(chapter3);
        Mockito.lenient().when(entityService.getParagraph(1L)).thenReturn(paragraph1);
        Mockito.lenient().when(entityService.getParagraph(2L)).thenReturn(paragraph2);
        Mockito.lenient().when(paragraphLikeRepository.countAllByParagraph(paragraph1))
                .thenReturn(paragraphLikeCnt);
    }

    @Test
    @DisplayName("회차 보기")
    void getParagraphs() {
        // given
        Mockito.when(paragraphRepository.findAllByChapter(chapter1))
                .thenReturn(Collections.singletonList(paragraph1));
        Mockito.when(paragraphRepository.findAllByChapter(chapter2))
                .thenReturn(Collections.singletonList(paragraph1));

        // when
        GetParagraphsRes paragraphs1 = paragraphServiceV0.getParagraphs(1L, 1L);
        GetParagraphsRes paragraphs2 = paragraphServiceV0.getParagraphs(1L, 2L);

        // then
        assertNotNull(paragraphs1.getMyParagraph());
        assertNotNull(paragraphs1.getBestParagraph());
        assertNull(paragraphs2.getMyParagraph());
        assertNull(paragraphs2.getBestParagraph());

        Mockito.verify(paragraphRepository).findAllByChapter(chapter1);
        Mockito.verify(paragraphRepository).findAllByChapter(chapter2);
    }

    @Test
    @DisplayName("처음 시작된 회차 보기")
    void getInitialChParagraph() {
        // given
        Mockito.when(paragraphRepository.findAllByChapter(chapter3))
                .thenReturn(Collections.emptyList());

        // when
        GetParagraphsRes paragraphs = paragraphServiceV0.getParagraphs(1L, 3L);

        // then
        assertNull(paragraphs.getSelectedParagraphs());
        assertNull(paragraphs.getMyParagraph());
        assertNull(paragraphs.getBestParagraph());
        assertNotNull(paragraphs);
    }

    @Test
    @DisplayName("투표 중인 한줄들 보기")
    void getInVotingParagraphs() {
        // given
        List<Paragraph> paragraphs = Arrays.asList(paragraph1, paragraph2);
        List<ParagraphInVoting> paragraphInVotings = paragraphs.stream()
                .map(paragraph -> ParagraphInVoting.fromParagraph(paragraph, 0)).toList();

        Mockito.when(paragraphRepository.findAllByChapter(chapter1))
                .thenReturn(paragraphs);

        // when
        List<ParagraphInVoting> inVotingParagraphs = paragraphServiceV0.getInVotingParagraphs(1L);

        // then
        assertEquals(paragraphInVotings.size(), inVotingParagraphs.size());
        for (int i = 0; i < paragraphInVotings.size(); i++) {
            assertEquals(paragraphInVotings.get(i).getContent(), inVotingParagraphs.get(i).getContent());
        }

    }

    @Test
    void postParagraph() {
        // TODO: MOCK 객체의 한계로 인해 테스트 코드가 너무 복잡해 짐, @Autowired 로 생성자 주입 받아 다시 작성 예정
//        // given
//        PostParagraphReq req = PostParagraphReq.builder()
//                .content("TEST")
//                .build();
//        Mockito.lenient().when(paragraphRepository.save(paragraph1)).thenReturn(paragraph1);
//        List<Paragraph> paragraphs = Arrays.asList(paragraph1, paragraph2);
//
//        // when
//        ParagraphInVoting paragraphInVoting = paragraphServiceV0.postParagraph(1L, 1L, req);
//
//        List<Integer> sequences = paragraphs.stream().map(Paragraph::getSequence).toList();
//        Integer maxSequence = Collections.max(sequences);
//
//        // then
//        Assertions.assertThat(paragraphInVoting.getSequence()).isEqualTo(maxSequence + 1);
    }

    @Test
    void selectParagraph() {
    }
}
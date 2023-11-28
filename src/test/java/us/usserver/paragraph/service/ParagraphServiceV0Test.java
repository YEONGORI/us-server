package us.usserver.paragraph.service;

import org.aspectj.lang.annotation.Before;
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
import us.usserver.chapter.Chapter;
import us.usserver.chapter.chapterEnum.ChapterStatus;
import us.usserver.global.EntityService;
import us.usserver.like.paragraph.ParagraphLike;
import us.usserver.like.paragraph.ParagraphLikeRepository;
import us.usserver.novel.Novel;
import us.usserver.novel.novelEnum.AgeRating;
import us.usserver.novel.novelEnum.Genre;
import us.usserver.novel.novelEnum.Hashtag;
import us.usserver.paragraph.Paragraph;
import us.usserver.paragraph.ParagraphRepository;
import us.usserver.paragraph.dto.GetParagraphsRes;
import us.usserver.paragraph.paragraphEnum.ParagraphStatus;
import us.usserver.stake.StakeService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class ParagraphServiceV0Test {
    @InjectMocks
    private ParagraphServiceV0 paragraphServiceV0;
    @Mock
    private EntityService entityService;
    @Mock
    private ParagraphRepository paragraphRepository;
    @Mock
    private ParagraphLikeRepository paragraphLikeRepository;
    @Mock
    private StakeService stakeService;

    private Author author;
    private Novel novel;
    private Chapter chapter1;
    private Chapter chapter2;
    private Chapter chapter3;
    private Paragraph paragraph;
    private int paragraphLikeCnt;

    @BeforeEach
    public void setUp() {
        author = Author.builder()
                .id(1L)
                .nickname("NICKNAME")
                .introduction("INTRODUCTION")
                .profileImg("PROFILE_IMG")
                .build();

        novel = Novel.builder()
                .id(1L)
                .title("NOVEL_TITLE")
                .thumbnail("THUMBNAIL")
                .synopsis("SYNOPSIS")
                .authorDescription("AUTHOR_DESCRIPTION")
                .hashtag(Collections.singleton(Hashtag.HASHTAG1))
                .genre(Genre.FANTASY)
                .ageRating(AgeRating.GENERAL)
                .author(author)
                .build();

        chapter1 = Chapter.builder()
                .id(1L)
                .title("PARAGRAPH_TITLE_1")
                .part(1)
                .status(ChapterStatus.IN_PROGRESS)
                .novel(novel)
                .build();

        chapter2 = Chapter.builder()
                .id(2L)
                .title("PARAGRAPH_TITLE_2")
                .part(2)
                .status(ChapterStatus.COMPLETED)
                .novel(novel)
                .build();

        chapter3 = Chapter.builder()
                .id(3L)
                .title("PARAGRAPH_TITLE_3")
                .part(3)
                .status(ChapterStatus.IN_PROGRESS)
                .novel(novel)
                .build();

        paragraph = Paragraph.builder()
                .id(1L)
                .paragraphStatus(ParagraphStatus.IN_VOTING)
                .content("PARAGRAPH_CONTENT")
                .chapter(chapter1)
                .author(author)
                .sequence(0)
                .build();


        Mockito.lenient().when(entityService.getAuthor(anyLong())).thenReturn(author);
        Mockito.lenient().when(entityService.getNovel(anyLong())).thenReturn(novel);
        Mockito.lenient().when(entityService.getChapter(1L)).thenReturn(chapter1);
        Mockito.lenient().when(entityService.getChapter(2L)).thenReturn(chapter2);
        Mockito.lenient().when(entityService.getChapter(3L)).thenReturn(chapter3);
        Mockito.lenient().when(entityService.getParagraph(anyLong())).thenReturn(paragraph);
        Mockito.lenient().when(paragraphLikeRepository.countAllByParagraph(paragraph))
                .thenReturn(paragraphLikeCnt);
    }

    @Test
    @DisplayName("회차 보기")
    void getParagraphs() {
        // when
        Mockito.when(paragraphRepository.findAllByChapter(chapter1))
                .thenReturn(Collections.singletonList(paragraph));
        Mockito.when(paragraphRepository.findAllByChapter(chapter2))
                .thenReturn(Collections.singletonList(paragraph));

        // given
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
        // when
        Mockito.when(paragraphRepository.findAllByChapter(chapter3))
                .thenReturn(Collections.emptyList());

        // given
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

    }

    @Test
    void postParagraph() {

    }

    @Test
    void selectParagraph() {
    }
}
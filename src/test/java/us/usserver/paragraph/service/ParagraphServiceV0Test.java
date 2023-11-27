package us.usserver.paragraph.service;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
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
import us.usserver.global.EntityService;
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
    private Chapter chapter;
    private Paragraph paragraph;

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

        chapter = Chapter.builder()
                .id(1L)
                .title("PARAGRAPH_TITLE")
                .part(1)
                .novel(novel)
                .build();

        paragraph = Paragraph.builder()
                .id(1L)
                .paragraphStatus(ParagraphStatus.IN_VOTING)
                .content("PARAGRAPH_CONTENT")
                .chapter(chapter)
                .author(author)
                .sequence(0)
                .build();




        Mockito.when(entityService.getAuthor(anyLong())).thenReturn(author);
        Mockito.when(entityService.getNovel(anyLong())).thenReturn(novel);
        Mockito.when(entityService.getChapter(anyLong())).thenReturn(chapter);
        Mockito.when(entityService.getParagraph(anyLong())).thenReturn(paragraph);
    }

    @Test
    void getParagraphs() {
        Mockito.when(paragraphRepository.findAllByChapter(any(Chapter.class)))
                .thenReturn(Collections.singletonList(paragraph));

        GetParagraphsRes paragraphs = paragraphServiceV0.getParagraphs(1L, 1L);

        assertNotNull(paragraphs);
        Mockito.verify(paragraphRepository).findAllByChapter(any(Chapter.class));
    }

    @Test
    void getInVotingParagraphs() {
    }

    @Test
    void postParagraph() {
    }

    @Test
    void selectParagraph() {
    }
}
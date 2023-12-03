package us.usserver.chapter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import us.usserver.author.Author;
import us.usserver.author.AuthorRepository;
import us.usserver.chapter.dto.ChaptersOfNovel;
import us.usserver.chapter.dto.CreateChapterReq;
import us.usserver.novel.Novel;
import us.usserver.novel.NovelRepository;
import us.usserver.novel.novelEnum.AgeRating;
import us.usserver.novel.novelEnum.Genre;
import us.usserver.novel.novelEnum.Hashtag;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class ChapterServiceV0Test {
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private NovelRepository novelRepository;
    @Autowired
    private ChapterServiceV0 chapterServiceV0;

    private Novel novel;
    private Author author;

    @BeforeEach
    void setUp() {
        Set<Hashtag> hashtags = new HashSet<>();
        hashtags.add(Hashtag.HASHTAG1);
        hashtags.add(Hashtag.HASHTAG2);
        hashtags.add(Hashtag.MONCHKIN);

        author = Author.builder()
                .id(1L)
                .nickname("NICKNAME")
                .introduction("INTRODUCTION")
                .profileImg("PROFILE_IMG")
                .build();
        authorRepository.save(author);

        novel = Novel.builder()
                .id(1L)
                .title("TITLE")
                .thumbnail("THUMBNAIL")
                .synopsis("SYNOPSIS")
                .author(author)
                .authorDescription("AUTHOR_DESCRIPTION")
                .hashtag(hashtags)
                .genre(Genre.FANTASY)
                .ageRating(AgeRating.GENERAL)
                .build();
        novelRepository.save(novel);
    }

    @Test
    @DisplayName("회차 생성")
    void createChapter() {
        CreateChapterReq createChapterReq1 = CreateChapterReq.builder()
                .title("첫번 째 이야기")
                .build();

        assertDoesNotThrow(
                () -> chapterServiceV0.createChapter(1L, 1L));
    }

    @Test
    @DisplayName("소설 회차 정보 조회")
    void getChaptersOfNovel() {
        int prevSize = chapterServiceV0.getChaptersOfNovel(1L).size();
        assertDoesNotThrow(
                () -> {
                    chapterServiceV0.createChapter(1L, 1L);
                    chapterServiceV0.createChapter(1L, 1L);
                }
        );

        List<ChaptersOfNovel> chaptersOfNovels = assertDoesNotThrow(
                () -> chapterServiceV0.getChaptersOfNovel(1L));

        assertThat(chaptersOfNovels.size()).isEqualTo(prevSize + 2);
    }
}
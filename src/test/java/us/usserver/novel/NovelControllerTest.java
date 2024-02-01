package us.usserver.novel;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import us.usserver.author.Author;
import us.usserver.author.AuthorMother;
import us.usserver.author.AuthorRepository;
import us.usserver.chapter.Chapter;
import us.usserver.chapter.ChapterMother;
import us.usserver.chapter.ChapterRepository;
import us.usserver.member.Member;
import us.usserver.member.MemberMother;
import us.usserver.member.MemberRepository;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Rollback
@AutoConfigureMockMvc
@SpringBootTest
class NovelControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private NovelRepository novelRepository;
    @Autowired
    private ChapterRepository chapterRepository;

    private Member member;
    private Author author;
    private Novel novel;
    private Chapter chapter1;
    private Chapter chapter2;
    private Chapter chapter3;

    @BeforeEach
    void setUp() {
        member = MemberMother.generateMember();
        author = AuthorMother.generateAuthor();
        author.setMember(member);

        novel = NovelMother.generateNovel(author);
        chapter1 = ChapterMother.generateChapter(novel);
        chapter1.setPartForTest(1);
        chapter2 = ChapterMother.generateChapter(novel);
        chapter2.setPartForTest(2);
        chapter3 = ChapterMother.generateChapter(novel);
        chapter3.setPartForTest(3);
        novel.getChapters().add(chapter1);
        novel.getChapters().add(chapter2);
        novel.getChapters().add(chapter3);

        memberRepository.save(member);
        authorRepository.save(author);
        novelRepository.save(novel);
        chapterRepository.save(chapter1);
        chapterRepository.save(chapter2);
        chapterRepository.save(chapter3);
    }

    @Test
    @DisplayName("소설 정보 조회")
    void getNovelInfo() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/novel/" + novel.getId())
                .contentType(MediaType.APPLICATION_JSON));
        String resultString = resultActions.andReturn().getResponse().getContentAsString();

        // then
        assertThat(resultString).contains(novel.getTitle());
        assertThat(resultString).contains(novel.getGenre().toString());
        novel.getHashtags().forEach(hashtag ->
                assertThat(resultString).contains(hashtag.toString()));
        assertThat(resultString).contains("\"nickName\":\"" + author.getNickname());
        assertThat(resultString).contains("joinedAuthorCnt\":");
        assertThat(resultString).contains("likeCnt\":");
    }

    @Test
    @DisplayName("소설 상세 정보 조회")
    void getNovelDetailInfo() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/novel/" + novel.getId() + "/detail")
                .contentType(MediaType.APPLICATION_JSON));
        String resultString = resultActions.andReturn().getResponse().getContentAsString();

        // then
        assertThat(resultString).contains(novel.getTitle());
        assertThat(resultString).contains(novel.getThumbnail());
        assertThat(resultString).contains(novel.getSynopsis());
        assertThat(resultString).contains(author.getNickname());
        assertThat(resultString).contains(novel.getAgeRating().toString());
        assertThat(resultString).contains(novel.getGenre().toString());
        novel.getHashtags().forEach(hashtag ->
                assertThat(resultString).contains(hashtag.toString()));
        assertThat(resultString).contains(chapter1.getTitle());
        assertThat(resultString).contains(chapter2.getTitle());
        assertThat(resultString).contains(chapter3.getTitle());


    }

    @Test
    @DisplayName("소설 소개 수정")
    void modifyNovelSynopsis() {
    }
}
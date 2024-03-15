package us.usserver.author.controller;

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
import org.springframework.transaction.annotation.Transactional;
import us.usserver.author.AuthorMother;
import us.usserver.chapter.ChapterMother;
import us.usserver.domain.author.entity.Author;
import us.usserver.domain.author.entity.ReadNovel;
import us.usserver.domain.author.repository.AuthorRepository;
import us.usserver.domain.author.repository.ReadNovelRepository;
import us.usserver.domain.authority.entity.Authority;
import us.usserver.domain.authority.repository.AuthorityRepository;
import us.usserver.domain.chapter.entity.Chapter;
import us.usserver.domain.chapter.repository.ChapterRepository;
import us.usserver.domain.member.entity.Member;
import us.usserver.domain.member.repository.MemberRepository;
import us.usserver.domain.novel.entity.Novel;
import us.usserver.domain.novel.repository.NovelRepository;
import us.usserver.domain.paragraph.constant.ParagraphStatus;
import us.usserver.domain.paragraph.entity.Paragraph;
import us.usserver.domain.paragraph.repository.ParagraphRepository;
import us.usserver.global.EntityFacade;
import us.usserver.member.MemberMother;
import us.usserver.novel.NovelMother;
import us.usserver.paragraph.ParagraphMother;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Rollback
@AutoConfigureMockMvc
@SpringBootTest
class BookshelfControllerTest {
    @Autowired
    private EntityFacade entityFacade;

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private NovelRepository novelRepository;
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private ParagraphRepository paragraphRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private ReadNovelRepository readNovelRepository;

    @Autowired
    private MockMvc mockMvc;

    private static final String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImlkIjoxOCwiZXhwIjoxNzEwNDkwMTE3fQ.zHAYX9q2zzxrssPxjIo2VibZifS5jIyIjSScPSXvSQ6cBZ8qulALqwlU0GyKY--znhBzw4cxsdjfNFM-8vkBpQ";
    private static final String refreshToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSZWZyZXNoVG9rZW4iLCJpZCI6MTgsImV4cCI6MTcxMjExMDExN30.LYcTv53rn4rMiANJ3_9gCCm4w_ZEDI5-j7vbt-QB8uvWyZUwbAg9Dmy7q3YMhNFtJJCi1KbHKPMS1ozp0rw7Sw";

    Author author;
    Member member;
    Novel novel;
    Chapter chapter;
    Paragraph paragraph1;
    Paragraph paragraph2;
    Paragraph paragraph3;
    Authority authority;

    private static final String TITLE = "title";
    private static final String MAINAUTHOR = "mainAuthor";
    private static final String JOINEDAUTHOR = "joinedAuthor";
    private static final String THUMBNAIL = "thumbnail";
    private static final String SHORTCUTS = "shortcus";

    @BeforeEach
    void setUp() {
        member = MemberMother.generateMember();
        author = AuthorMother.generateAuthorWithMember(member);
        member.setAuthor(author);
        memberRepository.save(member);

        novel = NovelMother.generateNovel(author);
        chapter = ChapterMother.generateChapter(novel);
        paragraph1 = ParagraphMother.generateParagraph(author, chapter);
        paragraph2 = ParagraphMother.generateParagraph(author, chapter);
        paragraph3 = ParagraphMother.generateParagraph(author, chapter);
        paragraph1.setParagraphStatusForTest(ParagraphStatus.SELECTED);
        authority = Authority.builder().author(author).novel(novel).build();

        novel.getChapters().add(chapter);
        chapter.getParagraphs().add(paragraph1);
        chapter.getParagraphs().add(paragraph2);
        chapter.getParagraphs().add(paragraph3);

        novelRepository.save(novel);
        chapterRepository.save(chapter);
        paragraphRepository.save(paragraph1);
        paragraphRepository.save(paragraph2);
        paragraphRepository.save(paragraph3);
        authorityRepository.save(authority);
    }

    @Test
    @DisplayName("최근 본 소설 불러오기 API TEST")
    @Transactional
    void recentViewedNovels2() throws Exception {
        // given
        Author curAuthor = entityFacade.getAuthorByMemberId(18L);
        ReadNovel readNovel = ReadNovel.builder().readDate(LocalDateTime.now()).novel(novel).author(curAuthor).build();
        curAuthor.addReadNovel(readNovel);

        // when
        authorRepository.save(curAuthor);
        readNovelRepository.save(readNovel);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/bookshelf/viewed")
                .header("Authorization", "Bearer " + accessToken)
                .header("Authorization-Refresh", "Bearer " + refreshToken)
                .contentType(MediaType.APPLICATION_JSON));
        String resultString = resultActions.andReturn().getResponse().getContentAsString();

        // then
        assertThat(resultString).contains(novel.getTitle());
    }

    @Test
    @DisplayName("최근 본 소설이 없는 API TEST")
    void recentViewedNovels3() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/bookshelf/viewed")
                .header("Authorization", "Bearer " + accessToken)
                .header("Authorization-Refresh", "Bearer " + refreshToken)
                .contentType(MediaType.APPLICATION_JSON));
        String resultString = resultActions.andReturn().getResponse().getContentAsString();

        // then
        assertThat(resultString).contains("[]"); // 빈 리스트 리턴
    }

    @Test
    @DisplayName("최근 본 소설 삭제 API TEST")
    @Transactional
    void deleteRecentViewedNovels() throws Exception {
        // given
        Author curAuthor = entityFacade.getAuthorByMemberId(18L);
        ReadNovel readNovel = ReadNovel.builder().readDate(LocalDateTime.now()).novel(novel).author(curAuthor).build();
        curAuthor.addReadNovel(readNovel);

        // when
        authorRepository.save(curAuthor);
        ReadNovel savedReadNovel = readNovelRepository.save(readNovel);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete("/bookshelf/viewed/" + savedReadNovel.getId())
                .header("Authorization", "Bearer " + accessToken)
                .header("Authorization-Refresh", "Bearer " + refreshToken)
                .contentType(MediaType.APPLICATION_JSON));
        String resultString = resultActions.andReturn().getResponse().getContentAsString();

        // then
        assertThat(resultString).contains("success");
    }

    @Test
    @DisplayName("내가 만든 소설 API TEST")
    void createdNovels() throws Exception {
        // given
        Author curAuthor = entityFacade.getAuthorByMemberId(18L);
        Novel newNovel = NovelMother.generateNovel(curAuthor);

        // when
        novelRepository.save(newNovel);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/bookshelf/created")
                .header("Authorization", "Bearer " + accessToken)
                .header("Authorization-Refresh", "Bearer " + refreshToken)
                .contentType(MediaType.APPLICATION_JSON));
        String resultString = resultActions.andReturn().getResponse().getContentAsString();

        // then
        assertThat(resultString).contains(newNovel.getTitle());
    }

    @Test
    @DisplayName("내가 만든 소설이 없는 API TEST")
    void createdNovels2() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/bookshelf/created")
                .header("Authorization", "Bearer " + accessToken)
                .header("Authorization-Refresh", "Bearer " + refreshToken)
                .contentType(MediaType.APPLICATION_JSON));
        String resultString = resultActions.andReturn().getResponse().getContentAsString();

        // then
        assertThat(resultString).contains("[]");
    }

    @Test
    void deleteCreatedNovels() {
    }

    @Test
    void joinedNovels() {
        // given

        // when

        // then
    }

    @Test
    void deleteJoinedNovels() {
    }

    @Test
    void likedNovels() {
        // given

        // when

        // then
    }

    @Test
    void deleteLikedNovels() {
    }
}
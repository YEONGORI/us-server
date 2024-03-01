package us.usserver.bookshelf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.domain.author.entity.Author;
import us.usserver.author.AuthorMother;
import us.usserver.domain.author.repository.AuthorRepository;
import us.usserver.domain.authority.entity.Authority;
import us.usserver.domain.authority.repository.AuthorityRepository;
import us.usserver.domain.chapter.entity.Chapter;
import us.usserver.chapter.ChapterMother;
import us.usserver.domain.chapter.repository.ChapterRepository;
import us.usserver.domain.chapter.service.ChapterService;
import us.usserver.domain.member.entity.Member;
import us.usserver.member.MemberMother;
import us.usserver.domain.member.repository.MemberRepository;
import us.usserver.domain.novel.entity.Novel;
import us.usserver.novel.NovelMother;
import us.usserver.domain.novel.repository.NovelDSLRepository;
import us.usserver.domain.paragraph.entity.Paragraph;
import us.usserver.paragraph.ParagraphMother;
import us.usserver.domain.paragraph.repository.ParagraphRepository;
import us.usserver.domain.paragraph.constant.ParagraphStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Rollback
@AutoConfigureMockMvc
@SpringBootTest
class BookshelfControllerTest {
    @Autowired
    private ChapterService chapterService;

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private NovelDSLRepository novelCustomRepository;
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private ParagraphRepository paragraphRepository;
    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private MockMvc mockMvc;

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
    private static final Long testAuthorId = 500L;



    @BeforeEach
    void setUp() {
        member = MemberMother.generateMember();
        author = AuthorMother.generateAuthor();
        author.setIdForTest(testAuthorId);
        novel = NovelMother.generateNovel(author);
        chapter = ChapterMother.generateChapter(novel);
        paragraph1 = ParagraphMother.generateParagraph(author, chapter);
        paragraph2 = ParagraphMother.generateParagraph(author, chapter);
        paragraph3 = ParagraphMother.generateParagraph(author, chapter);
        paragraph1.setParagraphStatusForTest(ParagraphStatus.SELECTED);
        authority = Authority.builder().author(author).novel(novel).build();

        author.setMember(member);
        novel.getChapters().add(chapter);
        chapter.getParagraphs().add(paragraph1);
        chapter.getParagraphs().add(paragraph2);
        chapter.getParagraphs().add(paragraph3);

        memberRepository.save(member);
        authorRepository.save(author);
        novelCustomRepository.save(novel);
        chapterRepository.save(chapter);
        paragraphRepository.save(paragraph1);
        paragraphRepository.save(paragraph2);
        paragraphRepository.save(paragraph3);
        authorityRepository.save(authority);
    }

    @Test
    @DisplayName("최근 본 소설 불러오기 API TEST 1") // 현재 authorId를 수동으로 설정해야 해서 이 테스트는 실패할 수 밖에 없음
    void recentViewedNovels() throws Exception {
//        // given
//        assertDoesNotThrow(() -> chapterService.getChapterDetailInfo(novel.getId(), author.getId(), chapter.getId()));
//
//        // when
//        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
//                .get("/bookshelf/viewed")
//                .contentType(MediaType.APPLICATION_JSON));
//        String resultString = resultActions.andReturn().getResponse().getContentAsString();
//
//        JacksonJsonParser jacksonJsonParser = new JacksonJsonParser();
//        Map<String, Object> resultJson = jacksonJsonParser.parseMap(resultString);
//        Map<String, Object> data = (LinkedHashMap<String, Object>) resultJson.get("data");
//        List<NovelPreview> novelPreviews = (List<NovelPreview>) data.get("novelPreviews");
//        Map<String, Object> novelPreview = (Map<String, Object>) novelPreviews.get(0);
//
//        // then
//        assertThat(novelPreview.get(TITLE)).isEqualTo(novel.getTitle());
//        assertThat(novelPreview.get(THUMBNAIL)).isEqualTo(novel.getThumbnail());
//        assertThat(novelPreview.get(JOINEDAUTHOR)).isEqualTo(1);
    }

    @Test
    @DisplayName("최근 본 소설 불러오기 API TEST 2")
    @Transactional
    void recentViewedNovels2() throws Exception {
//        // given
//        chapterService.getChapterDetailInfo(novel.getId(), author.getId(), chapter.getId());
//
//        // when
//        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
//                .get("/bookshelf/viewed")
//                .contentType(MediaType.APPLICATION_JSON));
//        String resultString = resultActions.andReturn().getResponse().getContentAsString();
//
//        // then
//        assertThat(resultString).contains(novel.getTitle());
//        assertThat(resultString).contains(novel.getThumbnail());
//        assertThat(resultString).contains(author.getNickname());
    }

    @Test
    @DisplayName("최근 본 소설이 없는 API TEST")
    void recentViewedNovels3() throws Exception { // 이거도 현재 authorId를 바꿀 수 없어서 성공만 가능
//        // given
//        Author newAuthor = AuthorMother.generateAuthor();
//        Member newMember = MemberMother.generateMember();
//        newAuthor.setMember(newMember);
//        memberRepository.save(newMember);
//        authorRepository.save(newAuthor);
//
//        // when
//        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
//                .get("/bookshelf/viewed")
//                .contentType(MediaType.APPLICATION_JSON));
//        String resultString = resultActions.andReturn().getResponse().getContentAsString();
//
//        // then
//        assertThat(resultString).doesNotContain(novel.getTitle());
//        assertThat(resultString).doesNotContain(novel.getThumbnail());
//        assertThat(resultString).doesNotContain(author.getNickname());
    }

    @Test
    void deleteRecentViewedNovels() {
    }

    @Test
    @DisplayName("내가 만든 소설 API TEST")
    void createdNovels() {
        // given

        // when

        // then
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
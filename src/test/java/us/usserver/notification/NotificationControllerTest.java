package us.usserver.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import us.usserver.domain.member.entity.Author;
import us.usserver.author.AuthorMother;
import us.usserver.domain.member.repository.AuthorRepository;
import us.usserver.domain.chapter.entity.Chapter;
import us.usserver.chapter.ChapterMother;
import us.usserver.domain.chapter.repository.ChapterRepository;
import us.usserver.domain.member.entity.Member;
import us.usserver.member.MemberMother;
import us.usserver.domain.member.repository.MemberRepository;
import us.usserver.domain.novel.Novel;
import us.usserver.novel.NovelMother;
import us.usserver.domain.novel.repository.NovelDSLRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Rollback
@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private NovelDSLRepository novelCustomRepository;
    @Autowired
    private ChapterRepository chapterRepository;

    private Author author;
    private Member member;
    private Novel novel;
    private Chapter chapter;

    private static final Long defaultId = 500L;


    @BeforeEach
    void setUp() {
        member = MemberMother.generateMember();
        author = AuthorMother.generateAuthor();
        author.setMember(member);
        author.setIdForTest(defaultId);

        novel = NovelMother.generateNovel(author);
        novel.setIdForTest(defaultId);
        chapter = ChapterMother.generateChapter(novel);
        novel.getChapters().add(chapter);

        memberRepository.save(member);
        authorRepository.save(author);
        novelCustomRepository.save(novel);
        chapterRepository.save(chapter);
    }

    @Test
    @DisplayName("구독 연결 테스트")
    void subscribe() throws Exception {
        // given
        String subscribeUrl = "/notification/subscribe";

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(subscribeUrl)
                .accept(MediaType.TEXT_EVENT_STREAM));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.content().contentType(MediaType.TEXT_EVENT_STREAM));
        resultActions.andExpect(MockMvcResultMatchers.header().string(HttpHeaders.TRANSFER_ENCODING, "chunked"));
        resultActions.andDo(MockMvcResultHandlers.print());

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        assertTrue(responseBody.contains("event:sse\ndata:EventStream Created. [receiverId ="));
    }

    @Test
    @DisplayName("서버 메시지 발신 테스트")
    void send() throws Exception {
        // given
        String chapterCreateUrl = "/chapter/" + chapter.getId();

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(chapterCreateUrl)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated());
    }
}

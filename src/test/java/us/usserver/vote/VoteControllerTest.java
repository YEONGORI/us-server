package us.usserver.vote;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import us.usserver.novel.Novel;
import us.usserver.novel.NovelMother;
import us.usserver.novel.NovelRepository;
import us.usserver.paragraph.Paragraph;
import us.usserver.paragraph.ParagraphMother;
import us.usserver.paragraph.paragraphEnum.ParagraphStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Rollback
@AutoConfigureMockMvc
@SpringBootTest
class VoteControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private VoteRepository voteRepository;

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
    private Chapter chapter;
    private Paragraph paragraph1;
    private Paragraph paragraph2;
    private Paragraph paragraph3;
    private Paragraph paragraph4;
    private Paragraph paragraph5;

    private static final Long defaultId = 500L;

    @BeforeEach
    void setUp() {
        member = MemberMother.generateMember();
        author = AuthorMother.generateAuthor();
        author.setMember(member);

        novel = NovelMother.generateNovel(author);
        chapter = ChapterMother.generateChapter(novel);
        chapter.setPartForTest(1);
        novel.getChapters().add(chapter);

        paragraph1 = ParagraphMother.generateParagraph(author, chapter);
        paragraph2 = ParagraphMother.generateParagraph(author, chapter);
        paragraph3 = ParagraphMother.generateParagraph(author, chapter);
        paragraph4 = ParagraphMother.generateParagraph(author, chapter);
        paragraph5 = ParagraphMother.generateParagraph(author, chapter);
        paragraph1.setSequenceForTest(1);
        paragraph2.setSequenceForTest(2);
        paragraph3.setSequenceForTest(3);
        paragraph4.setSequenceForTest(4);
        paragraph5.setSequenceForTest(4);
        paragraph1.setParagraphStatusForTest(ParagraphStatus.SELECTED);
        paragraph2.setParagraphStatusForTest(ParagraphStatus.SELECTED);
        paragraph3.setParagraphStatusForTest(ParagraphStatus.SELECTED);
        paragraph4.setParagraphStatusForTest(ParagraphStatus.IN_VOTING);
        paragraph5.setParagraphStatusForTest(ParagraphStatus.IN_VOTING);
        chapter.getParagraphs().add(paragraph1);
        chapter.getParagraphs().add(paragraph2);
        chapter.getParagraphs().add(paragraph3);
        chapter.getParagraphs().add(paragraph4);
        chapter.getParagraphs().add(paragraph5);

        memberRepository.save(member);
        authorRepository.save(author);
        author.setIdForTest(defaultId);
        authorRepository.save(author);
        novelRepository.save(novel);
        chapterRepository.save(chapter);
    }

    @Test
    @DisplayName("투표 하기 api test")
    void voting() throws Exception {
        // given

        // when
        List<Vote> prevVotes = voteRepository.findAllByAuthor(author);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/vote/" + paragraph4.getId())
                .contentType(MediaType.APPLICATION_JSON));
        List<Vote> nextVotes = voteRepository.findAllByAuthor(author);

        // then
        boolean anyMatch = nextVotes.stream().anyMatch(vote -> vote.getParagraph().getId().equals(paragraph4.getId()));
        assertThat(anyMatch).isTrue();
        assertThat(prevVotes.size()).isLessThan(nextVotes.size());
    }

    @Test
    @DisplayName("중복 투표 api test")
    void voting2() throws Exception {
        // given

        // when
        ResultActions resultActions1 = mockMvc.perform(MockMvcRequestBuilders
                .post("/vote/" + paragraph4.getId())
                .contentType(MediaType.APPLICATION_JSON));
        ResultActions resultActions2 = mockMvc.perform(MockMvcRequestBuilders
                .post("/vote/" + paragraph5.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions1.andExpect(status().isOk());
        resultActions2.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("투표 취소 하기 api test")
    void cancelVote() throws Exception {
        // given
        Vote vote = Vote.builder().paragraph(paragraph5).author(author).build();
        vote = voteRepository.save(vote);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/vote/" + vote.getId())
                .contentType(MediaType.APPLICATION_JSON));
        List<Vote> votes = voteRepository.findAllByAuthor(author);


        // then
        resultActions.andExpect(status().isOk());
        votes.forEach(v -> assertThat(v.getParagraph()).isNotEqualTo(paragraph5));
    }

    @Test
    @DisplayName("내가 하지 않은 투표 취소 하기 api test")
    void cancelVote2() throws Exception {
        // 현 컨트롤러에 기본 authorId가 500으로 설정되어 있어. 본 테스트에서 만든 author를 사용하면 권한이 없을 수 밖에 없음
        // given
        Vote vote = Vote.builder().paragraph(paragraph5).author(author).build();
        vote = voteRepository.save(vote);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete("/vote/" + vote.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isUnauthorized());
    }
}
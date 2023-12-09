package us.usserver.paragraph.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.author.Author;
import us.usserver.author.AuthorMother;
import us.usserver.author.AuthorRepository;
import us.usserver.chapter.Chapter;
import us.usserver.chapter.ChapterMother;
import us.usserver.chapter.ChapterRepository;
import us.usserver.chapter.chapterEnum.ChapterStatus;
import us.usserver.global.exception.ExceedParagraphLengthException;
import us.usserver.like.paragraph.ParagraphLike;
import us.usserver.like.paragraph.ParagraphLikeRepository;
import us.usserver.member.Member;
import us.usserver.member.MemberRepository;
import us.usserver.member.memberEnum.Gender;
import us.usserver.novel.Novel;
import us.usserver.novel.NovelMother;
import us.usserver.novel.NovelRepository;
import us.usserver.paragraph.Paragraph;
import us.usserver.paragraph.ParagraphMother;
import us.usserver.paragraph.ParagraphRepository;
import us.usserver.paragraph.dto.GetParagraphsRes;
import us.usserver.paragraph.dto.ParagraphInVoting;
import us.usserver.paragraph.dto.PostParagraphReq;
import us.usserver.paragraph.paragraphEnum.ParagraphStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ParagraphServiceV0Test {
    @Autowired
    private ParagraphServiceV0 paragraphServiceV0;
    @Autowired
    private ParagraphRepository paragraphRepository;
    @Autowired
    private ParagraphLikeRepository paragraphLikeRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private NovelRepository novelRepository;
    @Autowired
    private ChapterRepository chapterRepository;

    private Author author;
    private Novel novel;
    private Chapter chapter;
    private Paragraph paragraph1;
    private Paragraph paragraph2;

    @BeforeEach
    public void setUp() {
        author = AuthorMother.generateAuthor();
        setMember(author);
        novel = NovelMother.generateNovel(author);
        chapter = ChapterMother.generateChapter(novel);
        paragraph1 = ParagraphMother.generateParagraph(author, chapter);
        paragraph2 = ParagraphMother.generateParagraph(author, chapter);
        paragraph1.setSequenceForTest(0);
        paragraph2.setSequenceForTest(0);

        novel.getChapters().add(chapter);

        authorRepository.save(author);
        novelRepository.save(novel);
        chapterRepository.save(chapter);
        paragraphRepository.save(paragraph1);
        paragraphRepository.save(paragraph2);
    }

    @Test
    @DisplayName("회차 보기")
    void getParagraphs() {
        // given

        // when
        GetParagraphsRes paragraphs = paragraphServiceV0.getParagraphs(author.getId(), chapter.getId());

        // then
        assertNotNull(paragraphs.getMyParagraph());
        assertNull(paragraphs.getBestParagraph());
        assertThat(paragraphs.getSelectedParagraphs()).isEqualTo(Collections.emptyList());
    }

    @Test
    @DisplayName("어떤 한줄도 등록되지 않은 회차 보기")
    void getInitialChParagraph() {
        // given
        Chapter SecondChapter = ChapterMother.generateChapter(novel);

        // when
        novel.getChapters().add(SecondChapter);
        chapterRepository.save(SecondChapter);
        GetParagraphsRes paragraphs = paragraphServiceV0.getParagraphs(author.getId(), SecondChapter.getId());

        // then
        assertNull(paragraphs.getMyParagraph());
        assertNull(paragraphs.getBestParagraph());
        assertThat(paragraphs.getSelectedParagraphs()).isEqualTo(Collections.emptyList());
    }

    @Test
    @DisplayName("내가 쓴 한줄은 없고, 베스트 한줄은 있는 회차 보기")
    void getParagraph0() {
        // given
        Author author1 = AuthorMother.generateAuthor();
        Author author2 = AuthorMother.generateAuthor();
        Author author3 = AuthorMother.generateAuthor();
        setMember(author1);
        setMember(author2);
        setMember(author3);

        ParagraphLike like1 = ParagraphLike.builder().paragraph(paragraph1).author(author1).build();
        ParagraphLike like2 = ParagraphLike.builder().paragraph(paragraph1).author(author2).build();
        ParagraphLike like3 = ParagraphLike.builder().paragraph(paragraph1).author(author3).build();
        ParagraphLike like4 = ParagraphLike.builder().paragraph(paragraph2).author(author3).build();

        // when
        authorRepository.save(author1);
        authorRepository.save(author2);
        authorRepository.save(author3);
        paragraphLikeRepository.save(like1);
        paragraphLikeRepository.save(like2);
        paragraphLikeRepository.save(like3);
        paragraphLikeRepository.save(like4);

        GetParagraphsRes paragraphs = paragraphServiceV0.getParagraphs(author1.getId(), chapter.getId());

        // then
        assertNull(paragraphs.getMyParagraph());
        assertThat(paragraphs.getSelectedParagraphs()).isEqualTo(Collections.emptyList());
        assertThat(paragraphs.getBestParagraph().getContent()).isEqualTo(paragraph1.getContent());
    }

    @Test
    @DisplayName("투표 중인 한줄들 보기")
    void getInVotingParagraphs() {
        // given

        // when
        List<ParagraphInVoting> paragraphs = paragraphServiceV0.getInVotingParagraphs(chapter.getId());

        // then
        assertThat(paragraphs.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("작성이 완료된 회차 보기")
    void getCompletedChParagraph() {
        // given
        List<Long> idList = Arrays.asList(paragraph1.getId(), paragraph2.getId());

        // when
        chapter.setStatusForTest(ChapterStatus.COMPLETED);
        paragraph1.setParagraphStatus(ParagraphStatus.SELECTED);
        paragraph2.setParagraphStatus(ParagraphStatus.SELECTED);
        chapterRepository.save(chapter);
        paragraphRepository.save(paragraph1);
        paragraphRepository.save(paragraph2);
        GetParagraphsRes paragraphs = paragraphServiceV0.getParagraphs(author.getId(), chapter.getId());

        // then
        assertNull(paragraphs.getMyParagraph());
        assertNull(paragraphs.getBestParagraph());
        assertThat(paragraphs.getSelectedParagraphs().size()).isEqualTo(2);
        paragraphs.getSelectedParagraphs().forEach(p ->
                assertThat(p.getId()).isIn(idList));
    }
    
    @Test
    @DisplayName("한줄 쓰기 등록")
    void postParagraph() {
        // given
        PostParagraphReq req = PostParagraphReq.builder().content("TEST_CONTENT").build();

        // when
        ParagraphInVoting paragraphInVoting = assertDoesNotThrow(() -> paragraphServiceV0.postParagraph(author.getId(), chapter.getId(), req));
        List<Paragraph> paragraphs = paragraphRepository.findAllByChapter(chapter);

        // then
        assertThat(paragraphInVoting.getContent()).isEqualTo("TEST_CONTENT");
        assertThat(paragraphInVoting.getCreatedAt()).isNotNull();
        assertThat(paragraphInVoting.getUpdatedAt()).isNotNull();
        assertThat(paragraphInVoting.getLikeCnt()).isZero();
        assertThat(paragraphInVoting.getAuthorName()).isEqualTo(author.getNickname());
        assertThat(paragraphs.stream().anyMatch(p ->
                p.getContent().equals(req.getContent()))).isTrue();
    }

    @Test
    @DisplayName("300줄 이상 한줄 쓰기 등록")
    void postParagraphException() {
        // given
        String longContent = paragraph1.getContent() + paragraph1.getContent() + paragraph1.getContent();
        PostParagraphReq req = PostParagraphReq.builder().content(longContent).build();

        // when // then
        Assertions.assertThrows(ExceedParagraphLengthException.class,
                () -> paragraphServiceV0.postParagraph(author.getId(), chapter.getId(), req));
    }

    @Test
    @DisplayName("메인 작가가 마음에 드는 한줄 선택하기")
    void selectParagraph1() {
    }

    @Test
    @DisplayName("메인 작가가 아닌 사람이 마음에 드는 한줄 선택하기")
    void selectParagraph2() {

    }

    private void setMember(Author author) {
        Member member = Member.builder().age(1).gender(Gender.MALE).build();
        memberRepository.save(member);
        author.setMember(member);
    }
}
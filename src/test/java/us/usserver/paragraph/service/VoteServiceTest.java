//package us.usserver.paragraph.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import us.usserver.author.AuthorMother;
//import us.usserver.chapter.ChapterMother;
//import us.usserver.domain.author.entity.Author;
//import us.usserver.domain.author.repository.AuthorRepository;
//import us.usserver.domain.chapter.entity.Chapter;
//import us.usserver.domain.chapter.repository.ChapterRepository;
//import us.usserver.domain.member.entity.Member;
//import us.usserver.domain.member.repository.MemberRepository;
//import us.usserver.domain.novel.entity.Novel;
//import us.usserver.domain.novel.repository.NovelRepository;
//import us.usserver.domain.paragraph.entity.Paragraph;
//import us.usserver.domain.paragraph.entity.Vote;
//import us.usserver.domain.paragraph.repository.ParagraphRepository;
//import us.usserver.domain.paragraph.repository.VoteRepository;
//import us.usserver.domain.paragraph.service.VoteService;
//import us.usserver.global.response.exception.*;
//import us.usserver.member.MemberMother;
//import us.usserver.novel.NovelMother;
//import us.usserver.paragraph.ParagraphMother;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//@Rollback
//@SpringBootTest
//class VoteServiceTest {
//    @Autowired
//    private VoteService voteService;
//
//    @Autowired
//    private VoteRepository voteJpaRepository;
//    @Autowired
//    private AuthorRepository authorRepository;
//    @Autowired
//    private NovelRepository novelRepository;
//    @Autowired
//    private ChapterRepository chapterRepository;
//    @Autowired
//    private ParagraphRepository paragraphRepository;
//    @Autowired
//    private MemberRepository memberRepository;
//
//    private Author author;
//    private Novel novel;
//    private Chapter chapter;
//    private Paragraph paragraph;
//
//    @BeforeEach
//    void setUp() {
//        author = AuthorMother.generateAuthor();
//        setMember(author);
//        novel = NovelMother.generateNovel(author);
//        chapter = ChapterMother.generateChapter(novel);
//        paragraph = ParagraphMother.generateParagraph(author, chapter);
//        paragraph.setSequenceForTest(0);
//
//        novel.getChapters().add(chapter);
//
//        authorRepository.save(author);
//        novelRepository.save(novel);
//        chapterRepository.save(chapter);
//        paragraphRepository.save(paragraph);
//    }
//
//    @Test
//    @DisplayName("마음에 드는 한줄에 투표하기")
//    void voting() {
//        // given
//
//        // when
//        voteService.voting(new Member(), author.getId()); // 이부분 수정하긴 해야함
//        List<Vote> allByAuthor = voteJpaRepository.findAllByAuthor(author);
//
//        // then
//        assertThat(allByAuthor.size()).isEqualTo(1);
//    }
//
//    @Test
//    @DisplayName("마음에 드는 한줄에 투표하기 - 중복 투표 방지")
//    void voting2() {
//        // given
//
//        // when
//        voteService.voting(paragraph.getId(), author.getId());
//        BaseException baseException = assertThrows(BaseException.class,
//                () -> voteService.voting(paragraph.getId(), author.getId()));
//
//        // then
//        assertThat(baseException.getMessage()).isEqualTo(ExceptionMessage.VOTE_ONLY_ONE_PARAGRAPH);
//
//    }
//
//    @Test
//    @DisplayName("투표 취소")
//    void unvoting1() {
//        // given
//        Vote vote = Vote.builder().author(author).paragraph(paragraph).build();
//
//        // when
//        voteJpaRepository.save(vote);
//        voteService.unvoting(vote.getId(), author.getId());
//        List<Vote> allByAuthor = voteJpaRepository.findAllByAuthor(author);
//
//        // then
//        assertThat(allByAuthor.size()).isZero();
//    }
//
//    @Test
//    @DisplayName("투표 취소 - 투표한 본인이 아닌 경우")
//    void unvoting2() {
//        // given
//        Author newAuthor = AuthorMother.generateAuthor();
//        setMember(newAuthor);
//
//        Vote vote = Vote.builder().author(author).paragraph(paragraph).build();
//
//        // when
//        authorRepository.save(newAuthor);
//        voteJpaRepository.save(vote);
//        BaseException baseException = assertThrows(BaseException.class,
//                () -> voteService.unvoting(vote.getId(), newAuthor.getId()));
//
//        // then
//        assertThat(baseException.getMessage()).isEqualTo(ExceptionMessage.AUTHOR_NOT_AUTHORIZED);
//
//    }
//
//    @Test
//    @DisplayName("투표 취소 - 중복해서 투표 취소가 요청 될 때")
//    void unvoting3() {
//        // given
//        Vote vote = Vote.builder().author(author).paragraph(paragraph).build();
//
//        // when
//        voteJpaRepository.save(vote);
//        assertDoesNotThrow(
//                () -> voteService.unvoting(vote.getId(), author.getId()));
//        BaseException baseException = assertThrows(BaseException.class,
//                () -> voteService.unvoting(vote.getId(), author.getId()));
//
//        // then
//        assertThat(baseException.getMessage()).isEqualTo(ExceptionMessage.VOTE_NOT_FOUND);
//
//    }
//
//    private void setMember(Author author) {
//        Member member = MemberMother.generateMember();
//        memberRepository.save(member);
//        author.setMember(member);
//    }
//}
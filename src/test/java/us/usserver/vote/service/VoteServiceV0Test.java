package us.usserver.vote.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import us.usserver.author.Author;
import us.usserver.author.AuthorMother;
import us.usserver.author.AuthorRepository;
import us.usserver.chapter.Chapter;
import us.usserver.chapter.ChapterMother;
import us.usserver.chapter.ChapterRepository;
import us.usserver.member.Member;
import us.usserver.member.MemberRepository;
import us.usserver.member.memberEnum.Gender;
import us.usserver.novel.Novel;
import us.usserver.novel.NovelMother;
import us.usserver.novel.NovelRepository;
import us.usserver.paragraph.Paragraph;
import us.usserver.paragraph.ParagraphMother;
import us.usserver.paragraph.ParagraphRepository;
import us.usserver.vote.VoteRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VoteServiceV0Test {
    @Autowired
    private VoteServiceV0 voteServiceV0;

    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private NovelRepository novelRepository;
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private ParagraphRepository paragraphRepository;
    @Autowired
    private MemberRepository memberRepository;

    private Author author;
    private Novel novel;
    private Chapter chapter;
    private Paragraph paragraph1;

    @BeforeEach
    void setUp() {
        author = AuthorMother.generateAuthor();
        setMember(author);
        novel = NovelMother.generateNovel(author);
        chapter = ChapterMother.generateChapter(novel);
        paragraph1 = ParagraphMother.generateParagraph(author, chapter);
        paragraph1.setSequenceForTest(0);

        novel.getChapters().add(chapter);

        authorRepository.save(author);
        novelRepository.save(novel);
        chapterRepository.save(chapter);
        paragraphRepository.save(paragraph1);
    }

    @Test
    @DisplayName("마음에 드는 한줄에 투표하기")
    void voting() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("마음에 드는 한줄에 투표하기 - 중복 투표 방지")
    void voting() {
        // given

        // when

        // then
    }

    @Test
    void unvoting() {

    }

    private void setMember(Author author) {
        Member member = Member.builder().age(1).gender(Gender.MALE).build();
        memberRepository.save(member);
        author.setMember(member);
    }
}
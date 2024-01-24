package us.usserver.note.service;

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
import us.usserver.like.paragraph.ParagraphLike;
import us.usserver.like.paragraph.ParagraphLikeRepository;
import us.usserver.member.Member;
import us.usserver.member.MemberRepository;
import us.usserver.member.memberEnum.Gender;
import us.usserver.note.dto.ParagraphPreview;
import us.usserver.novel.Novel;
import us.usserver.novel.NovelMother;
import us.usserver.novel.repository.NovelJpaRepository;
import us.usserver.paragraph.Paragraph;
import us.usserver.paragraph.ParagraphMother;
import us.usserver.paragraph.ParagraphRepository;
import us.usserver.vote.Vote;
import us.usserver.vote.VoteRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class NoteServiceV0Test {
    @Autowired
    private NoteServiceV0 noteServiceV0;

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private NovelJpaRepository novelJpaRepository;
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private ParagraphRepository paragraphRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private ParagraphLikeRepository paragraphLikeRepository;

    Author author;
    Novel novel;
    Chapter chapter;
    Paragraph paragraph;

    @BeforeEach
    void setUp() {
        author = AuthorMother.generateAuthor();
        setMember(author);
        novel = NovelMother.generateNovel(author);
        chapter = ChapterMother.generateChapter(novel);
        paragraph = ParagraphMother.generateParagraph(author, chapter);

        chapter.getParagraphs().add(paragraph);
        novel.getChapters().add(chapter);

        authorRepository.save(author);
        novelJpaRepository.save(novel);
        chapterRepository.save(chapter);
        paragraphRepository.save(paragraph);
    }

    @Test
    @DisplayName("내가 쓴 한줄 찾기")
    void wroteParagraphs1() {
        // given
        Paragraph newParagraph = ParagraphMother.generateParagraph(author, chapter);

        // when
        chapter.getParagraphs().add(newParagraph);
        paragraphRepository.save(newParagraph);
        List<ParagraphPreview> paragraphPreviews = noteServiceV0.wroteParagraphs(author.getId());

        // then
        assertThat(paragraphPreviews.size()).isEqualTo(2);
        for (ParagraphPreview paragraphPreview : paragraphPreviews) {
            assertThat(paragraphPreview.getAuthorId()).isEqualTo(author.getId());
            assertThat(paragraphPreview.getChapterTitle()).isEqualTo(chapter.getTitle());
            assertThat(paragraphPreview.getNovelTitle()).isEqualTo(novel.getTitle());
        }
    }

    @Test
    @DisplayName("남이 쓴 한줄은 가져오지 않는 테스트")
    void wroteParagraphs2() {
        // given
        Author newAuthor = AuthorMother.generateAuthor();
        setMember(newAuthor);
        Paragraph newParagraph = ParagraphMother.generateParagraph(newAuthor, chapter);

        // when
        authorRepository.save(newAuthor);
        chapter.getParagraphs().add(newParagraph);
        paragraphRepository.save(newParagraph);
        List<ParagraphPreview> paragraphPreviews = noteServiceV0.wroteParagraphs(author.getId());

        // then
        assertThat(paragraphPreviews.size()).isOne();
        for (ParagraphPreview paragraphPreview : paragraphPreviews) {
            assertThat(paragraphPreview.getAuthorId()).isNotEqualTo(newAuthor.getId());
        }
    }


    @Test
    @DisplayName("내가 투표한 한줄 찾기")
    void votedParagraphs1() {
        // given
        Vote vote = Vote.builder().author(author).paragraph(paragraph).build();

        // when
        voteRepository.save(vote);
        List<ParagraphPreview> paragraphPreviews = noteServiceV0.votedParagraphs(author.getId());

        // then
        assertThat(paragraphPreviews.size()).isOne();
    }

    @Test
    @DisplayName("남이 투표한 한줄은 가져오지 않는 테스트")
    void votedParagraphs2() {
        // given
        Author newAuthor = AuthorMother.generateAuthor();
        setMember(newAuthor);
        Vote vote = Vote.builder().author(newAuthor).paragraph(paragraph).build();

        // when
        authorRepository.save(newAuthor);
        voteRepository.save(vote);
        List<ParagraphPreview> paragraphPreviews = noteServiceV0.votedParagraphs(author.getId());

        // then
        assertThat(paragraphPreviews.size()).isZero();
    }

    @Test
    @DisplayName("좋아요 누른 한줄 찾기")
    void likedParagraphs1() {
        // given
        ParagraphLike paragraphLike = ParagraphLike.builder().paragraph(paragraph).author(author).build();

        // when`
        paragraphLikeRepository.save(paragraphLike);
        List<ParagraphPreview> paragraphPreviews = noteServiceV0.likedParagraphs(author.getId());

        // then
        assertThat(paragraphPreviews.size()).isOne();
        assertThat(paragraphPreviews.get(0).getParagraphContent()).isEqualTo(paragraph.getContent());
    }

    @Test
    @DisplayName("남이 좋아요 누른 한줄 가져오지 않는 테스트")
    void likedParagraphs2() {
        // given
        ParagraphLike paragraphLike = ParagraphLike.builder().paragraph(paragraph).author(author).build();

        // when
        paragraphLikeRepository.save(paragraphLike);
        List<ParagraphPreview> paragraphPreviews = noteServiceV0.likedParagraphs(author.getId());

        // then
        assertThat(paragraphPreviews.size()).isOne();
        assertThat(paragraphPreviews.get(0).getParagraphContent()).isEqualTo(paragraph.getContent());

    }

    private void setMember(Author author) {
        Member member = Member.builder().age(1).gender(Gender.MALE).build();
        memberRepository.save(member);
        author.setMember(member);
    }
}
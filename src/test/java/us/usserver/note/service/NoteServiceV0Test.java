package us.usserver.note.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import us.usserver.domain.member.entity.Author;
import us.usserver.author.AuthorMother;
import us.usserver.domain.member.repository.AuthorRepository;
import us.usserver.domain.chapter.entity.Chapter;
import us.usserver.chapter.ChapterMother;
import us.usserver.domain.chapter.repository.ChapterRepository;
import us.usserver.domain.paragraph.entity.ParagraphLike;
import us.usserver.domain.paragraph.repository.ParagraphLikeRepository;
import us.usserver.domain.member.entity.Member;
import us.usserver.domain.note.service.NoteServiceV0;
import us.usserver.member.MemberMother;
import us.usserver.domain.member.repository.MemberRepository;
import us.usserver.domain.note.dto.ParagraphPreview;
import us.usserver.domain.novel.Novel;
import us.usserver.novel.NovelMother;
import us.usserver.domain.novel.repository.NovelRepository;
import us.usserver.domain.paragraph.entity.Paragraph;
import us.usserver.paragraph.ParagraphMother;
import us.usserver.domain.paragraph.repository.ParagraphRepository;
import us.usserver.domain.paragraph.entity.Vote;
import us.usserver.domain.paragraph.repository.VoteJpaRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Rollback
@SpringBootTest
class NoteServiceV0Test {
    @Autowired
    private NoteServiceV0 noteServiceV0;

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
    private VoteJpaRepository voteJpaRepository;
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
        novelRepository.save(novel);
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
        voteJpaRepository.save(vote);
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
        voteJpaRepository.save(vote);
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
        Member member = MemberMother.generateMember();
        memberRepository.save(member);
        author.setMember(member);
    }
}
package us.usserver.author.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import us.usserver.author.AuthorMother;
import us.usserver.chapter.ChapterMother;
import us.usserver.domain.author.dto.ParagraphPreview;
import us.usserver.domain.author.dto.res.GetParagraphNote;
import us.usserver.domain.author.entity.Author;
import us.usserver.domain.author.repository.AuthorRepository;
import us.usserver.domain.author.service.NoteService;
import us.usserver.domain.chapter.entity.Chapter;
import us.usserver.domain.chapter.repository.ChapterRepository;
import us.usserver.domain.member.entity.Member;
import us.usserver.domain.member.repository.MemberRepository;
import us.usserver.domain.novel.entity.Novel;
import us.usserver.domain.novel.repository.NovelRepository;
import us.usserver.domain.paragraph.entity.Paragraph;
import us.usserver.domain.paragraph.entity.ParagraphLike;
import us.usserver.domain.paragraph.entity.Vote;
import us.usserver.domain.paragraph.repository.ParagraphLikeRepository;
import us.usserver.domain.paragraph.repository.ParagraphRepository;
import us.usserver.domain.paragraph.repository.VoteRepository;
import us.usserver.member.MemberMother;
import us.usserver.novel.NovelMother;
import us.usserver.paragraph.ParagraphMother;

import static org.assertj.core.api.Assertions.assertThat;

@Rollback
@SpringBootTest
class NoteServiceTest {
    @Autowired
    private NoteService noteService;

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
    private VoteRepository voteJpaRepository;
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
        GetParagraphNote getParagraphNote = noteService.wroteParagraphs(author.getId());

        // then
        assertThat(getParagraphNote.getParagraphPreviews().size()).isEqualTo(2);
        for (ParagraphPreview paragraphPreview : getParagraphNote.getParagraphPreviews()) {
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
        GetParagraphNote getParagraphNote = noteService.wroteParagraphs(author.getId());

        // then
        assertThat(getParagraphNote.getParagraphPreviews().size()).isOne();
        for (ParagraphPreview paragraphPreview : getParagraphNote.getParagraphPreviews()) {
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
        GetParagraphNote getParagraphNote = noteService.votedParagraphs(author.getId());

        // then
        assertThat(getParagraphNote.getParagraphPreviews().size()).isOne();
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
        GetParagraphNote getParagraphNote = noteService.votedParagraphs(author.getId());

        // then
        assertThat(getParagraphNote.getParagraphPreviews().size()).isZero();
    }

    @Test
    @DisplayName("좋아요 누른 한줄 찾기")
    void likedParagraphs1() {
        // given
        ParagraphLike paragraphLike = ParagraphLike.builder().paragraph(paragraph).author(author).build();

        // when`
        paragraphLikeRepository.save(paragraphLike);
        GetParagraphNote getParagraphNote = noteService.likedParagraphs(author.getId());

        // then
        assertThat(getParagraphNote.getParagraphPreviews().size()).isOne();
        assertThat(getParagraphNote.getParagraphPreviews().get(0).getParagraphContent()).isEqualTo(paragraph.getContent());
    }

    @Test
    @DisplayName("남이 좋아요 누른 한줄 가져오지 않는 테스트")
    void likedParagraphs2() {
        // given
        ParagraphLike paragraphLike = ParagraphLike.builder().paragraph(paragraph).author(author).build();

        // when
        paragraphLikeRepository.save(paragraphLike);
        GetParagraphNote getParagraphNote = noteService.likedParagraphs(author.getId());

        // then
        assertThat(getParagraphNote.getParagraphPreviews().size()).isOne();
        assertThat(getParagraphNote.getParagraphPreviews().get(0).getParagraphContent()).isEqualTo(paragraph.getContent());

    }

    private void setMember(Author author) {
        Member member = MemberMother.generateMember();
        memberRepository.save(member);
        author.setMember(member);
    }
}
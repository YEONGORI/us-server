package us.usserver.like.paragraph.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import us.usserver.author.Author;
import us.usserver.author.AuthorMother;
import us.usserver.author.AuthorRepository;
import us.usserver.chapter.Chapter;
import us.usserver.chapter.ChapterMother;
import us.usserver.chapter.ChapterRepository;
import us.usserver.global.exception.DuplicatedLikeException;
import us.usserver.like.paragraph.ParagraphLike;
import us.usserver.like.paragraph.ParagraphLikeRepository;
import us.usserver.member.Member;
import us.usserver.member.MemberMother;
import us.usserver.member.MemberRepository;
import us.usserver.novel.Novel;
import us.usserver.novel.NovelMother;
import us.usserver.novel.repository.NovelJpaRepository;
import us.usserver.paragraph.Paragraph;
import us.usserver.paragraph.ParagraphMother;
import us.usserver.paragraph.ParagraphRepository;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@Rollback
@SpringBootTest
class ParagraphLikeServiceV0Test {
    @Autowired
    private ParagraphLikeServiceV0 paragraphLikeServiceV0;

    @Autowired
    private ParagraphLikeRepository paragraphLikeRepository;
    @Autowired
    private NovelJpaRepository novelJpaRepository;
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private ParagraphRepository paragraphRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private MemberRepository memberRepository;

    private Novel novel;
    private Author author;
    private Chapter chapter;
    private Paragraph paragraph;

    @BeforeEach
    void setUp() {
        author = AuthorMother.generateAuthor();
        setMember(author);
        authorRepository.save(author);

        novel = NovelMother.generateNovel(author);
        chapter = ChapterMother.generateChapter(novel);
        paragraph = ParagraphMother.generateParagraph(author, chapter);

        novel.getChapters().add(chapter);
        chapter.getParagraphs().add(paragraph);

        novelJpaRepository.save(novel);
        chapterRepository.save(chapter);
        paragraphRepository.save(paragraph);
    }

    @Test
    @DisplayName("한줄 좋아요")
    void setParagraphLike1() {
        // given
        List<ParagraphLike> beforeParagraphLikes = paragraphLikeRepository.findAllByParagraph(paragraph);

        // when
        paragraphLikeServiceV0.setParagraphLike(paragraph.getId(), author.getId());
        List<ParagraphLike> afterparagraphLikes = paragraphLikeRepository.findAllByParagraph(paragraph);

        // then
        assertThat(beforeParagraphLikes).isEqualTo(Collections.emptyList());
        assertThat(afterparagraphLikes).isNotEqualTo(Collections.emptyList());
    }

    @Test
    @DisplayName("한줄 좋아요 중복 불가")
    void setParagraphLike2() {
        // given

        // when
        assertDoesNotThrow(
                () -> paragraphLikeServiceV0.setParagraphLike(paragraph.getId(), author.getId()));

        // then
        assertThrows(DuplicatedLikeException.class,
                () -> paragraphLikeServiceV0.setParagraphLike(paragraph.getId(), author.getId()));
    }

    @Test
    @DisplayName("한줄 좋아요 삭제")
    void deleteParagraphLike2() {
        // given
        ParagraphLike paragraphLike = ParagraphLike.builder().paragraph(paragraph).author(author).build();

        // when
        paragraphLikeRepository.save(paragraphLike);
        assertDoesNotThrow(
                () -> paragraphLikeServiceV0.deleteParagraphLike(paragraph.getId(), author.getId()));
        List<ParagraphLike> paragraphLikes = paragraphLikeRepository.findAllByAuthor(author);

        // then
        assertThat(paragraphLikes.size()).isZero();
    }


    private void setMember(Author author) {
        Member member = MemberMother.generateMember();
        memberRepository.save(member);
        author.setMember(member);
    }
}
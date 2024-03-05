package us.usserver.paragraph.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import us.usserver.author.AuthorMother;
import us.usserver.chapter.ChapterMother;
import us.usserver.domain.author.entity.Author;
import us.usserver.domain.author.repository.AuthorRepository;
import us.usserver.domain.chapter.entity.Chapter;
import us.usserver.domain.chapter.repository.ChapterRepository;
import us.usserver.domain.member.entity.Member;
import us.usserver.domain.member.repository.MemberRepository;
import us.usserver.domain.novel.entity.Novel;
import us.usserver.domain.novel.repository.NovelRepository;
import us.usserver.domain.paragraph.entity.Paragraph;
import us.usserver.domain.paragraph.entity.ParagraphLike;
import us.usserver.domain.paragraph.repository.ParagraphLikeRepository;
import us.usserver.domain.paragraph.repository.ParagraphRepository;
import us.usserver.domain.paragraph.service.ParagraphLikeService;
import us.usserver.global.response.exception.BaseException;
import us.usserver.global.response.exception.DuplicatedLikeException;
import us.usserver.global.response.exception.ExceptionMessage;
import us.usserver.member.MemberMother;
import us.usserver.novel.NovelMother;
import us.usserver.paragraph.ParagraphMother;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


@Rollback
@SpringBootTest
class ParagraphLikeServiceTest {
    @Autowired
    private ParagraphLikeService paragraphLikeService;

    @Autowired
    private ParagraphLikeRepository paragraphLikeRepository;
    @Autowired
    private NovelRepository novelRepository;
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

        novelRepository.save(novel);
        chapterRepository.save(chapter);
        paragraphRepository.save(paragraph);
    }

    @Test
    @DisplayName("한줄 좋아요")
    void setParagraphLike1() {
        // given
        List<ParagraphLike> beforeParagraphLikes = paragraphLikeRepository.findAllByParagraph(paragraph);

        // when
        paragraphLikeService.setParagraphLike(paragraph.getId(), author.getId());
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
                () -> paragraphLikeService.setParagraphLike(paragraph.getId(), author.getId()));
        BaseException baseException = assertThrows(BaseException.class,
                () -> paragraphLikeService.setParagraphLike(paragraph.getId(), author.getId()));

        // then
        assertThat(baseException.getMessage()).isEqualTo(ExceptionMessage.LIKE_DUPLICATED);

    }

    @Test
    @DisplayName("한줄 좋아요 삭제")
    void deleteParagraphLike2() {
        // given
        ParagraphLike paragraphLike = ParagraphLike.builder().paragraph(paragraph).author(author).build();

        // when
        paragraphLikeRepository.save(paragraphLike);
        assertDoesNotThrow(
                () -> paragraphLikeService.deleteParagraphLike(paragraph.getId(), author.getId()));
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
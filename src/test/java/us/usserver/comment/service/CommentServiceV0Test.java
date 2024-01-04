package us.usserver.comment.service;

import org.assertj.core.api.Assertions;
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
import us.usserver.comment.Comment;
import us.usserver.comment.CommentMother;
import us.usserver.comment.CommentRepository;
import us.usserver.comment.dto.CommentInfo;
import us.usserver.global.exception.NovelNotFoundException;
import us.usserver.member.Member;
import us.usserver.member.MemberRepository;
import us.usserver.member.memberEnum.Gender;
import us.usserver.novel.Novel;
import us.usserver.novel.NovelMother;
import us.usserver.novel.NovelRepository;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceV0Test {
    @Autowired
    private CommentServiceV0 commentServiceV0;

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private NovelRepository novelRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ChapterRepository chapterRepository;

    private Novel novel;
    private Author author;
    private Chapter chapter;

    @BeforeEach
    void setUp() {
        author = AuthorMother.generateAuthor();
        setMember(author);
        novel = NovelMother.generateNovel(author);
        chapter = ChapterMother.generateChapter(novel);
        novel.getChapters().add(chapter);

        authorRepository.save(author);
        novelRepository.save(novel);
        chapterRepository.save(chapter);
    }

    @Test
    @DisplayName("소설 댓글 불러오기 - 0개")
    void getCommentsOfNovel() {
        // given

        // when
        List<CommentInfo> comments = commentServiceV0.getCommentsOfNovel(novel.getId());

        // then
        assertThat(comments).isEqualTo(Collections.emptyList());
    }

    @Test
    @DisplayName("소설 댓글 불러오기 - n개")
    void getCommentsOfNovel1() {
        // given
        Comment comment1 = CommentMother.generateComment(author, novel, chapter);
        Comment comment2 = CommentMother.generateComment(author, novel, chapter);
        Comment comment3 = CommentMother.generateComment(author, novel, chapter);

        // when
        novel.getComments().add(comment1);
        novel.getComments().add(comment2);
        novel.getComments().add(comment3);
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        List<CommentInfo> comments = commentServiceV0.getCommentsOfNovel(novel.getId());

        // then
        assertThat(comments.size()).isEqualTo(3);
        assertThat(comments.get(0).getLocation()).isEqualTo(novel.getTitle());
        assertThat(comments.get(1).getLocation()).isEqualTo(novel.getTitle());
        assertThat(comments.get(2).getLocation()).isEqualTo(novel.getTitle());
    }

    @Test
    @DisplayName("소설 댓글 불러오기 - 삭제된 소설")
    void getCommentsOfNovel2() {
        // given
        Novel newNovel = NovelMother.generateNovel(author);
        Comment newComment = CommentMother.generateComment(author, newNovel, null);
        newNovel.getComments().add(newComment);

        // when
        novelRepository.save(newNovel);
        commentRepository.save(newComment);
        List<CommentInfo> beforeComments = commentServiceV0.getCommentsOfNovel(newNovel.getId());
        novelRepository.delete(newNovel);
        List<Comment> afterComments = commentRepository.findAllByAuthor(author);
        assertThrows(NovelNotFoundException.class,
                () -> commentServiceV0.getCommentsOfNovel(newNovel.getId()));

        // then
        assertThat(beforeComments.get(0).getLocation()).isEqualTo(newNovel.getTitle());
        for (Comment comment : afterComments) {
            assertThat(comment.getNovel().getTitle()).isNotEqualTo(newNovel.getTitle());
        }
    }

    @Test
    void getCommentsOfChapter() {
        // given

        // when

        // then
    }

    @Test
    void writeCommentOnNovel() {
        // given

        // when

        // then
    }

    @Test
    void writeCommentOnChapter() {
        // given

        // when

        // then
    }

    @Test
    void getCommentsByAuthor() {
        // given

        // when

        // then
    }

    private void setMember(Author author) {
        Member member = Member.builder().age(1).gender(Gender.MALE).build();
        memberRepository.save(member);
        author.setMember(member);
    }
}
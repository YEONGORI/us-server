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
import us.usserver.comment.dto.CommentInfo;
import us.usserver.member.Member;
import us.usserver.member.MemberRepository;
import us.usserver.member.memberEnum.Gender;
import us.usserver.novel.Novel;
import us.usserver.novel.NovelMother;
import us.usserver.novel.NovelRepository;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceV0Test {
    @Autowired
    private CommentServiceV0 commentServiceV0;

    @Autowired
    private NovelRepository novelRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private MemberRepository memberRepository;

    private Novel novel;
    private Author author;

    @BeforeEach
    void setUp() {
        author = AuthorMother.generateAuthor();
        setMember(author);
        novel = NovelMother.generateNovel(author);

        authorRepository.save(author);
        novelRepository.save(novel);
    }

    @Test
    @DisplayName("소설 댓글 불러오기 - 0개")
    void getCommentsOfNovel() {
        // given

        // when
        List<CommentInfo> comments = commentServiceV0.getCommentsOfNovel(novel.getId());

        // then
        Assertions.assertThat(comments).isEqualTo(Collections.emptyList());
    }

    @Test
    @DisplayName("소설 댓글 불러오기 - n개")
    void getCommentsOfNovel1() {
        // given


        // when

        // then
    }

    @Test
    @DisplayName("소설 댓글 불러오기 - 삭제된 소설")
    void getCommentsOfNovel2() {
        // given

        // when

        // then
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
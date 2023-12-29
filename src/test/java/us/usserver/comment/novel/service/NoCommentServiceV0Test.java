package us.usserver.comment.novel.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import us.usserver.author.Author;
import us.usserver.author.AuthorMother;
import us.usserver.author.AuthorRepository;
import us.usserver.comment.novel.dto.CommentsInNovelRes;
import us.usserver.comment.novel.dto.PostCommentReq;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.global.exception.NovelNotFoundException;
import us.usserver.like.novel.service.NovelLikeServiceV0;
import us.usserver.member.Member;
import us.usserver.member.MemberRepository;
import us.usserver.member.memberEnum.Gender;
import us.usserver.novel.Novel;
import us.usserver.novel.NovelMother;
import us.usserver.novel.NovelRepository;
import us.usserver.novel.novelEnum.AgeRating;
import us.usserver.novel.novelEnum.Genre;
import us.usserver.novel.novelEnum.Hashtag;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NoCommentServiceV0Test {
    @Autowired
    private NoCommentServiceV0 noCommentServiceV0;
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
    @DisplayName("소설 모든 댓글 불러오기")
    void getCommentsInNovel() {
        List<CommentsInNovelRes> commentsInNovelRes = assertDoesNotThrow(
                () -> noCommentServiceV0.getCommentsInNovel(novel.getId()));

        assertThat(commentsInNovelRes).isNotNull();
    }

    @Test
    @DisplayName("존재 하지 않는 소설 댓글 불러오기")
    void getCommentsInNotExistNovel() {
        // given
        Long randomNovelId = 99L;

        // when then
        assertThrows(NovelNotFoundException.class,
                () -> noCommentServiceV0.getCommentsInNovel(randomNovelId));
    }

    @Test
    @DisplayName("소설 댓글 작성하기")
    void postCommentInNovel() {
        // given
        PostCommentReq testComment = PostCommentReq.builder()
                .content("TEST COMMENT")
                .build();

        // when
        List<CommentsInNovelRes> commentsInNovel = assertDoesNotThrow(() ->
                noCommentServiceV0.postCommentInNovel(
                        novel.getId(), author.getId(), testComment)
        );

        // then
        assertThat(commentsInNovel.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("존재 하지 않는 소설 댓글 작성하기")
    void postCommentInNotExistNovel() {
        // given
        Long randomNovelId = 99L;
        PostCommentReq req = PostCommentReq.builder().content("").build();

        // when then
        assertThrows(NovelNotFoundException.class,
                () -> noCommentServiceV0.postCommentInNovel(
                        randomNovelId,
                        author.getId(),
                        req)
        );
    }

    @Test
    @DisplayName("존재 하지 않는 작가의 댓글 작성")
    void postCommentInNotExistAuthor() {
        // given
        Long randomAuthorId = 99L;
        PostCommentReq req = PostCommentReq.builder().content("").build();

        // when then
        assertThrows(AuthorNotFoundException.class,
                () -> noCommentServiceV0.postCommentInNovel(
                        novel.getId(),
                        randomAuthorId,
                        req));
    }

    private void setMember(Author author) {
        Member member = Member.builder().age(1).gender(Gender.MALE).build();
        memberRepository.save(member);
        author.setMember(member);
    }
}
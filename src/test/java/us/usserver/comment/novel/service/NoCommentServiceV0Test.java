package us.usserver.comment.novel.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import us.usserver.author.Author;
import us.usserver.author.AuthorRepository;
import us.usserver.comment.novel.dto.CommentsInNovelRes;
import us.usserver.comment.novel.dto.PostCommentReq;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.global.exception.NovelNotFoundException;
import us.usserver.like.novel.service.NovelLikeServiceV0;
import us.usserver.novel.Novel;
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

    private Novel novel;
    private Author author;

    @BeforeEach
    void setUp() {
        Set<Hashtag> hashtags = new HashSet<>();
        hashtags.add(Hashtag.HASHTAG1);
        hashtags.add(Hashtag.HASHTAG2);
        hashtags.add(Hashtag.MONCHKIN);

        novel = Novel.builder()
                .id(1L)
                .title("TITLE")
                .thumbnail("THUMBNAIL")
                .synopsis("SYNOPSIS")
                .authorDescription("AUTHOR_DESCRIPTION")
                .hashtags(hashtags)
                .genre(Genre.FANTASY)
                .ageRating(AgeRating.GENERAL)
                .build();

        author = Author.builder()
                .id(1L)
                .nickname("NICKNAME")
                .introduction("INTRODUCTION")
                .profileImg("PROFILE_IMG")
                .build();

        novelRepository.save(novel);
        authorRepository.save(author);
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
        assertThrows(NovelNotFoundException.class,
                () -> noCommentServiceV0.getCommentsInNovel(11L));
    }

    @Test
    @DisplayName("소설 댓글 작성하기")
    void postCommentInNovel() {
        PostCommentReq testComment = PostCommentReq.builder()
                .content("TEST COMMENT")
                .build();
        List<CommentsInNovelRes> commentsInNovel = assertDoesNotThrow(() ->
                noCommentServiceV0.postCommentInNovel(
                        novel.getId(),
                        author.getId(),
                        testComment
                )
        );

        assertThat(commentsInNovel.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("존재 하지 않는 소설 댓글 작성하기")
    void postCommentInNotExistNovel() {
        assertThrows(NovelNotFoundException.class,
                () -> noCommentServiceV0.postCommentInNovel(
                        11L,
                        1L,
                        PostCommentReq.builder().content("").build())
        );
    }

    @Test
    @DisplayName("존재 하지 않는 작가의 댓글 작성")
    void postCommentInNotExistAuthor() {
        assertThrows(AuthorNotFoundException.class,
                () -> noCommentServiceV0.postCommentInNovel(
                        1L,
                        11L,
                        PostCommentReq.builder().content("").build()
                ));
    }
}
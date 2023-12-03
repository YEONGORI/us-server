package us.usserver.like.novel.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import us.usserver.author.Author;
import us.usserver.author.AuthorRepository;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.global.exception.NovelNotFoundException;
import us.usserver.like.novel.NovelLike;
import us.usserver.like.novel.NovelLikeRepository;
import us.usserver.like.novel.NovelLikeService;
import us.usserver.novel.Novel;
import us.usserver.novel.NovelRepository;
import us.usserver.novel.novelEnum.AgeRating;
import us.usserver.novel.novelEnum.Genre;
import us.usserver.novel.novelEnum.Hashtag;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class NovelLikeServiceV0Test {
    @Autowired
    private NovelRepository novelRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private NovelLikeServiceV0 novelLikeServiceV0;

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
                .hashtag(hashtags)
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
    @DisplayName("소설 좋아요 테스트")
    void setNovelLike() {
        Assertions.assertDoesNotThrow(
                () -> novelLikeServiceV0.setNovelLike(1L, 1L));
    }

    @Test
    @DisplayName("존재 하지 않는 소설에 대한 좋아요 테스트")
    void setNovelLikeToNotExistNovel() {
        Assertions.assertThrows(NovelNotFoundException.class,
                () -> novelLikeServiceV0.setNovelLike(2L, 1L));
    }

    @Test
    @DisplayName("존재하지 않는 유저에 대한 좋아요 테스트")
    void setNoveLikeToNotExistAuthor() {
        Assertions.assertThrows(AuthorNotFoundException.class,
                () -> novelLikeServiceV0.setNovelLike(1L, 2L));
    }
}

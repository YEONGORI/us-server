package us.usserver.like.novel.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import us.usserver.domain.member.entity.Author;
import us.usserver.author.AuthorMother;
import us.usserver.domain.member.repository.AuthorRepository;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.global.exception.DuplicatedLikeException;
import us.usserver.global.exception.NovelNotFoundException;
import us.usserver.domain.like.novel.NovelLike;
import us.usserver.domain.like.novel.repository.NovelLikeJpaRepository;
import us.usserver.domain.like.novel.service.NovelLikeServiceV0;
import us.usserver.domain.member.entity.Member;
import us.usserver.member.MemberMother;
import us.usserver.domain.member.repository.MemberRepository;
import us.usserver.domain.novel.Novel;
import us.usserver.novel.NovelMother;
import us.usserver.domain.novel.repository.NovelRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@Rollback
@SpringBootTest
class NovelLikeServiceV0Test {
    @Autowired
    private NovelRepository novelRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private NovelLikeJpaRepository novelLikeJpaRepository;

    @Autowired
    private NovelLikeServiceV0 novelLikeServiceV0;

    private Novel novel;
    private Author author;

    @BeforeEach
    void setUp() {
        author = AuthorMother.generateAuthor();
        setMember(author);
        authorRepository.save(author);

        novel = NovelMother.generateNovel(author);
        novelRepository.save(novel);
    }

    @Test
    @DisplayName("소설 좋아요 테스트")
    void setNovelLike() {
        // given
        List<NovelLike> beforeNovelLike = novelLikeJpaRepository.findAllByNovel(novel);

        // when
        Assertions.assertDoesNotThrow(
                () -> novelLikeServiceV0.setNovelLike(novel.getId(), author.getId()));
        List<NovelLike> afterNovelLike = novelLikeJpaRepository.findAllByNovel(novel);

        // then
        assertThat(beforeNovelLike).isEqualTo(Collections.emptyList());
        assertThat(afterNovelLike).isNotEqualTo(Collections.emptyList());
    }

    @Test
    @DisplayName("존재 하지 않는 소설에 대한 좋아요 테스트")
    void setNovelLikeToNotExistNovel() {
        // given
        Long notExistNovelId = 9999L;

        // when then
        Assertions.assertThrows(NovelNotFoundException.class,
                () -> novelLikeServiceV0.setNovelLike(notExistNovelId, author.getId()));
    }

    @Test
    @DisplayName("존재하지 않는 유저에 대한 좋아요 테스트")
    void setNoveLikeToNotExistAuthor() {
        // given
        Long notExistAuthorId = 123L;


        // when then
        Assertions.assertThrows(AuthorNotFoundException.class,
                () -> novelLikeServiceV0.setNovelLike(novel.getId(), notExistAuthorId));
    }

    @Test
    @DisplayName("소설 좋아요 중복 불가")
    void setNovelLikeDuplicated() {
        // given

        // when
        Assertions.assertDoesNotThrow(
                () -> novelLikeServiceV0.setNovelLike(novel.getId(), author.getId()));

        // then
        Assertions.assertThrows(DuplicatedLikeException.class,
                () -> novelLikeServiceV0.setNovelLike(novel.getId(), author.getId()));
    }

    @Test
    @DisplayName("소설 좋아요 삭제")
    void deleteNovelLike() {
        // given
        NovelLike novelLike = NovelLike.builder().novel(novel).author(author).build();

        // when
        novelLikeJpaRepository.save(novelLike);
        Assertions.assertDoesNotThrow(
                () -> novelLikeServiceV0.deleteNovelLike(novel.getId(), author.getId()));
        List<NovelLike> novelLikes = novelLikeJpaRepository.findAllByAuthor(author);

        // then
        assertThat(novelLikes.size()).isZero();
    }

    private void setMember(Author author) {
        Member member = MemberMother.generateMember();
        memberRepository.save(member);
        author.setMember(member);
    }
}

package us.usserver.novel.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import us.usserver.author.AuthorMother;
import us.usserver.domain.author.entity.Author;
import us.usserver.domain.author.repository.AuthorRepository;
import us.usserver.domain.member.entity.Member;
import us.usserver.domain.member.repository.MemberRepository;
import us.usserver.domain.novel.entity.Novel;
import us.usserver.domain.novel.entity.NovelLike;
import us.usserver.domain.novel.repository.NovelLikeRepository;
import us.usserver.domain.novel.repository.NovelRepository;
import us.usserver.domain.novel.service.NovelLikeService;
import us.usserver.global.response.exception.BaseException;
import us.usserver.global.response.exception.DuplicatedLikeException;
import us.usserver.global.response.exception.ExceptionMessage;
import us.usserver.global.response.exception.NovelNotFoundException;
import us.usserver.member.MemberMother;
import us.usserver.novel.NovelMother;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Rollback
@SpringBootTest
class NovelLikeServiceTest {
    @Autowired
    private NovelLikeService novelLikeService;

    @Autowired
    private NovelRepository novelRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private NovelLikeRepository novelLikeCustomRepository;

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
        List<NovelLike> beforeNovelLike = novelLikeCustomRepository.findAllByNovel(novel);

        // when
        Assertions.assertDoesNotThrow(
                () -> novelLikeService.setNovelLike(novel.getId(), author.getId()));
        List<NovelLike> afterNovelLike = novelLikeCustomRepository.findAllByNovel(novel);

        // then
        assertThat(beforeNovelLike).isEqualTo(Collections.emptyList());
        assertThat(afterNovelLike).isNotEqualTo(Collections.emptyList());
    }

    @Test
    @DisplayName("존재 하지 않는 소설에 대한 좋아요 테스트")
    void setNovelLikeToNotExistNovel() {
        // given
        Long notExistNovelId = 9999L;

        // when
        BaseException baseException = Assertions.assertThrows(BaseException.class,
                () -> novelLikeService.setNovelLike(notExistNovelId, author.getId()));

        // then
        assertThat(baseException.getMessage()).isEqualTo(ExceptionMessage.NOVEL_NOT_FOUND);

    }

    @Test
    @DisplayName("존재하지 않는 유저에 대한 좋아요 테스트")
    void setNoveLikeToNotExistAuthor() {
        // given
        Long notExistAuthorId = 123L;


        // when then
        BaseException baseException = Assertions.assertThrows(BaseException.class,
                () -> novelLikeService.setNovelLike(novel.getId(), notExistAuthorId));

        // then
        assertThat(baseException.getMessage()).isEqualTo(ExceptionMessage.AUTHOR_NOT_FOUND);
    }

    @Test
    @DisplayName("소설 좋아요 중복 불가")
    void setNovelLikeDuplicated() {
        // given

        // when
        Assertions.assertDoesNotThrow(
                () -> novelLikeService.setNovelLike(novel.getId(), author.getId()));

        // then
        BaseException baseException = Assertions.assertThrows(BaseException.class,
                () -> novelLikeService.setNovelLike(novel.getId(), author.getId()));
        assertThat(baseException.getMessage()).isEqualTo(ExceptionMessage.LIKE_DUPLICATED);
    }

    @Test
    @DisplayName("소설 좋아요 삭제")
    void deleteNovelLike() {
        // given
        NovelLike novelLike = NovelLike.builder().novel(novel).author(author).build();

        // when
        novelLikeCustomRepository.save(novelLike);
        Assertions.assertDoesNotThrow(
                () -> novelLikeService.deleteNovelLike(novel.getId(), author.getId()));
        List<NovelLike> novelLikes = novelLikeCustomRepository.findAllByAuthor(author);

        // then
        assertThat(novelLikes.size()).isZero();
    }

    private void setMember(Author author) {
        Member member = MemberMother.generateMember();
        memberRepository.save(member);
        author.setMember(member);
    }
}

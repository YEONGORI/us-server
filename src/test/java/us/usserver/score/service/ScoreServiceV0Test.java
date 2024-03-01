package us.usserver.score.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.domain.author.entity.Author;
import us.usserver.author.AuthorMother;
import us.usserver.domain.author.repository.AuthorRepository;
import us.usserver.domain.chapter.entity.Chapter;
import us.usserver.chapter.ChapterMother;
import us.usserver.domain.chapter.repository.ChapterRepository;
import us.usserver.global.exception.ScoreOutOfRangeException;
import us.usserver.domain.member.entity.Member;
import us.usserver.domain.chapter.service.ScoreServiceV0;
import us.usserver.member.MemberMother;
import us.usserver.domain.member.repository.MemberRepository;
import us.usserver.domain.novel.entity.Novel;
import us.usserver.novel.NovelMother;
import us.usserver.domain.novel.repository.NovelRepository;
import us.usserver.domain.chapter.repository.ScoreRepository;
import us.usserver.domain.chapter.dto.PostScore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Rollback
@Transactional
@SpringBootTest
class ScoreServiceV0Test {
    @Autowired
    private ScoreServiceV0 scoreServiceV0;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private NovelRepository novelRepository;
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private ScoreRepository scoreRepository;

    private Author author;
    private Novel novel;
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
    @DisplayName("챕터 평가 하기")
    void postScore0() {
        // given
        PostScore postScore = PostScore.builder().score(10).build();

        // when
        assertDoesNotThrow(
                () -> scoreServiceV0.postScore(chapter.getId(), author.getId(), postScore));
    }

    @Test
    @DisplayName("범위를 벗어난 점수로 평가 하기")
    void postScore1() {
        // given
        int maxInt = 2147483647;
        int minInt = -2147483648;
        PostScore postScore1 = PostScore.builder().score(maxInt).build();
        PostScore postScore2 = PostScore.builder().score(minInt).build();

        // when then
        assertThrows(ScoreOutOfRangeException.class,
                () -> scoreServiceV0.postScore(chapter.getId(), author.getId(), postScore1));
        assertThrows(ScoreOutOfRangeException.class,
                () -> scoreServiceV0.postScore(chapter.getId(), author.getId(), postScore2));
    }

    @Test
    @DisplayName("챕터 중복 평가 하기")
    void postScore2() {
        // given
        PostScore postScore1 = PostScore.builder().score(10).build();
        PostScore postScore2 = PostScore.builder().score(1).build();

        // when
        assertDoesNotThrow(
                () -> scoreServiceV0.postScore(chapter.getId(), author.getId(), postScore1));
        assertDoesNotThrow(
                () -> scoreServiceV0.postScore(chapter.getId(), author.getId(), postScore2));
        Double chapterScore = scoreServiceV0.getChapterScore(chapter);


        // then
        assertThat(chapterScore).isEqualTo(10.0);
    }


    @Test
    @DisplayName("챕터 평점 조회 하기")
    void getChapterScore() {
        // given
        Author author1 = AuthorMother.generateAuthor();
        Author author2 = AuthorMother.generateAuthor();
        Author author3 = AuthorMother.generateAuthor();
        setMember(author1);
        setMember(author2);
        setMember(author3);

        PostScore postScore1 = PostScore.builder().score(1).build();
        PostScore postScore2 = PostScore.builder().score(3).build();
        PostScore postScore3 = PostScore.builder().score(10).build();

        // when
        authorRepository.save(author1);
        authorRepository.save(author2);
        authorRepository.save(author3);
        scoreServiceV0.postScore(chapter.getId(), author1.getId(), postScore1);
        scoreServiceV0.postScore(chapter.getId(), author2.getId(), postScore2);
        scoreServiceV0.postScore(chapter.getId(), author3.getId(), postScore3);
        Double chapterScore = scoreServiceV0.getChapterScore(chapter);

        // then
        assertThat(chapterScore).isEqualTo(4.666666666666667);
    }

    private void setMember(Author author) {
        Member member = MemberMother.generateMember();
        memberRepository.save(member);
        author.setMember(member);
    }
}
package us.usserver.chapter.service;

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
import us.usserver.chapter.chapterEnum.ChapterStatus;
import us.usserver.chapter.dto.ChapterDetailInfo;
import us.usserver.chapter.dto.ChapterInfo;
import us.usserver.chapter.dto.CreateChapterReq;
import us.usserver.global.exception.MainAuthorIsNotMatchedException;
import us.usserver.global.exception.NovelNotFoundException;
import us.usserver.member.Member;
import us.usserver.member.MemberMother;
import us.usserver.member.MemberRepository;
import us.usserver.member.memberEnum.Gender;
import us.usserver.novel.Novel;
import us.usserver.novel.NovelMother;
import us.usserver.novel.NovelRepository;
import us.usserver.novel.novelEnum.AgeRating;
import us.usserver.novel.novelEnum.Genre;
import us.usserver.novel.novelEnum.Hashtag;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ChapterServiceV0Test {
    @Autowired
    private ChapterServiceV0 chapterServiceV0;

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private NovelRepository novelRepository;
    @Autowired
    private ChapterRepository chapterRepository;

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
    @DisplayName("회차 생성")
    void createChapter1() {
        // when
        assertDoesNotThrow(
                () -> chapterServiceV0.createChapter(novel.getId(), author.getId()));
        List<Chapter> chapters = chapterRepository.findAllByNovel(novel);

        // then
        assertThat(chapters.size()).isNotZero();
    }

    @Test
    @DisplayName("권한 없는 유저의 회차 생성")
    void createChapter2() {
        // given
        Author newAuthor = AuthorMother.generateAuthor();
        setMember(newAuthor);

        // when
        authorRepository.save(newAuthor);

        // then
        assertThrows(MainAuthorIsNotMatchedException.class,
                () -> chapterServiceV0.createChapter(novel.getId(), newAuthor.getId()));
    }

    @Test
    @DisplayName("소설 회차 정보 조회")
    void getChaptersOfNovel() {
        // given
        Chapter chapter1 = ChapterMother.generateChapter(novel);
        Chapter chapter2 = ChapterMother.generateChapter(novel);
        Chapter chapter3 = ChapterMother.generateChapter(novel);
        Chapter chapter4 = ChapterMother.generateChapter(novel);
        chapter1.setPartForTest(0);
        chapter2.setPartForTest(1);
        chapter3.setPartForTest(2);
        chapter4.setPartForTest(3);

        // when
        novel.getChapters().add(chapter1);
        novel.getChapters().add(chapter2);
        novel.getChapters().add(chapter3);
        novel.getChapters().add(chapter4);
        novelRepository.save(novel);
        chapterRepository.save(chapter3);
        chapterRepository.save(chapter4);
        chapterRepository.save(chapter1);
        chapterRepository.save(chapter2);

        List<ChapterInfo> chapterInfos = chapterServiceV0.getChaptersOfNovel(novel);

        // then
        assertThat(chapterInfos.size()).isEqualTo(4);
        assertThat(chapterInfos.get(0).getId()).isEqualTo(chapter1.getId());
        assertThat(chapterInfos.get(1).getId()).isEqualTo(chapter2.getId());
        assertThat(chapterInfos.get(2).getId()).isEqualTo(chapter3.getId());
        assertThat(chapterInfos.get(3).getId()).isEqualTo(chapter4.getId());
    }

    @Test
    @DisplayName("존재하지 않는 소설의 회차 조회 테스트")
    void getChaptersOfNotExistNovel() {
        // given
        Novel newNovel = NovelMother.generateNovel(author);

        // when
        List<ChapterInfo> chapterInfos = chapterServiceV0.getChaptersOfNovel(novel);

        // then
        assertThat(chapterInfos).isEqualTo(Collections.emptyList());
    }

    @Test
    @DisplayName("회차 상세 정보 조회")
    void getChapterDetailInfo() {
        // given
        Chapter chapter1 = ChapterMother.generateChapter(novel);
        Chapter chapter2 = ChapterMother.generateChapter(novel);
        Chapter chapter3 = ChapterMother.generateChapter(novel);
        chapter1.setPartForTest(0);
        chapter1.setStatusForTest(ChapterStatus.COMPLETED);
        chapter2.setPartForTest(1);
        chapter2.setStatusForTest(ChapterStatus.COMPLETED);
        chapter3.setPartForTest(2);

        // when
        novel.getChapters().add(chapter1);
        novel.getChapters().add(chapter2);
        novel.getChapters().add(chapter3);
        novelRepository.save(novel);
        chapterRepository.save(chapter1);
        chapterRepository.save(chapter2);
        chapterRepository.save(chapter3);
        ChapterDetailInfo chapterDetailInfo = chapterServiceV0.getChapterDetailInfo(novel.getId(), author.getId(), chapter2.getId());

        // then
        assertThat(chapterDetailInfo.getPart()).isEqualTo(chapter2.getPart());
        assertThat(chapterDetailInfo.getTitle()).isEqualTo(chapter2.getTitle());
        assertThat(chapterDetailInfo.getMyParagraph()).isNull();
        assertThat(chapterDetailInfo.getBestParagraph()).isNull();
        assertThat(chapterDetailInfo.getSelectedParagraphs()).isEqualTo(Collections.emptyList());
        assertThat(chapterDetailInfo.getPrevChapterUrl()).isEqualTo(createChapterUrl(novel.getId(), chapter1.getId()));
        assertThat(chapterDetailInfo.getNextChapterUrl()).isEqualTo(createChapterUrl(novel.getId(), chapter3.getId()));
    }

    @Test
    @DisplayName("첫 회차, 마지막 회차 정보 조회")
    void getChapterDetailInfo2() {
        // given
        Chapter chapter1 = ChapterMother.generateChapter(novel);
        Chapter chapter2 = ChapterMother.generateChapter(novel);
        Chapter chapter3 = ChapterMother.generateChapter(novel);
        chapter1.setPartForTest(0);
        chapter1.setStatusForTest(ChapterStatus.COMPLETED);
        chapter2.setPartForTest(1);
        chapter2.setStatusForTest(ChapterStatus.COMPLETED);
        chapter3.setPartForTest(2);

        // when
        novel.getChapters().add(chapter1);
        novel.getChapters().add(chapter2);
        novel.getChapters().add(chapter3);
        novelRepository.save(novel);
        chapterRepository.save(chapter1);
        chapterRepository.save(chapter2);
        chapterRepository.save(chapter3);
        ChapterDetailInfo chapterDetailInfo1 = chapterServiceV0.getChapterDetailInfo(novel.getId(), author.getId(), chapter1.getId());
        ChapterDetailInfo chapterDetailInfo3 = chapterServiceV0.getChapterDetailInfo(novel.getId(), author.getId(), chapter3.getId());

        // then
        assertThat(chapterDetailInfo1.getPart()).isEqualTo(chapter1.getPart());
        assertThat(chapterDetailInfo1.getTitle()).isEqualTo(chapter1.getTitle());
        assertThat(chapterDetailInfo1.getMyParagraph()).isNull();
        assertThat(chapterDetailInfo1.getBestParagraph()).isNull();
        assertThat(chapterDetailInfo1.getSelectedParagraphs()).isEqualTo(Collections.emptyList());
        assertThat(chapterDetailInfo1.getPrevChapterUrl()).isNull();
        assertThat(chapterDetailInfo1.getNextChapterUrl()).isEqualTo(createChapterUrl(novel.getId(), chapter2.getId()));

        assertThat(chapterDetailInfo3.getPart()).isEqualTo(chapter3.getPart());
        assertThat(chapterDetailInfo3.getTitle()).isEqualTo(chapter3.getTitle());
        assertThat(chapterDetailInfo3.getMyParagraph()).isNull();
        assertThat(chapterDetailInfo3.getBestParagraph()).isNull();
        assertThat(chapterDetailInfo3.getSelectedParagraphs()).isEqualTo(Collections.emptyList());
        assertThat(chapterDetailInfo3.getPrevChapterUrl()).isEqualTo(createChapterUrl(novel.getId(), chapter2.getId()));
        assertThat(chapterDetailInfo3.getNextChapterUrl()).isNull();
    }

    private void setMember(Author author) {
        Member member = MemberMother.generateMember();
        memberRepository.save(member);
        author.setMember(member);
    }

    private static String createChapterUrl(Long novelId, Long chapterId) {
        return "http://localhost:8000/chapter/" + novelId + "/" + chapterId;
    }
}
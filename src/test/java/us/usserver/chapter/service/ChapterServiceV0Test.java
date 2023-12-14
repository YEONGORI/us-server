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
import us.usserver.chapter.ChapterRepository;
import us.usserver.chapter.dto.ChapterInfo;
import us.usserver.chapter.dto.CreateChapterReq;
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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
    void createChapter() {
        // given
        String testTitle = "TITLE";
        CreateChapterReq createChapterReq = CreateChapterReq.builder().title(testTitle).build();

        // when
        assertDoesNotThrow(
                () -> chapterServiceV0.createChapter(novel.getId(), author.getId()));
        List<Chapter> chapters = chapterRepository.findAllByNovel(novel);

        // then
        assertThat(chapters.get(0).getTitle()).isEqualTo(testTitle);
    }

    @Test
    @DisplayName("소설 회차 정보 조회")
    void getChaptersOfNovel() {
        int prevSize = chapterServiceV0.getChaptersOfNovel(novel).size();
        assertDoesNotThrow(
                () -> {
                    chapterServiceV0.createChapter(1L, 1L);
                    chapterServiceV0.createChapter(1L, 1L);
                }
        );

        List<ChapterInfo> chapterInfos = assertDoesNotThrow(
                () -> chapterServiceV0.getChaptersOfNovel(novel));

        assertThat(chapterInfos.size()).isEqualTo(prevSize + 2);
    }

    private void setMember(Author author) {
        Member member = Member.builder().age(1).gender(Gender.MALE).build();
        memberRepository.save(member);
        author.setMember(member);
    }
}
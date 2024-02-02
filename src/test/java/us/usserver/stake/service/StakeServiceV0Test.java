package us.usserver.stake.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.author.Author;
import us.usserver.author.AuthorMother;
import us.usserver.author.AuthorRepository;
import us.usserver.authority.Authority;
import us.usserver.authority.AuthorityRepository;
import us.usserver.chapter.Chapter;
import us.usserver.chapter.ChapterMother;
import us.usserver.chapter.ChapterRepository;
import us.usserver.member.Member;
import us.usserver.member.MemberMother;
import us.usserver.member.MemberRepository;
import us.usserver.novel.Novel;
import us.usserver.novel.NovelMother;
import us.usserver.novel.repository.NovelJpaRepository;
import us.usserver.paragraph.Paragraph;
import us.usserver.paragraph.ParagraphMother;
import us.usserver.paragraph.ParagraphRepository;
import us.usserver.paragraph.paragraphEnum.ParagraphStatus;
import us.usserver.paragraph.service.ParagraphServiceV0;
import us.usserver.stake.dto.StakeInfo;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Rollback
@Transactional
@SpringBootTest
class StakeServiceV0Test {
    @Autowired
    StakeServiceV0 stakeServiceV0;
    @Autowired
    ParagraphServiceV0 paragraphServiceV0;

    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private NovelJpaRepository novelJpaRepository;
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private ParagraphRepository paragraphRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private MemberRepository memberRepository;

    Author mainAuthor, author1, author2, author3;
    Novel novel;
    Chapter chapter1, chapter2, chapter3;
    Paragraph paragraph1_1, paragraph2_1, paragraph2_2, paragraph3_1, paragraph3_2, paragraph3_3;

    @BeforeEach
    void setUp() {
        mainAuthor = AuthorMother.generateAuthor();
        author1 = AuthorMother.generateAuthor();
        author2 = AuthorMother.generateAuthor();
        author3 = AuthorMother.generateAuthor();
        setMember(mainAuthor);
        setMember(author1);
        setMember(author2);
        setMember(author3);
        authorRepository.save(mainAuthor);
        authorRepository.save(author1);
        authorRepository.save(author2);
        authorRepository.save(author3);

        novel = NovelMother.generateNovel(mainAuthor);

        chapter1 = ChapterMother.generateChapter(novel);
        chapter2 = ChapterMother.generateChapter(novel);
        chapter3 = ChapterMother.generateChapter(novel);
        novel.getChapters().add(chapter1);
        novel.getChapters().add(chapter2);
        novel.getChapters().add(chapter3);
        novelJpaRepository.save(novel);

        authorityRepository.save(Authority.builder()
                .novel(novel).author(author1).build());
        authorityRepository.save(Authority.builder()
                .novel(novel).author(author2).build());
        authorityRepository.save(Authority.builder()
                .novel(novel).author(author3).build());

        paragraph1_1 = ParagraphMother.generateParagraph(author1, chapter1);
        paragraph2_1 = ParagraphMother.generateParagraph(author1, chapter2);
        paragraph3_1 = ParagraphMother.generateParagraph(author1, chapter3);
        paragraph2_2 = ParagraphMother.generateParagraph(author2, chapter2);
        paragraph3_2 = ParagraphMother.generateParagraph(author2, chapter3);
        paragraph3_3 = ParagraphMother.generateParagraph(author3, chapter3);
        chapter1.getParagraphs().add(paragraph1_1);
        chapter2.getParagraphs().add(paragraph2_1);
        chapter2.getParagraphs().add(paragraph2_2);
        chapter3.getParagraphs().add(paragraph3_1);
        chapter3.getParagraphs().add(paragraph3_2);
        chapter3.getParagraphs().add(paragraph3_3);
        chapterRepository.save(chapter1);
        chapterRepository.save(chapter2);
        chapterRepository.save(chapter3);

        paragraphRepository.save(paragraph1_1);
        paragraphRepository.save(paragraph2_1);
        paragraphRepository.save(paragraph2_2);
        paragraphRepository.save(paragraph3_1);
        paragraphRepository.save(paragraph3_2);
        paragraphRepository.save(paragraph3_3);

        paragraphServiceV0.selectParagraph(mainAuthor.getId(), novel.getId(), chapter1.getId(), paragraph1_1.getId());
        paragraphServiceV0.selectParagraph(mainAuthor.getId(), novel.getId(), chapter2.getId(), paragraph2_1.getId());
        paragraphServiceV0.selectParagraph(mainAuthor.getId(), novel.getId(), chapter2.getId(), paragraph2_2.getId());
        paragraphServiceV0.selectParagraph(mainAuthor.getId(), novel.getId(), chapter3.getId(), paragraph3_1.getId());
        paragraphServiceV0.selectParagraph(mainAuthor.getId(), novel.getId(), chapter3.getId(), paragraph3_2.getId());
        paragraphServiceV0.selectParagraph(mainAuthor.getId(), novel.getId(), chapter3.getId(), paragraph3_3.getId());
    }


    @Test
    @DisplayName("소설이 처음 만들어질 때 mainAuthor 의 지분이 100인지 확인")
    void checkInitialStake() {
        // TODO: @종두님 소설 생성 하기 만드신 후 이 테스트 작성해 주세요
    }

    @Test
    @DisplayName("작가 한명만 계속 해서 소설을 업데이트 할 때 지분 100이 유지되는지 확인")
    void checkOneAuthorStake() {
        // given
        Novel novelForOne = NovelMother.generateNovel(author1);
        Chapter chapterForOne = ChapterMother.generateChapter(novelForOne);
        Paragraph p1 = ParagraphMother.generateParagraph(author1, chapterForOne);
        p1.setParagraphStatusForTest(ParagraphStatus.SELECTED);
        Paragraph p2 = ParagraphMother.generateParagraph(author1, chapterForOne);
        p2.setParagraphStatusForTest(ParagraphStatus.SELECTED);
        Paragraph p3 = ParagraphMother.generateParagraph(author1, chapterForOne);
        p3.setParagraphStatusForTest(ParagraphStatus.SELECTED);

        // when
        novelForOne.getChapters().add(chapterForOne);
        novelJpaRepository.save(novelForOne);

        chapterForOne.getParagraphs().add(p1);
        chapterForOne.getParagraphs().add(p2);
        chapterRepository.save(chapterForOne);

        paragraphRepository.save(p1);
        paragraphRepository.save(p2);
        paragraphRepository.save(p3);

        authorityRepository.save(Authority.builder()
                .novel(novelForOne).author(author1).build());
        stakeServiceV0.setStakeInfoOfNovel(novelForOne);

        // then
        List<StakeInfo> stakeInfos = stakeServiceV0.getStakeInfoOfNovel(novelForOne.getId());
        assertThat(stakeInfos.size()).isEqualTo(1);
        assertThat(stakeInfos.get(0).getAuthorInfo().getId()).isEqualTo(author1.getId());
        assertThat(stakeInfos.get(0).getPercentage()).isEqualTo(1F);
    }

    @Test
    @DisplayName("여러 작가가 추가 되었을 때 지분 정보 확인")
    void getStakeInfoOfNovel() {
        // given
        List<StakeInfo> prevInfo = stakeServiceV0.getStakeInfoOfNovel(novel.getId());
        Author newAuthor = AuthorMother.generateAuthor();
        setMember(newAuthor);
        Authority authority = Authority.builder().author(newAuthor).novel(novel).build();
        Paragraph newParagraph = ParagraphMother.generateParagraph(newAuthor, chapter3);

        // when
        authorRepository.save(newAuthor);
        authorityRepository.save(authority);
        chapter3.getParagraphs().add(newParagraph);
        paragraphRepository.save(newParagraph);
        stakeServiceV0.setStakeInfoOfNovel(novel);

        // then
        List<StakeInfo> currInfo = stakeServiceV0.getStakeInfoOfNovel(novel.getId());
        assertThat(currInfo.size()).isEqualTo(prevInfo.size() + 1);

        for (StakeInfo prevStake : prevInfo) {
            for (StakeInfo curStake : currInfo) {
                if (curStake.getAuthorInfo() == prevStake.getAuthorInfo()) {
                    assertThat(prevStake.getPercentage()).isGreaterThanOrEqualTo(curStake.getPercentage());
                }
            }
        }
    }

    private void setMember(Author author) {
        Member member = MemberMother.generateMember();
        memberRepository.save(member);
        author.setMember(member);
    }
}

package us.usserver.stake;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.domain.authority.dto.res.StakeInfoResponse;
import us.usserver.domain.authority.service.StakeService;
import us.usserver.domain.author.entity.Author;
import us.usserver.author.AuthorMother;
import us.usserver.domain.author.repository.AuthorRepository;
import us.usserver.domain.authority.entity.Authority;
import us.usserver.domain.authority.repository.AuthorityRepository;
import us.usserver.domain.chapter.entity.Chapter;
import us.usserver.chapter.ChapterMother;
import us.usserver.domain.chapter.repository.ChapterRepository;
import us.usserver.domain.member.entity.Member;
import us.usserver.member.MemberMother;
import us.usserver.domain.member.repository.MemberRepository;
import us.usserver.domain.novel.entity.Novel;
import us.usserver.novel.NovelMother;
import us.usserver.domain.novel.repository.NovelRepository;
import us.usserver.domain.paragraph.entity.Paragraph;
import us.usserver.paragraph.ParagraphMother;
import us.usserver.domain.paragraph.repository.ParagraphRepository;
import us.usserver.domain.paragraph.constant.ParagraphStatus;
import us.usserver.domain.paragraph.service.ParagraphServiceImpl;
import us.usserver.domain.authority.dto.StakeInfo;

import static org.assertj.core.api.Assertions.assertThat;

@Rollback
@Transactional
@SpringBootTest
class StakeServiceTest {
    @Autowired
    StakeService stakeService;
    @Autowired
    ParagraphServiceImpl paragraphServiceImpl;

    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private NovelRepository novelRepository;
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
        novelRepository.save(novel);

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

        paragraphServiceImpl.selectParagraph(mainAuthor.getId(), novel.getId(), chapter1.getId(), paragraph1_1.getId());
        paragraphServiceImpl.selectParagraph(mainAuthor.getId(), novel.getId(), chapter2.getId(), paragraph2_1.getId());
        paragraphServiceImpl.selectParagraph(mainAuthor.getId(), novel.getId(), chapter2.getId(), paragraph2_2.getId());
        paragraphServiceImpl.selectParagraph(mainAuthor.getId(), novel.getId(), chapter3.getId(), paragraph3_1.getId());
        paragraphServiceImpl.selectParagraph(mainAuthor.getId(), novel.getId(), chapter3.getId(), paragraph3_2.getId());
        paragraphServiceImpl.selectParagraph(mainAuthor.getId(), novel.getId(), chapter3.getId(), paragraph3_3.getId());
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
        novelRepository.save(novelForOne);

        chapterForOne.getParagraphs().add(p1);
        chapterForOne.getParagraphs().add(p2);
        chapterRepository.save(chapterForOne);

        paragraphRepository.save(p1);
        paragraphRepository.save(p2);
        paragraphRepository.save(p3);

        authorityRepository.save(Authority.builder()
                .novel(novelForOne).author(author1).build());
        stakeService.setStakeInfoOfNovel(novelForOne);

        // then
        StakeInfoResponse stakeInfoResponse = stakeService.getStakeInfoOfNovel(novelForOne.getId());
        assertThat(stakeInfoResponse.getStakeInfos().size()).isEqualTo(1);
        assertThat(stakeInfoResponse.getStakeInfos().get(0).getAuthorInfo().id()).isEqualTo(author1.getId());
        assertThat(stakeInfoResponse.getStakeInfos().get(0).getPercentage()).isEqualTo(1F);
    }

    @Test
    @DisplayName("여러 작가가 추가 되었을 때 지분 정보 확인")
    void getStakeInfoOfNovel() {
        // given
        StakeInfoResponse prevStakeResponse = stakeService.getStakeInfoOfNovel(novel.getId());
        Author newAuthor = AuthorMother.generateAuthor();
        setMember(newAuthor);
        Authority authority = Authority.builder().author(newAuthor).novel(novel).build();
        Paragraph newParagraph = ParagraphMother.generateParagraph(newAuthor, chapter3);

        // when
        authorRepository.save(newAuthor);
        authorityRepository.save(authority);
        chapter3.getParagraphs().add(newParagraph);
        paragraphRepository.save(newParagraph);
        stakeService.setStakeInfoOfNovel(novel);

        // then
        StakeInfoResponse curStakeResponse = stakeService.getStakeInfoOfNovel(novel.getId());
        assertThat(prevStakeResponse.getStakeInfos().size()).isEqualTo(curStakeResponse.getStakeInfos().size() + 1);

        for (StakeInfo prevStake : prevStakeResponse.getStakeInfos()) {
            for (StakeInfo curStake : curStakeResponse.getStakeInfos()) {
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

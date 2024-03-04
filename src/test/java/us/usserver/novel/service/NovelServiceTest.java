package us.usserver.novel.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.author.AuthorMother;
import us.usserver.chapter.ChapterMother;
import us.usserver.domain.author.entity.Author;
import us.usserver.domain.author.entity.ReadNovel;
import us.usserver.domain.author.repository.AuthorRepository;
import us.usserver.domain.chapter.entity.Chapter;
import us.usserver.domain.chapter.repository.ChapterRepository;
import us.usserver.domain.member.entity.Member;
import us.usserver.domain.member.repository.MemberRepository;
import us.usserver.domain.novel.constant.AgeRating;
import us.usserver.domain.novel.constant.Genre;
import us.usserver.domain.novel.constant.Hashtag;
import us.usserver.domain.novel.constant.NovelSize;
import us.usserver.domain.novel.dto.*;
import us.usserver.domain.novel.entity.Novel;
import us.usserver.domain.novel.repository.NovelRepository;
import us.usserver.domain.novel.service.NovelService;
import us.usserver.global.response.exception.BaseException;
import us.usserver.global.response.exception.ErrorCode;
import us.usserver.global.response.exception.MainAuthorIsNotMatchedException;
import us.usserver.global.response.exception.NovelNotFoundException;
import us.usserver.member.MemberMother;
import us.usserver.novel.NovelMother;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class NovelServiceTest {
    @Autowired
    private NovelService novelService;

    @Autowired
    private NovelRepository novelRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ChapterRepository chapterRepository;

    private Novel novel;
    private Novel dummyNovel;
    private Member member;
    private Member dummymember;
    private Author author;
    private Author dummyAuthor;

    @BeforeEach
    void setup() {
        member = MemberMother.generateMember();
        dummymember = MemberMother.generateMember();
        author = AuthorMother.generateAuthor();
        dummyAuthor = AuthorMother.generateAuthor();

        author.setMember(member);
        dummyAuthor.setMember(dummymember);

        memberRepository.save(member);
        memberRepository.save(dummymember);
        authorRepository.save(author);
        authorRepository.save(dummyAuthor);

        novel = NovelMother.generateNovel(author);
        dummyNovel = NovelMother.generateNovel(dummyAuthor);

        novelRepository.save(novel);
        novelRepository.save(dummyNovel);
    }

    @Test
    @DisplayName("소설 정보 확인")
    void getNovelInfo() {
        NovelInfo novelInfo = assertDoesNotThrow(
                () -> novelService.getNovelInfo(novel.getId()));

        assertThat(novelInfo.novelSharelUrl()).contains("");
    }

    @Test
    @DisplayName("존재하지 않는 소설 정보 확인")
    void getNotExistNovel() {
        assertThrows(NovelNotFoundException.class,
                () -> novelService.getNovelInfo(novel.getId() + 9999));
    }

    @Test
    @DisplayName("소설 상세 정보 확인")
    void getNovelDetailInfo() {
        NovelDetailInfo novelDetailInfo = assertDoesNotThrow(
                () -> novelService.getNovelDetailInfo(novel.getId()));

        assertThat(novelDetailInfo.getTitle()).isEqualTo(novel.getTitle());
        assertThat(novelDetailInfo.getThumbnail()).isEqualTo(novel.getThumbnail());
        assertThat(novelDetailInfo.getSynopsis()).isEqualTo(novel.getSynopsis());
        assertThat(novelDetailInfo.getAuthorName()).isEqualTo(novel.getMainAuthor().getNickname());
        assertThat(novelDetailInfo.getAuthorIntroduction()).isEqualTo(novel.getAuthorDescription());
        assertThat(novelDetailInfo.getAgeRating()).isEqualTo(novel.getAgeRating());
        assertThat(novelDetailInfo.getGenre()).isEqualTo(novel.getGenre());
        assertThat(novelDetailInfo.getHashtags()).isEqualTo(novel.getHashtags());
        assertThat(novelDetailInfo.getStakeInfos()).isEqualTo(Collections.emptyList());
        assertThat(novelDetailInfo.getChapterInfos()).isEqualTo(Collections.emptyList());
    }

    @Test
    @DisplayName("소설 상세 정보 확인 - Chapter 정보 추가")
    void getNovelDetailInfo2() {
        // given
        Chapter chapter1 = ChapterMother.generateChapter(novel);
        Chapter chapter2 = ChapterMother.generateChapter(novel);

        // when
        chapterRepository.save(chapter1);
        chapterRepository.save(chapter2);
        novel.getChapters().add(chapter1);
        novel.getChapters().add(chapter2);
        novelRepository.save(novel);

        NovelDetailInfo novelDetailInfo = assertDoesNotThrow(
                () -> novelService.getNovelDetailInfo(novel.getId()));

        // then
        assertThat(novelDetailInfo.getTitle()).isEqualTo(novel.getTitle());
        assertThat(novelDetailInfo.getThumbnail()).isEqualTo(novel.getThumbnail());
        assertThat(novelDetailInfo.getSynopsis()).isEqualTo(novel.getSynopsis());
        assertThat(novelDetailInfo.getAuthorName()).isEqualTo(novel.getMainAuthor().getNickname());
        assertThat(novelDetailInfo.getAuthorIntroduction()).isEqualTo(novel.getAuthorDescription());
        assertThat(novelDetailInfo.getAgeRating()).isEqualTo(novel.getAgeRating());
        assertThat(novelDetailInfo.getGenre()).isEqualTo(novel.getGenre());
        assertThat(novelDetailInfo.getHashtags()).isEqualTo(novel.getHashtags());
        assertThat(novelDetailInfo.getStakeInfos()).isEqualTo(Collections.emptyList());
        assertThat(novelDetailInfo.getChapterInfos().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("쇼셜 소개 수정")
    void modifyNovelSynopsis() {
        NovelSynopsis synopsisRequest = NovelMother.generateSysnopsis();
        String synopsisResponse = assertDoesNotThrow(
                () -> novelService.modifyNovelSynopsis(novel.getId(), author.getId(), synopsisRequest.getSynopsis()));

        assertThat(synopsisRequest.getSynopsis()).isEqualTo(synopsisResponse);
    }
    
    @Test
    @DisplayName("권한 없는 작가의 소설 소개 수정")
    void modifySysnopsisNotAuthority() {
        NovelSynopsis synopsisRequest = NovelMother.generateSysnopsis();
        assertThrows(MainAuthorIsNotMatchedException.class,
                () -> novelService.modifyNovelSynopsis(novel.getId(), dummyAuthor.getId(), synopsisRequest.getSynopsis()));
    }

    @Test
    @DisplayName("작가 소개 수정")
    void modifyAuthorDescription() {
        AuthorDescription descriptionRequest = NovelMother.generateDescription();
        AuthorDescription desriptionResponse = assertDoesNotThrow(
                () -> novelService.modifyAuthorDescription(novel.getId(), author.getId(), descriptionRequest));

        assertThat(descriptionRequest.getDescription()).isEqualTo(desriptionResponse.getDescription());
    }

    @Test
    @DisplayName("권한 없는 작가의 작가 소개 수정")
    void modifyDescriptionNotAuthority() {
        AuthorDescription authorDescription = NovelMother.generateDescription();
        assertThrows(MainAuthorIsNotMatchedException.class,
                () -> novelService.modifyAuthorDescription(novel.getId(), dummyAuthor.getId(), authorDescription));

    }


    @Test
    @DisplayName("소설 생성 성공")
    void create_novel_success() {
        //given
        NovelBlueprint novelBlueprint = NovelBlueprint.builder()
                .title("TEST TITLE")
                .thumbnail("TEST THUMBNAIL")
                .synopsis("TEST SYNOPSIS")
                .authorDescription("TEST AUTHORDESCRIPTION")
                .hashtag(Collections.singleton(Hashtag.HASHTAG1))
                .genre(Genre.FANTASY)
                .ageRating(AgeRating.GENERAL)
                .novelSize(NovelSize.LONG)
                .build();

        //when
        NovelInfo novelInfo = novelService.createNovel(member, novelBlueprint);

        //then
        assertThat(novelInfo).isNotNull();
        assertThat(novelInfo.title()).isEqualTo("TEST TITLE");
        assertThat(novelInfo.hashtag()).isEqualTo(Collections.singleton(Hashtag.HASHTAG1));
        assertThat(novelInfo.genre()).isEqualTo(Genre.FANTASY);
        assertThat(novelInfo.createdAuthor().id()).isEqualTo(author.getId());
        assertThat(novelInfo.joinedAuthorCnt()).isZero();
        assertThat(novelInfo.commentCnt()).isZero();
    }

    @Test
    @DisplayName("소설 생성 실패")
    void create_novel_fail() {
        //given
        Member newMember = MemberMother.generateMember();
        NovelBlueprint novelBlueprint = NovelBlueprint.builder()
                .title("TEST TITLE")
                .thumbnail("TEST THUMBNAIL")
                .synopsis("TEST SYNOPSIS")
                .authorDescription("TEST AUTHORDESCRIPTION")
                .hashtag(Collections.singleton(Hashtag.HASHTAG1))
                .genre(Genre.FANTASY)
                .ageRating(AgeRating.GENERAL)
                .novelSize(NovelSize.LONG)
                .build();

        //when then
        Assertions.assertThrows(BaseException.class,
                () -> novelService.createNovel(newMember, novelBlueprint));
        Assertions.assertThrows(BaseException.class,
                () -> novelService.createNovel(null, novelBlueprint));
    }

    /*
    @Test
    @DisplayName("소설 검색")
    void 소설검색_제목_성공() {
        //given
        SearchNovelReq searchNovelReq1 = SearchNovelReq.builder()
                .authorId(author.getId())
                .title("TITLE")
                .hashtag(null)
                .status(null)
                .lastNovelId(0L)
                .size(5)
                .sortDto(new SortDto(Sorts.HIT, Orders.DESC))
                .build();

        SearchNovelReq searchNovelReq2 = SearchNovelReq.builder()
                .authorId(author.getId())
                .title("TITLE")
                .hashtag(null)
                .status(null)
                .lastNovelId(0L)
                .size(5)
                .sortDto(new SortDto(Sorts.LATEST, Orders.ASC))
                .build();


        //when
        NovelPageInfoResponse novelPageInfoResponse1 = novelServiceV0.searchNovel(searchNovelReq1);
        NovelPageInfoResponse novelPageInfoResponse2 = novelServiceV0.searchNovel(searchNovelReq2);

        //then
        assertThat(novelPageInfoResponse1.getNovelList().size()).isEqualTo(2);
        assertThat(novelPageInfoResponse1.getNovelList().get(0).getTitle()).isEqualTo("TITLE2");
        assertThat(novelPageInfoResponse1.getNovelList().get(1).getTitle()).isEqualTo("TITLE");

        assertThat(novelPageInfoResponse2.getNovelList().size()).isEqualTo(2);
        assertThat(novelPageInfoResponse2.getNovelList().get(0).getTitle()).isEqualTo("TITLE");
        assertThat(novelPageInfoResponse2.getNovelList().get(1).getTitle()).isEqualTo("TITLE2");
    }

    @Test
    @DisplayName("검색 키워드")
    void 소설검색_키워드_성공() {
        //given
        SearchNovelReq searchNovelReq1 = SearchNovelReq.builder()
                .authorId(author.getId())
                .title("TITLE")
                .hashtag(null)
                .status(null)
                .lastNovelId(0L)
                .size(5)
                .sortDto(new SortDto(Sorts.HIT, Orders.DESC))
                .build();

        SearchNovelReq searchNovelReq2 = SearchNovelReq.builder()
                .authorId(author.getId())
                .title("TITLE2")
                .hashtag(null)
                .status(null)
                .lastNovelId(0L)
                .size(5)
                .sortDto(new SortDto(Sorts.LATEST, Orders.ASC))
                .build();
        SearchNovelReq searchNovelReq3 = SearchNovelReq.builder()
                .authorId(author.getId())
                .title("TITLE")
                .hashtag(null)
                .status(null)
                .lastNovelId(0L)
                .size(5)
                .sortDto(new SortDto(Sorts.LATEST, Orders.ASC))
                .build();

        //when
        NovelPageInfoResponse novelPageInfoResponse1 = novelServiceV0.searchNovel(searchNovelReq1);
        NovelPageInfoResponse novelPageInfoResponse2 = novelServiceV0.searchNovel(searchNovelReq2);
        NovelPageInfoResponse novelPageInfoResponse3 = novelServiceV0.searchNovel(searchNovelReq3);

        SearchKeywordResponse searchKeywordResponse = novelServiceV0.searchKeyword();

        //then
        assertThat(searchKeywordResponse.getHotSearch().size()).isEqualTo(2);
        assertThat(searchKeywordResponse.getHotSearch().get(0)).isEqualTo("TITLE");
        assertThat(searchKeywordResponse.getHotSearch().get(1)).isEqualTo("TITLE2");

        assertThat(searchKeywordResponse.getRecentSearch().size()).isEqualTo(2);
        assertThat(searchKeywordResponse.getRecentSearch().get(0)).isEqualTo("TITLE");
        assertThat(searchKeywordResponse.getRecentSearch().get(1)).isEqualTo("TITLE2");
    } */

    @Test
    @DisplayName("메인 페이지 로드 테스트")
    void get_main_page() {
        // given
        Novel newNovel = NovelMother.generateNovel(author);
        for (int i=0; i<100; i++)
            newNovel.upHitCnt();
        Chapter newChapter = ChapterMother.generateChapter(novel);
        author.addReadNovel(ReadNovel.builder().author(author).novel(novel).readDate(LocalDateTime.now()).build());
        novel.addChapter(newChapter);

        //when
        authorRepository.save(author);
        novelRepository.save(newNovel);
        novelRepository.save(novel);
        chapterRepository.save(newChapter);
        MainPageResponse mainPageResponse = novelService.getMainPage(member);

        //then
        assertThat(mainPageResponse.getReadNovels().get(0).id()).isEqualTo(novel.getId());
        assertThat(mainPageResponse.getPopularNovels().get(0).id()).isEqualTo(newNovel.getId());
        assertThat(mainPageResponse.getRealTimeUpdateNovels().get(0).id()).isEqualTo(novel.getId());
        assertThat(mainPageResponse.getRecentlyCreatedNovels().get(0).id()).isEqualTo(newNovel.getId());
    }

    @Test
    @DisplayName("신작 소설 더보기")
    void get_more_novel_NEW() {
        //given
        Novel novel1 = NovelMother.generateNovel(author);
        Novel novel2 = NovelMother.generateNovel(author);
        Novel novel3 = NovelMother.generateNovel(author);
        Novel novel4 = NovelMother.generateNovel(author);
        Novel novel5 = NovelMother.generateNovel(author);
        Novel novel6 = NovelMother.generateNovel(author);
        Novel novel7 = NovelMother.generateNovel(author);
        Novel novel8 = NovelMother.generateNovel(author);
        MoreNovelRequest moreNovelRequest1 = MoreNovelRequest.builder()
                .mainNovelType(MainNovelType.NEW).nextPage(0).build();
        MoreNovelRequest moreNovelRequest2 = MoreNovelRequest.builder()
                .mainNovelType(MainNovelType.NEW).nextPage(1).build();

        //when
        novelRepository.save(novel1);
        novelRepository.save(novel2);
        novelRepository.save(novel3);
        novelRepository.save(novel4);
        novelRepository.save(novel5);
        novelRepository.save(novel6);
        novelRepository.save(novel7);
        novelRepository.save(novel8);
        MoreNovelResponse moreNovelResponse1 = novelService.getMoreNovels(member, moreNovelRequest1);
        MoreNovelResponse moreNovelResponse2 = novelService.getMoreNovels(member, moreNovelRequest2);

        //then
        assertThat(moreNovelResponse1.novelList().get(0).id()).isEqualTo(novel8.getId());
        assertThat(moreNovelResponse1.novelList().get(5).id()).isEqualTo(novel3.getId());
        assertThat(moreNovelResponse1.nextPage()).isOne();
        assertThat(moreNovelResponse1.hasNext()).isTrue();
        assertThat(moreNovelResponse1.novelList().size()).isEqualTo(6);

        assertThat(moreNovelResponse2.nextPage()).isEqualTo(2);
        assertThat(moreNovelResponse2.hasNext()).isFalse();
        assertThat(moreNovelResponse2.novelList().size()).isEqualTo(4); // setup 메서드 2개 포함
    }

    @Test
    @DisplayName("인기 소설 더보기")
    void get_more_novel_POPULAR() {
        //given
        Novel novel1 = NovelMother.generateNovel(author);
        Novel novel2 = NovelMother.generateNovel(author);
        Novel novel3 = NovelMother.generateNovel(author);
        Novel novel4 = NovelMother.generateNovel(author);
        Novel novel5 = NovelMother.generateNovel(author);
        Novel novel6 = NovelMother.generateNovel(author);
        Novel novel7 = NovelMother.generateNovel(author);
        Novel novel8 = NovelMother.generateNovel(author);
        for (int i=0; i<6; i++) novel1.upHitCnt();
        for (int i=0; i<5; i++) novel2.upHitCnt();
        for (int i=0; i<4; i++) novel3.upHitCnt();
        for (int i=0; i<3; i++) novel4.upHitCnt();
        for (int i=0; i<2; i++) novel5.upHitCnt();
        for (int i=0; i<1; i++) novel6.upHitCnt();

        MoreNovelRequest moreNovelRequest1 = MoreNovelRequest.builder()
                .mainNovelType(MainNovelType.POPULAR).nextPage(0).build();
        MoreNovelRequest moreNovelRequest2 = MoreNovelRequest.builder()
                .mainNovelType(MainNovelType.POPULAR).nextPage(1).build();

        //when
        novelRepository.save(novel1);
        novelRepository.save(novel2);
        novelRepository.save(novel3);
        novelRepository.save(novel4);
        novelRepository.save(novel5);
        novelRepository.save(novel6);
        novelRepository.save(novel7);
        novelRepository.save(novel8);
        MoreNovelResponse moreNovelResponse1 = novelService.getMoreNovels(member, moreNovelRequest1);
        MoreNovelResponse moreNovelResponse2 = novelService.getMoreNovels(member, moreNovelRequest2);

        //then
        assertThat(moreNovelResponse1.novelList().get(0).id()).isEqualTo(novel1.getId());
        assertThat(moreNovelResponse1.novelList().get(1).id()).isEqualTo(novel2.getId());
        assertThat(moreNovelResponse1.novelList().get(2).id()).isEqualTo(novel3.getId());
        assertThat(moreNovelResponse1.novelList().get(3).id()).isEqualTo(novel4.getId());
        assertThat(moreNovelResponse1.novelList().get(4).id()).isEqualTo(novel5.getId());
        assertThat(moreNovelResponse1.novelList().get(5).id()).isEqualTo(novel6.getId());

        assertThat(moreNovelResponse1.nextPage()).isOne();
        assertThat(moreNovelResponse1.hasNext()).isTrue();
        assertThat(moreNovelResponse1.novelList().size()).isEqualTo(6);

        assertThat(moreNovelResponse2.nextPage()).isEqualTo(2);
        assertThat(moreNovelResponse2.hasNext()).isFalse();
    }

    @Test
    @DisplayName("실시간 업데이트 소설 더보기")
    void get_more_novel_UPDATE() {
        //given
        Novel novel1 = NovelMother.generateNovel(author);
        Novel novel2 = NovelMother.generateNovel(author);
        Novel novel3 = NovelMother.generateNovel(author);
        Novel novel4 = NovelMother.generateNovel(author);
        Novel novel5 = NovelMother.generateNovel(author);
        Novel novel6 = NovelMother.generateNovel(author);
        Novel novel7 = NovelMother.generateNovel(author);
        Novel novel8 = NovelMother.generateNovel(author);
        Chapter chapter1 = ChapterMother.generateChapter(novel1);
        novel1.addChapter(chapter1);
        novel2.addChapter(chapter1);
        novel3.addChapter(chapter1);
        novel4.addChapter(chapter1);
        novel5.addChapter(chapter1);
        novel6.addChapter(chapter1);

        MoreNovelRequest moreNovelRequest1 = MoreNovelRequest.builder()
                .mainNovelType(MainNovelType.UPDATE).nextPage(0).build();
        MoreNovelRequest moreNovelRequest2 = MoreNovelRequest.builder()
                .mainNovelType(MainNovelType.UPDATE).nextPage(1).build();

        //when
        novelRepository.save(novel1);
        novelRepository.save(novel2);
        novelRepository.save(novel3);
        novelRepository.save(novel4);
        novelRepository.save(novel5);
        novelRepository.save(novel6);
        novelRepository.save(novel7);
        novelRepository.save(novel8);
        chapterRepository.save(chapter1);
        MoreNovelResponse moreNovelResponse1 = novelService.getMoreNovels(member, moreNovelRequest1);
        MoreNovelResponse moreNovelResponse2 = novelService.getMoreNovels(member, moreNovelRequest2);

        //then
        moreNovelResponse1.novelList()
                        .forEach(novelInfo ->
                                assertThat(novelInfo.id()).isIn(
                                        novel6.getId(),
                                        novel5.getId(),
                                        novel4.getId(),
                                        novel3.getId(),
                                        novel2.getId(),
                                        novel1.getId()
                                )
                        );
        assertThat(moreNovelResponse1.nextPage()).isOne();
        assertThat(moreNovelResponse1.hasNext()).isTrue();
        assertThat(moreNovelResponse1.novelList().size()).isEqualTo(6);

        assertThat(moreNovelResponse2.nextPage()).isEqualTo(2);
        assertThat(moreNovelResponse2.hasNext()).isFalse();
    }
}
package us.usserver.domain.novel.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.domain.author.AuthorMother;
import us.usserver.domain.author.entity.Author;
import us.usserver.domain.author.entity.ReadNovel;
import us.usserver.domain.author.repository.AuthorRepository;
import us.usserver.domain.chapter.ChapterMother;
import us.usserver.domain.chapter.entity.Chapter;
import us.usserver.domain.chapter.repository.ChapterRepository;
import us.usserver.domain.member.MemberMother;
import us.usserver.domain.member.entity.Member;
import us.usserver.domain.member.repository.MemberRepository;
import us.usserver.domain.novel.NovelMother;
import us.usserver.domain.novel.constant.AgeRating;
import us.usserver.domain.novel.constant.Genre;
import us.usserver.domain.novel.constant.Hashtag;
import us.usserver.domain.novel.constant.NovelSize;
import us.usserver.domain.novel.dto.AuthorDescription;
import us.usserver.domain.novel.dto.MainNovelType;
import us.usserver.domain.novel.dto.NovelDetailInfo;
import us.usserver.domain.novel.dto.NovelInfo;
import us.usserver.domain.novel.dto.req.MoreNovelReq;
import us.usserver.domain.novel.dto.req.NovelBlueprint;
import us.usserver.domain.novel.dto.req.NovelSynopsis;
import us.usserver.domain.novel.dto.req.SearchKeyword;
import us.usserver.domain.novel.dto.res.MainPageRes;
import us.usserver.domain.novel.dto.res.MoreNovelRes;
import us.usserver.domain.novel.dto.res.SearchNovelRes;
import us.usserver.domain.novel.entity.Novel;
import us.usserver.domain.novel.repository.NovelRepository;
import us.usserver.global.response.exception.BaseException;
import us.usserver.global.response.exception.ExceptionMessage;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Rollback
@Transactional
@SpringBootTest
class SearchServiceTest {
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
    private Novel newNovel;
    private Member member;
    private Member newMember;
    private Author author;
    private Author newAuthor;

    @BeforeEach
    void setup() {
        member = MemberMother.generateMember();
        author = AuthorMother.generateAuthorWithMember(member);
        member.setAuthor(author);
        memberRepository.save(member);

        newMember = MemberMother.generateMember();
        newAuthor = AuthorMother.generateAuthorWithMember(newMember);
        newMember.setAuthor(newAuthor);
        memberRepository.save(newMember);

        memberRepository.save(member);
        memberRepository.save(newMember);
        authorRepository.save(author);
        authorRepository.save(newAuthor);

        novel = NovelMother.generateNovel(author);
        newNovel = NovelMother.generateNovel(newAuthor);

        novelRepository.save(novel);
        novelRepository.save(newNovel);
    }

    @Test
    @DisplayName("소설 정보 확인")
    void getNovelInfo() {
        // given
        SearchKeyword searchKeyword = SearchKeyword.builder().keyword("환생 CEO").nextPage(0).build();

        // when
        SearchNovelRes searchNovelRes = novelService.searchNovel(member.getId(), searchKeyword);

        // then
    }

    /*
    @Test
    @DisplayName("소설 검색")
    void 소설검색_제목_성공() {
        //given
        SearchKeyword searchNovelReq1 = SearchKeyword.builder()
                .authorId(author.getId())
                .title("TITLE")
                .hashtag(null)
                .status(null)
                .lastNovelId(0L)
                .size(5)
                .sortDto(new SortDto(Sorts.HIT, Orders.DESC))
                .build();

        SearchKeyword searchNovelReq2 = SearchKeyword.builder()
                .authorId(author.getId())
                .title("TITLE")
                .hashtag(null)
                .status(null)
                .lastNovelId(0L)
                .size(5)
                .sortDto(new SortDto(Sorts.LATEST, Orders.ASC))
                .build();


        //when
        NovelPageInfoRes novelPageInfoResponse1 = novelServiceV0.searchNovel(searchNovelReq1);
        NovelPageInfoRes novelPageInfoResponse2 = novelServiceV0.searchNovel(searchNovelReq2);

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
        SearchKeyword searchNovelReq1 = SearchKeyword.builder()
                .authorId(author.getId())
                .title("TITLE")
                .hashtag(null)
                .status(null)
                .lastNovelId(0L)
                .size(5)
                .sortDto(new SortDto(Sorts.HIT, Orders.DESC))
                .build();

        SearchKeyword searchNovelReq2 = SearchKeyword.builder()
                .authorId(author.getId())
                .title("TITLE2")
                .hashtag(null)
                .status(null)
                .lastNovelId(0L)
                .size(5)
                .sortDto(new SortDto(Sorts.LATEST, Orders.ASC))
                .build();
        SearchKeyword searchNovelReq3 = SearchKeyword.builder()
                .authorId(author.getId())
                .title("TITLE")
                .hashtag(null)
                .status(null)
                .lastNovelId(0L)
                .size(5)
                .sortDto(new SortDto(Sorts.LATEST, Orders.ASC))
                .build();

        //when
        NovelPageInfoRes novelPageInfoResponse1 = novelServiceV0.searchNovel(searchNovelReq1);
        NovelPageInfoRes novelPageInfoResponse2 = novelServiceV0.searchNovel(searchNovelReq2);
        NovelPageInfoRes novelPageInfoResponse3 = novelServiceV0.searchNovel(searchNovelReq3);

        SearchKeywordResponse searchKeywordResponse = novelServiceV0.searchKeyword();

        //then
        assertThat(searchKeywordResponse.getHotSearch().size()).isEqualTo(2);
        assertThat(searchKeywordResponse.getHotSearch().get(0)).isEqualTo("TITLE");
        assertThat(searchKeywordResponse.getHotSearch().get(1)).isEqualTo("TITLE2");

        assertThat(searchKeywordResponse.getRecentSearch().size()).isEqualTo(2);
        assertThat(searchKeywordResponse.getRecentSearch().get(0)).isEqualTo("TITLE");
        assertThat(searchKeywordResponse.getRecentSearch().get(1)).isEqualTo("TITLE2");
    } */
}
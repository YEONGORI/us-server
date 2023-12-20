package us.usserver.novel.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import us.usserver.author.Author;
import us.usserver.author.AuthorRepository;
import us.usserver.global.exception.NovelNotFoundException;
import us.usserver.novel.Novel;
import us.usserver.novel.NovelMother;
import us.usserver.novel.NovelRepository;
import us.usserver.novel.dto.*;
import us.usserver.novel.novelEnum.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NovelServiceV0Test {
    @Autowired
    private NovelRepository novelRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private NovelServiceV0 novelServiceV0;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private Novel novel;
    private Novel novel2;
    private Novel novel3;
    private Novel novel4;
    private Novel novel5;
    private Novel novel6;
    private Novel novel7;
    private Novel novel8;
    private Author author;
    private Author author2;

    @BeforeEach
    void setup() throws InterruptedException {
        Set<Hashtag> hashtags = new HashSet<>();
        hashtags.add(Hashtag.HASHTAG1);
        hashtags.add(Hashtag.HASHTAG2);
        hashtags.add(Hashtag.MONCHKIN);

        author = Author.builder()
                .id(1L)
                .nickname("NICKNAME")
                .introduction("INTRODUCTION")
                .profileImg("PROFILE_IMG")
                .build();
        authorRepository.save(author);

        novel = Novel.builder()
                .id(1L)
                .title("TITLE")
                .thumbnail("THUMBNAIL")
                .synopsis("SYNOPSIS")
                .author(author)
                .authorDescription("AUTHOR_DESCRIPTION")
                .hashtags(hashtags)
                .genre(Genre.FANTASY)
                .ageRating(AgeRating.GENERAL)
                .novelSize(NovelSize.LONG)
                .novelStatus(NovelStatus.IN_PROGRESS)
                .hit(1)
                .build();
        novelRepository.save(novel);

        novel2 = Novel.builder()
                .id(2L)
                .title("TITLE2")
                .thumbnail("THUMBNAIL2")
                .synopsis("SYNOPSIS2")
                .author(author)
                .authorDescription("AUTHOR_DESCRIPTION2")
                .hashtags(hashtags)
                .genre(Genre.MYSTERY)
                .ageRating(AgeRating.TWELVE_PLUS)
                .novelSize(NovelSize.SHORT)
                .novelStatus(NovelStatus.IN_PROGRESS)
                .hit(2)
                .build();
        novelRepository.save(novel2);
        novel3 = Novel.builder()
                .id(3L)
                .title("TITLE3")
                .thumbnail("THUMBNAIL3")
                .synopsis("SYNOPSIS3")
                .author(author)
                .authorDescription("AUTHOR_DESCRIPTION3")
                .hashtags(hashtags)
                .genre(Genre.HISTORICAL)
                .ageRating(AgeRating.GENERAL)
                .novelSize(NovelSize.LONG)
                .novelStatus(NovelStatus.IN_PROGRESS)
                .hit(3)
                .build();
        novelRepository.save(novel3);

        novel4 = Novel.builder()
                .id(4L)
                .title("TITLE4")
                .thumbnail("THUMBNAIL4")
                .synopsis("SYNOPSIS4")
                .author(author)
                .authorDescription("AUTHOR_DESCRIPTION4")
                .hashtags(hashtags)
                .genre(Genre.HISTORICAL)
                .ageRating(AgeRating.GENERAL)
                .novelSize(NovelSize.LONG)
                .novelStatus(NovelStatus.IN_PROGRESS)
                .hit(4)
                .build();
        novelRepository.save(novel4);

        novel5 = Novel.builder()
                .id(5L)
                .title("TITLE5")
                .thumbnail("THUMBNAIL5")
                .synopsis("SYNOPSIS5")
                .author(author)
                .authorDescription("AUTHOR_DESCRIPTION5")
                .hashtags(hashtags)
                .genre(Genre.HISTORICAL)
                .ageRating(AgeRating.GENERAL)
                .novelSize(NovelSize.LONG)
                .novelStatus(NovelStatus.IN_PROGRESS)
                .hit(5)
                .build();
        novelRepository.save(novel5);

        novel6 = Novel.builder()
                .id(6L)
                .title("TITLE6")
                .thumbnail("THUMBNAIL6")
                .synopsis("SYNOPSIS6")
                .author(author)
                .authorDescription("AUTHOR_DESCRIPTION6")
                .hashtags(hashtags)
                .genre(Genre.HISTORICAL)
                .ageRating(AgeRating.GENERAL)
                .novelSize(NovelSize.LONG)
                .novelStatus(NovelStatus.IN_PROGRESS)
                .hit(6)
                .build();
        novelRepository.save(novel6);

        novel7 = Novel.builder()
                .id(7L)
                .title("TITLE7")
                .thumbnail("THUMBNAIL7")
                .synopsis("SYNOPSIS7")
                .author(author)
                .authorDescription("AUTHOR_DESCRIPTION7")
                .hashtags(hashtags)
                .genre(Genre.HISTORICAL)
                .ageRating(AgeRating.GENERAL)
                .novelSize(NovelSize.LONG)
                .novelStatus(NovelStatus.IN_PROGRESS)
                .hit(7)
                .build();
        novelRepository.save(novel7);

        novel8 = Novel.builder()
                .id(8L)
                .title("TITLE8")
                .thumbnail("THUMBNAIL8")
                .synopsis("SYNOPSIS8")
                .author(author)
                .authorDescription("AUTHOR_DESCRIPTION8")
                .hashtags(hashtags)
                .genre(Genre.HISTORICAL)
                .ageRating(AgeRating.GENERAL)
                .novelSize(NovelSize.LONG)
                .novelStatus(NovelStatus.IN_PROGRESS)
                .hit(8)
                .build();
        novelRepository.save(novel8);

        author = Author.builder()
                .id(1L)
                .nickname("NICKNAME")
                .introduction("INTRODUCTION")
                .profileImg("PROFILE_IMG")
                .build();

        List<Novel> readNovels = new ArrayList<>();
        readNovels.add(novel);
        readNovels.add(novel2);

        author2 = Author.builder()
                .id(2L)
                .nickname("NICKNAME2")
                .introduction("INTRODUCTION2")
                .profileImg("PROFILE_IMG")
                .readNovels(readNovels)
                .build();
        authorRepository.save(author2);
    }

    @BeforeEach
    void clear() {
        redisTemplate.keys("*").stream().forEach(k -> {
            redisTemplate.delete(k);
        });
    }

    @Test
    @DisplayName("소설 정보 확인")
    void getNovelInfo() {
        NovelInfoResponse novelInfoResponse = assertDoesNotThrow(
                () -> novelServiceV0.getNovelInfo(1L));

        assertThat(novelInfoResponse.getTitle()).isEqualTo(novel.getTitle());
        assertThat(novelInfoResponse.getCreatedAuthor().getId()).isEqualTo(novel.getAuthor().getId());
        assertThat(novelInfoResponse.getGenre()).isEqualTo(novel.getGenre());
        assertThat(novelInfoResponse.getHashtag()).isEqualTo(novel.getHashtags());

        assertThat(novelInfoResponse.getNovelSharelUrl()).contains("/novel/" + (1L));
        assertThat(novelInfoResponse.getDetailNovelInfoUrl()).contains("novel/" + (1L) + "/detail");
    }

    @Test
    @DisplayName("존재하지 않는 소설 정보 확인")
    void getNotExistNovel() {
        assertThrows(NovelNotFoundException.class,
                () -> novelServiceV0.getNovelInfo(2L));
    }

    @Test
    @DisplayName("소설에 참여한 작가 확인")
    void getNovelJoinedAuthor() {

    }

    @Test
    @DisplayName("소설에 달린 댓글 확인")
    void getNovelComment() {

    }

    @Test
    @DisplayName("소설 상세 정보 확인")
    void getNovelDetailInfo() {
        DetailInfoResponse detailInfoResponse = assertDoesNotThrow(
                () -> novelServiceV0.getNovelDetailInfo(1L));

        assertThat(detailInfoResponse.getTitle()).isEqualTo(novel.getTitle());
        assertThat(detailInfoResponse.getThumbnail()).isEqualTo(novel.getThumbnail());
        assertThat(detailInfoResponse.getSynopsis()).isEqualTo(novel.getSynopsis());
        assertThat(detailInfoResponse.getAuthorName()).isEqualTo(novel.getAuthor().getNickname());
        assertThat(detailInfoResponse.getAuthorIntroduction()).isEqualTo(novel.getAuthorDescription());
        assertThat(detailInfoResponse.getAgeRating()).isEqualTo(novel.getAgeRating());
        assertThat(detailInfoResponse.getGenre()).isEqualTo(novel.getGenre());
        assertThat(detailInfoResponse.getHashtags()).isEqualTo(novel.getHashtags());
        assertThat(detailInfoResponse.getStakeInfos()).isEqualTo(Collections.emptyList());
    }

    @Test
    @DisplayName("소설 생성")
    void 소설생성_성공() {
        //given
        CreateNovelReq createNovelReq = CreateNovelReq.builder()
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
        Novel createNovel = novelServiceV0.createNovel(createNovelReq);

        //then
        assertThat(createNovel).isNotNull();
        assertThat(createNovel.getTitle()).isEqualTo("TEST TITLE");
        assertThat(createNovel.getThumbnail()).isEqualTo("TEST THUMBNAIL");
        assertThat(createNovel.getSynopsis()).isEqualTo("TEST SYNOPSIS");
        assertThat(createNovel.getAuthorDescription()).isEqualTo("TEST AUTHORDESCRIPTION");
        assertThat(createNovel.getHashtags()).isEqualTo(Collections.singleton(Hashtag.HASHTAG1));
        assertThat(createNovel.getGenre()).isEqualTo(Genre.FANTASY);
        assertThat(createNovel.getAgeRating()).isEqualTo(AgeRating.GENERAL);
        assertThat(createNovel.getNovelSize()).isEqualTo(NovelSize.LONG);
    }

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
    }

    @Test
    @DisplayName("메인화면 소설 List")
    @Transactional
    void 메인_소설_성공() {
        //when
        HomeNovelListResponse homeNovelListResponse = novelServiceV0.homeNovelInfo();

        //then
        assertThat(homeNovelListResponse.getRealTimeNovels().size()).isEqualTo(3);
        assertThat(homeNovelListResponse.getRealTimeNovels().get(0).getTitle()).isEqualTo("TITLE3");

        assertThat(homeNovelListResponse.getNewNovels().size()).isEqualTo(3);
        assertThat(homeNovelListResponse.getNewNovels().get(0).getTitle()).isEqualTo("TITLE3");
        assertThat(homeNovelListResponse.getNewNovels().get(1).getTitle()).isEqualTo("TITLE2");

        assertThat(homeNovelListResponse.getReadNovels().size()).isEqualTo(2);
        assertThat(homeNovelListResponse.getReadNovels().get(0).getTitle()).isEqualTo("TITLE");
        assertThat(homeNovelListResponse.getReadNovels().get(1).getTitle()).isEqualTo("TITLE2");
    }

    @Test
    @DisplayName("메인화면 소설 더보기")
    @Transactional
    void 메인_소설_더보기_실시간업데이트_성공() {
        //given
        HomeNovelListResponse homeNovelListResponse = novelServiceV0.homeNovelInfo();
        Novel realTimeNovel = homeNovelListResponse.getRealTimeNovels().get(homeNovelListResponse.getRealTimeNovels().size() - 1);
        MoreInfoOfNovel moreInfoOfNovel = MoreInfoOfNovel.builder()
                .lastNovelId(realTimeNovel.getId())
                .size(3)
                .sortDto(SortDto.builder().sorts(Sorts.LATEST).orders(Orders.DESC).build())
                .build();
        //when
        NovelPageInfoResponse novelPageInfoResponse = novelServiceV0.moreNovel(moreInfoOfNovel);

        //then
        assertThat(novelPageInfoResponse.getNovelList().size()).isEqualTo(2);
        assertThat(novelPageInfoResponse.getHasNext()).isFalse();
        assertThat(novelPageInfoResponse.getSorts()).isEqualTo(Sorts.LATEST);
    }

    @Test
    @DisplayName("메인화면 소설 더보기")
    void 메인_소설_더보기_신작_성공() {
        //given
        HomeNovelListResponse homeNovelListResponse = novelServiceV0.homeNovelInfo();
        Novel newNovel = homeNovelListResponse.getNewNovels().get(homeNovelListResponse.getNewNovels().size() - 1);
        MoreInfoOfNovel moreInfoOfNovel = MoreInfoOfNovel.builder()
                .lastNovelId(newNovel.getId())
                .size(3)
                .sortDto(SortDto.builder().sorts(Sorts.NEW).orders(Orders.DESC).build())
                .build();
        //when
        NovelPageInfoResponse novelPageInfoResponse = novelServiceV0.moreNovel(moreInfoOfNovel);

        //then
        assertThat(novelPageInfoResponse.getNovelList().size()).isEqualTo(2);
        assertThat(novelPageInfoResponse.getHasNext()).isFalse();
        assertThat(novelPageInfoResponse.getSorts()).isEqualTo(Sorts.NEW);
    }

    @Test
    @DisplayName("메인화면 소설 더보기")
    @Transactional
    void 메인_소설_더보기_읽은소설_성공() {
        //given
        ReadInfoOfNovel readInfoOfNovel = ReadInfoOfNovel.builder()
                .getNovelSize(0)
                .size(3)
                .build();
        //when
        NovelPageInfoResponse novelPageInfoResponse = novelServiceV0.readMoreNovel(readInfoOfNovel);

        //then
        assertThat(novelPageInfoResponse.getNovelList().size()).isEqualTo(2);
        assertThat(novelPageInfoResponse.getNovelList().get(0).getId()).isEqualTo(1L);
        assertThat(novelPageInfoResponse.getHasNext()).isFalse();
    }
}
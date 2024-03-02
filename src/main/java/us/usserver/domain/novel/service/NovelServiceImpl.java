package us.usserver.domain.novel.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import us.usserver.domain.author.entity.Author;
import us.usserver.domain.author.repository.AuthorRepository;
import us.usserver.domain.authority.entity.Authority;
import us.usserver.domain.authority.repository.AuthorityRepository;
import us.usserver.domain.chapter.dto.ChapterInfo;
import us.usserver.domain.chapter.service.ChapterService;
import us.usserver.domain.member.entity.Member;
import us.usserver.domain.novel.entity.Novel;
import us.usserver.domain.novel.dto.*;
import us.usserver.domain.novel.constant.Orders;
import us.usserver.domain.novel.constant.Sorts;
import us.usserver.domain.novel.repository.NovelRepository;
import us.usserver.domain.authority.dto.res.StakeInfoResponse;
import us.usserver.domain.authority.dto.StakeInfo;
import us.usserver.domain.authority.service.StakeService;
import us.usserver.global.EntityFacade;
import us.usserver.global.response.exception.ExceptionMessage;
import us.usserver.global.response.exception.AuthorNotFoundException;
import us.usserver.global.response.exception.MainAuthorIsNotMatchedException;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NovelServiceImpl implements NovelService {
    private final EntityFacade entityFacade;
    private final StakeService stakeService;
    private final ChapterService chapterService;

    private final AuthorityRepository authorityRepository;
    private final AuthorRepository authorRepository;
    private final NovelRepository novelRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final Integer RECENT_KEYWORD_SIZE = 10;

    @Override
    public NovelInfo createNovel(Member member, CreateNovelReq createNovelReq) {
        Author author = getAuthor(member);

        Novel novel = createNovelReq.toEntity(author);
        Novel saveNovel = novelRepository.save(novel);

        author.getCreatedNovels().add(novel);
        authorityRepository.save(Authority.builder().author(author).novel(novel).build());

        chapterService.createChapter(saveNovel.getId(), author.getId());
        return NovelInfo.mapNovelToNovelInfo(novel);

//        return NovelInfo.builder() TODO: 리펙토링으로 삭제 예정
//                .title(novel.getTitle())
//                .createdAuthor(AuthorInfo.fromAuthor(author))
//                .genre(novel.getGenre())
//                .hashtag(novel.getHashtags())
//                .joinedAuthorCnt(1)
//                .commentCnt(0)
//                .likeCnt(0)
//                .build();
    }

    @Override
    public NovelInfo getNovelInfo(Long novelId) {
        Novel novel = entityFacade.getNovel(novelId);
        return NovelInfo.mapNovelToNovelInfo(novel);
//        AuthorInfo authorInfo = AuthorInfo.fromAuthor(novel.getMainAuthor()); TODO: 리펙토링으로 삭제 예정
//
//        return NovelInfo.builder()
//                .title(novel.getTitle())
//                .createdAuthor(authorInfo)
//                .genre(novel.getGenre())
//                .hashtag(novel.getHashtags())
//                .joinedAuthorCnt(authorityRepository.countAllByNovel(novel))
//                .commentCnt(commentJpaRepository.countAllByNovel(novel))
//                .likeCnt(novelLikeRepository.countAllByNovel(novel))
//                .novelSharelUrl("http://localhost:8080/novel/" + novel.getId())
//                .build();
    }

    @Override
    public NovelDetailInfo getNovelDetailInfo(Long novelId) {
        Novel novel = entityFacade.getNovel(novelId);
        StakeInfoResponse stakeResponse = stakeService.getStakeInfoOfNovel(novelId);
        List<StakeInfo> stakeInfos = stakeResponse.getStakeInfos();
        List<ChapterInfo> chapterInfos = chapterService.getChaptersOfNovel(novel);

        return NovelDetailInfo.builder()
                .title(novel.getTitle())
                .thumbnail(novel.getThumbnail())
                .synopsis(novel.getSynopsis())
                .authorName(novel.getMainAuthor().getNickname())
                .authorIntroduction(novel.getAuthorDescription())
                .ageRating(novel.getAgeRating())
                .genre(novel.getGenre())
                .hashtags(novel.getHashtags())
                .stakeInfos(stakeInfos)
                .chapterInfos(chapterInfos)
                .build();
    }

    @Override
    public String modifyNovelSynopsis(Long novelId, Long authorId, String synopsis) {
        Novel novel = entityFacade.getNovel(novelId);
        Author author = entityFacade.getAuthor(authorId);

        if (!novel.getMainAuthor().getId().equals(author.getId())) {
            throw new MainAuthorIsNotMatchedException(ExceptionMessage.MAIN_AUTHOR_NOT_MATCHED);
        }

        novel.setSynopsis(synopsis);
        return synopsis;
    }

    @Override
    public AuthorDescription modifyAuthorDescription(Long novelId, Long authorId, AuthorDescription req) {
        Novel novel = entityFacade.getNovel(novelId);
        Author author = entityFacade.getAuthor(authorId);

        if (!novel.getMainAuthor().getId().equals(author.getId())) {
            throw new MainAuthorIsNotMatchedException(ExceptionMessage.MAIN_AUTHOR_NOT_MATCHED);
        }

        novel.setAuthorDescription(req.getDescription());
        return req;
    }

    @Override
    public HomeNovelListResponse homeNovelInfo(Member member) {
        Author author = getAuthor(member);
        MoreInfoOfNovel realTimeNovels = MoreInfoOfNovel.builder()
                .lastNovelId(501L)
                .sortDto(SortDto.builder().sorts(Sorts.LATEST).orders(Orders.DESC).build())
                .build();

        MoreInfoOfNovel newNovels = MoreInfoOfNovel.builder()
                .lastNovelId(501L)
                .sortDto(SortDto.builder().sorts(Sorts.NEW).orders(Orders.DESC).build())
                .build();

        return HomeNovelListResponse.builder()
                .realTimeNovels(mapNovelsToNovelInfos(novelRepository.moreNovelList(realTimeNovels, getPageRequest(realTimeNovels))))
                .newNovels(mapNovelsToNovelInfos(novelRepository.moreNovelList(newNovels, getPageRequest(newNovels))))
                .readNovels((author == null) ? null : author.getViewedNovels()
                        .stream().limit(8)
                        .sorted(Comparator.comparing(Novel::getId))
                        .map(NovelInfo::mapNovelToNovelInfo)
                        .toList())
                .build();
    }

    @Override
    public NovelPageInfoResponse moreNovel(MoreInfoOfNovel moreInfoOfNovel) {
        PageRequest pageable = getPageRequest(moreInfoOfNovel);
        Slice<Novel> novelSlice = novelRepository.moreNovelList(moreInfoOfNovel, pageable);

        return getNovelPageInfoResponse(novelSlice, moreInfoOfNovel.getSortDto());
    }
    @Override
    public NovelPageInfoResponse readMoreNovel(Member member, ReadInfoOfNovel readInfoOfNovel){
        Author author = getAuthor(member);

        if (author != null) {
            int author_readNovel_cnt = author.getViewedNovels().size();
            int getSize = readInfoOfNovel.getGetNovelSize() + readInfoOfNovel.getSize();
            int endPoint = Math.min(author_readNovel_cnt, getSize);

            List<Novel> novelList = author
                    .getViewedNovels()
                    .stream()
                    .sorted(Comparator.comparing(Novel::getId))
                    .toList()
                    .subList(readInfoOfNovel.getGetNovelSize(), endPoint);
            boolean hasNext = getSize == endPoint;

            return NovelPageInfoResponse.builder()
                    .novelList(novelList.stream().map(NovelInfo::mapNovelToNovelInfo).toList())
                    .lastNovelId(novelList.get(novelList.size()-1).getId())
                    .hasNext(hasNext)
                    .sorts(null)
                    .build();
        } else {
            return null;
        }
    }

    private PageRequest getPageRequest(MoreInfoOfNovel moreInfoOfNovel) {
        if (moreInfoOfNovel.getSize() == null) {
            moreInfoOfNovel.setSize(6);
        }
        return PageRequest.ofSize(moreInfoOfNovel.getSize());
    }

    private NovelPageInfoResponse getNovelPageInfoResponse(Slice<Novel> novelSlice, SortDto novelMoreDto) {
        Long newLastNovelId = getLastNovelId(novelSlice);

        return NovelPageInfoResponse
                .builder()
                .novelList(mapNovelsToNovelInfos(novelSlice))
                .lastNovelId(newLastNovelId)
                .hasNext(novelSlice.hasNext())
                .sorts(novelMoreDto.getSorts())
                .build();
    }

    private Long getLastNovelId(Slice<Novel> novelSlice){
        return novelSlice.isEmpty() ? null : novelSlice.getContent().get(novelSlice.getNumberOfElements() - 1).getId();
    }

    @Override
    public NovelPageInfoResponse searchNovel(Member member, SearchNovelReq searchNovelReq) {
        Author author = getAuthor(member);
        PageRequest pageable = PageRequest.ofSize(searchNovelReq.getSize());
        if (searchNovelReq.getTitle() != null && searchNovelReq.getLastNovelId() == 0L) {
            increaseKeywordScore(searchNovelReq.getTitle());
            recentKeyword((author == null) ? null : author.getId(), searchNovelReq.getTitle());
        }
        Slice<Novel> novelSlice = novelRepository.searchNovelList(searchNovelReq, pageable);

        return getNovelPageInfoResponse(novelSlice, searchNovelReq.getSortDto());
    }

    @Override
    public SearchKeywordResponse searchKeyword(Member member) {
        Author author = getAuthor(member);

        //최신 검색어
        ListOperations<String, String> opsForList = redisTemplate.opsForList();
        ZSetOperations<String, String> opsForZSet = redisTemplate.opsForZSet();

        //인기 검색어
        String hot_keyword = "ranking";
        Set<ZSetOperations.TypedTuple<String>> rankingTuples = opsForZSet.reverseRangeWithScores(hot_keyword, 0, 9);

        return SearchKeywordResponse.builder()
                .recentSearch(opsForList.range(String.valueOf(author.getId()), 0, 9))
                .hotSearch(rankingTuples.stream().map(set -> set.getValue()).collect(Collectors.toList()))
                .build();
    }

    @Override
    public void deleteSearchKeyword(Member member) {
        Author author = getAuthor(member);

        String key = String.valueOf(author.getId());
        Long size = redisTemplate.opsForList().size(key);

        redisTemplate.opsForList().rightPop(key, size);
    }

    private void increaseKeywordScore(String keyword) {
        int score = 0;

        try {
            redisTemplate.opsForZSet().incrementScore("ranking", keyword,1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        redisTemplate.opsForZSet().incrementScore("ranking", keyword, score);
    }

    private void recentKeyword(Long authorId, String keyword) {
        if (authorId == null) {
            return;
        }

        String key = String.valueOf(authorId);
        String equalWord = null;
        ListOperations<String, String> list = redisTemplate.opsForList();

        for (int i = 0; i < list.size(key); i++) {
            String frontWord = list.leftPop(key);
            if (frontWord.equals(keyword)) {
                equalWord = frontWord;
            } else{
                list.rightPush(key, frontWord);
            }
        }
        if (equalWord != null) {
            list.leftPush(key, equalWord);
            return;
        }

        Long size = list.size(key);
        if (size == (long) RECENT_KEYWORD_SIZE) {
            list.rightPop(key);
        }
        list.leftPush(key, keyword);
    }

    private Author getAuthor(Member member) {
        if (member == null) {
            return null;
        }
        return authorRepository.getAuthorByMemberId(member.getId()).orElseThrow(() -> new AuthorNotFoundException(ExceptionMessage.AUTHOR_NOT_FOUND));
    }

    private List<NovelInfo> mapNovelsToNovelInfos(Slice<Novel> novels) {
        return novels.getContent()
                .stream().map(NovelInfo::mapNovelToNovelInfo)
                .toList();
    }
}

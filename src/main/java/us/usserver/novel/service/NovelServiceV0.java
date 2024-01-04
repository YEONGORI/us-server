package us.usserver.novel.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import us.usserver.author.Author;
import us.usserver.author.AuthorRepository;
import us.usserver.authority.Authority;
import us.usserver.authority.AuthorityRepository;
import us.usserver.chapter.ChapterService;
import us.usserver.chapter.dto.ChapterInfo;
import us.usserver.comment.novel.NoCommentRepository;
import us.usserver.global.EntityService;
import us.usserver.global.ExceptionMessage;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.global.exception.MainAuthorIsNotMatchedException;
import us.usserver.author.AuthorRepository;
import us.usserver.global.EntityService;
import us.usserver.global.ExceptionMessage;
import us.usserver.authority.AuthorityRepository;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.novel.Novel;
import us.usserver.novel.NovelRepository;
import us.usserver.novel.NovelService;
import us.usserver.novel.dto.*;
import us.usserver.novel.novelEnum.Orders;
import us.usserver.novel.novelEnum.Sorts;
import us.usserver.novel.repository.NovelCustomRepository;
import us.usserver.stake.StakeService;
import us.usserver.novel.dto.*;
import us.usserver.comment.novel.NoCommentRepository;
import us.usserver.novel.novelEnum.Orders;
import us.usserver.novel.novelEnum.Sorts;
import us.usserver.novel.repository.NovelCustomRepository;
import us.usserver.stake.StakeRepository;
import us.usserver.stake.dto.StakeInfo;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NovelServiceV0 implements NovelService {
    private final EntityService entityService;
    private final StakeService stakeService;
    private final ChapterService chapterService;

    private final AuthorityRepository authorityRepository;
    private final NoCommentRepository noCommentRepository;
    private final AuthorRepository authorRepository;
    private final NovelRepository novelRepository;
    private final NovelCustomRepository novelCustomRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final Integer RECENT_KEYWORD_SIZE = 10;

    @Override
    public Novel createNovel(CreateNovelReq createNovelReq) {
        //TODO: 토큰 값 변경 예정
        Long authorId = 1L;
        Author author = authorRepository.findById(authorId).orElseThrow(() -> new AuthorNotFoundException(ExceptionMessage.Author_NOT_FOUND));

        Novel novel = createNovelReq.toEntity(author);
        Novel saveNovel = novelRepository.save(novel);

        author.getCreatedNovels().add(novel);
        authorityRepository.save(Authority.builder().author(author).novel(novel).build());

        return saveNovel;
    }

    @Override
    public NovelInfo getNovelInfo(Long novelId) {
        Novel novel = entityService.getNovel(novelId);

        // TODO : url 은 상의가 좀 필요함
        return NovelInfo.builder()
                .title(novel.getTitle())
                .createdAuthor(novel.getMainAuthor())
                .genre(novel.getGenre())
                .hashtag(novel.getHashtags())
                .joinedAuthorCnt(authorityRepository.countAllByNovel(novel))
                .commentCnt(noCommentRepository.countAllByNovel(novel))
                .novelSharelUrl("http://localhost:8080/novel/" + novel.getId())
                .build();
    }

    @Override
    public NovelDetailInfo getNovelDetailInfo(Long novelId) {
        Novel novel = entityService.getNovel(novelId);
        List<StakeInfo> stakeInfos = stakeService.getStakeInfoOfNovel(novelId);
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
    public NovelSynopsis modifyNovelSynopsis(Long novelId, Long authorId, NovelSynopsis req) {
        Novel novel = entityService.getNovel(novelId);
        Author author = entityService.getAuthor(authorId);

        if (!novel.getMainAuthor().getId().equals(author.getId())) {
            throw new MainAuthorIsNotMatchedException(ExceptionMessage.Main_Author_NOT_MATCHED);
        }

        novel.setSynopsis(req.getSynopsis());
        return req;
    }

    @Override
    public AuthorDescription modifyAuthorDescription(Long novelId, Long authorId, AuthorDescription req) {
        Novel novel = entityService.getNovel(novelId);
        Author author = entityService.getAuthor(authorId);

        if (!novel.getMainAuthor().getId().equals(author.getId())) {
            throw new MainAuthorIsNotMatchedException(ExceptionMessage.Main_Author_NOT_MATCHED);
        }

        novel.setAuthorDescription(req.getDescription());
        return req;
    }

    @Transactional
    @Override
    //TODO: authorId 임시
    public HomeNovelListResponse homeNovelInfo() {
        Long authorId = 2L;
        Author author = authorRepository.findById(authorId).orElseThrow(() -> new AuthorNotFoundException(ExceptionMessage.Author_NOT_FOUND));
        MoreInfoOfNovel realTimeNovels = MoreInfoOfNovel.builder()
                .sortDto(SortDto.builder().sorts(Sorts.LATEST).orders(Orders.DESC).build())
                .build();

        MoreInfoOfNovel newNovels = MoreInfoOfNovel.builder()
                .sortDto(SortDto.builder().sorts(Sorts.NEW).orders(Orders.DESC).build())
                .build();

        return HomeNovelListResponse.builder()
                .realTimeNovels(novelCustomRepository.moreNovelList(realTimeNovels, getPageRequest(realTimeNovels)).toList())
                .newNovels(novelCustomRepository.moreNovelList(newNovels, getPageRequest(newNovels)).toList())
                .readNovels(author.getViewedNovels()
                        .stream()
                        .limit(8)
                        .sorted(Comparator.comparing(Novel::getId))
                        .toList())
                .build();
    }
    
    @Override
    public NovelPageInfoResponse moreNovel(MoreInfoOfNovel moreInfoOfNovel) {

        PageRequest pageable = getPageRequest(moreInfoOfNovel);
        Slice<Novel> novelSlice = novelCustomRepository.moreNovelList(moreInfoOfNovel, pageable);

        return getNovelPageInfoResponse(novelSlice, moreInfoOfNovel.getSortDto());
    }
    @Override
    public NovelPageInfoResponse readMoreNovel(ReadInfoOfNovel readInfoOfNovel){
        Long authorId = 2L;
        Optional<Author> findAuthorId = authorRepository.findById(authorId);

        if (!findAuthorId.isEmpty()) {
            int author_readNovel_cnt = findAuthorId.get().getViewedNovels().size();
            int getSize = readInfoOfNovel.getGetNovelSize() + readInfoOfNovel.getSize();
            int endPoint = author_readNovel_cnt < getSize ? author_readNovel_cnt : getSize;

            List<Novel> novelList = findAuthorId.get()
                    .getViewedNovels()
                    .stream()
                    .sorted(Comparator.comparing(Novel::getId))
                    .toList()
                    .subList(readInfoOfNovel.getGetNovelSize(), endPoint);
            boolean hasNext = (getSize == endPoint) ? true : false;

            return NovelPageInfoResponse.builder()
                    .novelList(novelList)
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
                .novelList(novelSlice.getContent())
                .lastNovelId(newLastNovelId)
                .hasNext(novelSlice.hasNext())
                .sorts(novelMoreDto.getSorts())
                .build();
    }

    private Long getLastNovelId(Slice<Novel> novelSlice){
        Long id = novelSlice.isEmpty() ? null : novelSlice.getContent().get(novelSlice.getNumberOfElements() - 1).getId();
        return id;
    }

    @Override
    public NovelPageInfoResponse searchNovel(SearchNovelReq searchNovelReq) {
        PageRequest pageable = PageRequest.ofSize(searchNovelReq.getSize());
        if (searchNovelReq.getTitle() != null && searchNovelReq.getLastNovelId() == 0L) {
            increaseKeywordScore(searchNovelReq.getTitle());
            recentKeyword(searchNovelReq.getAuthorId(), searchNovelReq.getTitle());
        }
        Slice<Novel> novelSlice = novelCustomRepository.searchNovelList(searchNovelReq, pageable);

        return getNovelPageInfoResponse(novelSlice, searchNovelReq.getSortDto());
    }

    @Override
    public SearchKeywordResponse searchKeyword() {
        //Token 값으로 변경
        Long authorId = 1L;

        //최신 검색어
        ListOperations<String, String> opsForList = redisTemplate.opsForList();
        ZSetOperations<String, String> opsForZSet = redisTemplate.opsForZSet();

        //인기 검색어
        String hot_keyword = "ranking";
        Set<ZSetOperations.TypedTuple<String>> rankingTuples = opsForZSet.reverseRangeWithScores(hot_keyword, 0, 9);

        return SearchKeywordResponse.builder()
                .recentSearch(opsForList.range(String.valueOf(authorId), 0, 9))
                .hotSearch(rankingTuples.stream().map(set -> set.getValue()).collect(Collectors.toList()))
                .build();
    }

    @Override
    public void deleteSearchKeyword() {
        //TODO memberId 토큰에서 꺼내올 예정
        Long authorId = 1L;
        String key = String.valueOf(authorId);
        Long size = redisTemplate.opsForList().size(key);

        redisTemplate.opsForList().rightPop(key, size);
    }

    private void increaseKeywordScore(String keyword) {
        Integer score = 0;

        try {
            redisTemplate.opsForZSet().incrementScore("ranking", keyword,1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        redisTemplate.opsForZSet().incrementScore("ranking", keyword, score);
    }

    private void recentKeyword(Long authorId, String keyword) {
        //security 도입 후 Long memberId -> 현재 로그인 중인 MemberId
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
}

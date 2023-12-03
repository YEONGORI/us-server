package us.usserver.novel.service;

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
import us.usserver.global.EntityService;
import us.usserver.global.ExceptionMessage;
import us.usserver.authority.AuthorityRepository;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.novel.Novel;
import us.usserver.novel.NovelRepository;
import us.usserver.novel.NovelService;
import us.usserver.novel.dto.*;
import us.usserver.comment.novel.NoCommentRepository;
import us.usserver.novel.repository.NovelCustomRepository;
import us.usserver.stake.StakeRepository;
import us.usserver.stake.dto.StakeInfo;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NovelServiceV0 implements NovelService {
    private final EntityService entityService;
    private final AuthorityRepository authorityRepository;
    private final NoCommentRepository noCommentRepository;
    private final StakeRepository stakeRepository;
    private final NovelRepository novelRepository;
    private final AuthorRepository authorRepository;
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

        return saveNovel;
    }

    @Override
    public NovelInfoResponse getNovelInfo(Long novelId) {
        Novel novel = entityService.getNovel(novelId);

        // TODO : url 은 상의가 좀 필요함
        return NovelInfoResponse.builder()
                .title(novel.getTitle())
                .createdAuthor(novel.getAuthor())
                .genre(novel.getGenre())
                .hashtag(novel.getHashtag())
                .joinedAuthorCnt(authorityRepository.countAllByNovel(novel))
                .commentCnt(noCommentRepository.countAllByNovel(novel))
                .novelSharelUrl("http://localhost:8080/novel/" + novel.getId())
                .detailNovelInfoUrl("http://localhost:8080/novel/" + novel.getId() + "/detail")
                .build();
    }

    @Override
    public DetailInfoResponse getNovelDetailInfo(Long novelId) {
        Novel novel = entityService.getNovel(novelId);
        List<StakeInfo> stakeInfos = stakeRepository.findAllByNovel(novel);

        return DetailInfoResponse.builder()
                .title(novel.getTitle())
                .thumbnail(novel.getThumbnail())
                .synopsis(novel.getSynopsis())
                .authorName(novel.getAuthor().getNickname())
                .authorIntroduction(novel.getAuthorDescription())
                .ageRating(novel.getAgeRating())
                .genre(novel.getGenre())
                .hashtags(novel.getHashtag())
                .stakeInfos(stakeInfos)
                .build();
    }

    @Override
    //TODO: authorId 임시
    public HomeNovelListResponse homeNovelInfo(Long authorId) {
        Author author = authorRepository.findById(authorId).orElseThrow(() -> new AuthorNotFoundException(ExceptionMessage.Author_NOT_FOUND));

        HomeNovelListResponse homeInfoResponse = HomeNovelListResponse.builder()
                .realTimeNovels(novelRepository.getRealTimeNovels())
                .newNovels(novelRepository.getNewNovels())
                .readNovels(author.getReadNovels()
                        .stream()
                        .sorted(Comparator.comparing(Novel::getTitle))
                        .toList()
                        .subList(0, 8))
                .build();
        return homeInfoResponse;
    }

    @Override
    public NovelPageInfoResponse moreNovel(MoreInfoOfNovel novelMoreDto) {
        PageRequest pageable = PageRequest.ofSize(novelMoreDto.getSize());
        Slice<Novel> novelSlice = novelCustomRepository.moreNovelList(novelMoreDto, pageable);

        return getNovelPageInfoResponse(novelSlice, novelMoreDto.getSortDto());
    }

    private NovelPageInfoResponse getNovelPageInfoResponse(Slice<Novel> novelSlice, SortDto novelMoreDto) {
        Long newLastNovelId = getLastNovelId(novelSlice);

        return NovelPageInfoResponse
                .builder()
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
        Long memberId = 1L;

        //최신 검색어
        ListOperations<String, String> opsForList = redisTemplate.opsForList();
        ZSetOperations<String, String> opsForZSet = redisTemplate.opsForZSet();

        //인기 검색어
        String hot_keyword = "ranking";
        Set<ZSetOperations.TypedTuple<String>> rankingTuples = opsForZSet.reverseRangeWithScores(hot_keyword, 0, 9);

        return SearchKeywordResponse.builder()
                .recentSearch(opsForList.range(String.valueOf(memberId), 0, 9))
                .hotSearch(rankingTuples.stream().map(set -> String.valueOf(set)).collect(Collectors.toList()))
                .build();
    }

    @Override
    public void deleteSearchKeyword() {
        //TODO memberId 토큰에서 꺼내올 예정
        Long memberId = 1L;
        String key = String.valueOf(memberId);
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

    private void recentKeyword(Long memberId, String keyword) {
        //security 도입 후 Long memberId -> 현재 로그인 중인 MemberId
        String key = String.valueOf(memberId);

        Long size = redisTemplate.opsForList().size(key);
        if (size == (long) RECENT_KEYWORD_SIZE) {
            redisTemplate.opsForList().rightPop(key);
        }
        redisTemplate.opsForList().leftPush(key, keyword);
    }
}

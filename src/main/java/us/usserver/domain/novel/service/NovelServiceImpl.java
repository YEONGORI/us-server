package us.usserver.domain.novel.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import us.usserver.domain.author.entity.Author;
import us.usserver.domain.author.entity.ReadNovel;
import us.usserver.domain.author.repository.AuthorRepository;
import us.usserver.domain.authority.dto.StakeInfo;
import us.usserver.domain.authority.dto.res.StakeInfoResponse;
import us.usserver.domain.authority.entity.Authority;
import us.usserver.domain.authority.repository.AuthorityRepository;
import us.usserver.domain.authority.service.StakeService;
import us.usserver.domain.chapter.dto.ChapterInfo;
import us.usserver.domain.chapter.service.ChapterService;
import us.usserver.domain.member.entity.Member;
import us.usserver.domain.novel.dto.*;
import us.usserver.domain.novel.entity.Novel;
import us.usserver.domain.novel.repository.NovelRepository;
import us.usserver.global.EntityFacade;
import us.usserver.global.ExceptionMessage;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.global.exception.MainAuthorIsNotMatchedException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
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
    private static final Integer DEFAULT_PAGE_SIZE = 6;

    @Override
    @Transactional
    public NovelInfo createNovel(Member member, NovelBlueprint novelBlueprint) {
        Author author = authorRepository.getAuthorByMember(member)
                .orElseThrow(() -> new AuthorNotFoundException(ExceptionMessage.AUTHOR_NOT_FOUND));

        Novel novel = novelBlueprint.toEntity(author);
        Novel saveNovel = novelRepository.save(novel);

        authorityRepository.save(Authority.builder()
                .author(author).novel(novel).build());

        chapterService.createChapter(saveNovel.getId(), author.getId());
        return NovelInfo.mapNovelToNovelInfo(novel);
    }

    @Override
    @Transactional
    public NovelInfo getNovelInfo(Long novelId) {
        Novel novel = entityFacade.getNovel(novelId);
        return NovelInfo.mapNovelToNovelInfo(novel);
    }

    @Override
    @Transactional
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
    @Transactional
    public String modifyNovelSynopsis(Long novelId, Long authorId, String synopsis) {
        Novel novel = entityFacade.getNovel(novelId);
        Author author = entityFacade.getAuthor(authorId);

        if (!novel.getMainAuthor().getId().equals(author.getId())) {
            throw new MainAuthorIsNotMatchedException(ExceptionMessage.MAIN_AUTHOR_NOT_MATCHED);
        }

        novel.changeSynopsis(synopsis);
        return synopsis;
    }

    @Override
    @Transactional
    public AuthorDescription modifyAuthorDescription(Long novelId, Long authorId, AuthorDescription req) {
        Novel novel = entityFacade.getNovel(novelId);
        Author author = entityFacade.getAuthor(authorId);

        if (!novel.getMainAuthor().getId().equals(author.getId())) {
            throw new MainAuthorIsNotMatchedException(ExceptionMessage.MAIN_AUTHOR_NOT_MATCHED);
        }

        novel.changeAuthorDescription(req.getDescription());
        return req;
    }

    @Override
    @Transactional
    public MainPageResponse getMainPage(Member member) {
        List<NovelInfo> readNovels = getReadNovels(member);

        PageRequest realTimeUpdates = getPageRequest(0, DEFAULT_PAGE_SIZE, Sort.Direction.DESC, SortColumn.recentlyUpdated);
        PageRequest recentlyCreated = getPageRequest(0, DEFAULT_PAGE_SIZE, Sort.Direction.DESC, SortColumn.createdAt);
        PageRequest popular = getPageRequest(0, 3, Sort.Direction.DESC, SortColumn.hit);

        List<NovelInfo> realTimeUpdatesNovels = novelRepository.findSliceBy(realTimeUpdates)
                .map(NovelInfo::mapNovelToNovelInfo).toList();
        List<NovelInfo> recentlyCreatedNovels = novelRepository.findSliceBy(recentlyCreated)
                .map(NovelInfo::mapNovelToNovelInfo).toList();
        List<NovelInfo> popularNovels = novelRepository.findSliceBy(popular)
                .map(NovelInfo::mapNovelToNovelInfo).toList();

        return new MainPageResponse(popularNovels, readNovels, realTimeUpdatesNovels, recentlyCreatedNovels);
    }

    @Override
    @Transactional
    public MoreNovelResponse getMoreNovels(Member member, MoreNovelRequest moreNovelRequest) {
        PageRequest pageRequest = switch (moreNovelRequest.mainNovelType()) {
            case NEW -> getPageRequest(moreNovelRequest.nextPage(), DEFAULT_PAGE_SIZE, Sort.Direction.DESC, SortColumn.createdAt);
            case UPDATE -> getPageRequest(moreNovelRequest.nextPage(), DEFAULT_PAGE_SIZE, Sort.Direction.DESC, SortColumn.recentlyUpdated);
            case POPULAR -> getPageRequest(moreNovelRequest.nextPage(), DEFAULT_PAGE_SIZE, Sort.Direction.DESC, SortColumn.hit);
        };

        Slice<Novel> novelSlice = novelRepository.findSliceBy(pageRequest);
        List<NovelInfo> novelInfos = novelSlice
                .map(NovelInfo::mapNovelToNovelInfo)
                .toList();
        return new MoreNovelResponse(novelInfos, novelSlice.getNumber() + 1, novelSlice.hasNext());
    }


    @Override
    @Transactional
    public MoreNovelResponse readMoreNovel(Member member){
        Author author = authorRepository.getAuthorByMember(member)
                .orElseThrow(() -> new AuthorNotFoundException(ExceptionMessage.AUTHOR_NOT_FOUND));

        List<NovelInfo> novelInfos = author.getReadNovels().stream()
                .sorted(Comparator.comparing(ReadNovel::getReadDate).reversed())
                .map(ReadNovel::getNovel)
                .map(NovelInfo::mapNovelToNovelInfo)
                .toList();

        return new MoreNovelResponse(novelInfos, 0, Boolean.FALSE);
    }

    private PageRequest getPageRequest(int pageNum, int pageSize, Sort.Direction direction, SortColumn sortColumn) {
        return PageRequest.of(pageNum, pageSize, Sort.by(direction, sortColumn.toString()));
    }

    private List<NovelInfo> getReadNovels(Member member) {
        Author author = authorRepository.getAuthorByMember(member)
                .orElseThrow(() -> new AuthorNotFoundException(ExceptionMessage.AUTHOR_NOT_FOUND));

        return author.getReadNovels().stream()
                .sorted(Comparator.comparing(ReadNovel::getReadDate).reversed())
                .limit(6)
                .map(ReadNovel::getNovel)
                .map(NovelInfo::mapNovelToNovelInfo)
                .toList();
    }

    @Override
    @Transactional
    public NovelPageInfoResponse searchNovel(Member member, SearchNovelReq searchNovelReq) {
        Optional<Author> authorByMember = authorRepository.getAuthorByMember(member);

        PageRequest pageable = PageRequest.ofSize(searchNovelReq.getSize());
//        if (searchNovelReq.getTitle() != null && searchNovelReq.getLastNovelId() == 0L) {
//            increaseKeywordScore(searchNovelReq.getTitle());
//            recentKeyword((author == null) ? null : author.getId(), searchNovelReq.getTitle());
//        }
        Slice<Novel> novelSlice = novelRepository.searchNovelList(searchNovelReq, pageable);

        return getNovelPageInfoResponse(novelSlice, searchNovelReq.getSortDto());
    }

    @Override
    @Transactional
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
    @Transactional
    public void deleteSearchKeyword(Member member) {
        Author author = authorRepository.getAuthorByMember(member)
                .orElseThrow(() -> new AuthorNotFoundException(ExceptionMessage.AUTHOR_NOT_FOUND));

        String key = String.valueOf(author.getId());
        Long size = redisTemplate.opsForList().size(key);

        redisTemplate.opsForList().rightPop(key, size);
    }

    private NovelPageInfoResponse getNovelPageInfoResponse(Slice<Novel> novelSlice, SortDto novelMoreDto) {
        Long newLastNovelId = getLastNovelId(novelSlice);
//        novelSlice.getNumber()

        return NovelPageInfoResponse
                .builder()
                .novelList(mapNovelsToNovelInfos(novelSlice))
//                .lastNovelId(newLastNovelId)
                .hasNext(novelSlice.hasNext())
                .sorts(novelMoreDto.getSorts())
                .build();
    }

    private Long getLastNovelId(Slice<Novel> novelSlice){
        return novelSlice.isEmpty() ? null : novelSlice.getContent().get(novelSlice.getNumberOfElements() - 1).getId();
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
        return authorRepository.getAuthorByMember(member)
                .orElseThrow(() -> new AuthorNotFoundException(ExceptionMessage.AUTHOR_NOT_FOUND));
    }

    private List<NovelInfo> mapNovelsToNovelInfos(Slice<Novel> novels) {
        return novels.getContent()
                .stream().map(NovelInfo::mapNovelToNovelInfo)
                .toList();
    }
}

package us.usserver.domain.novel.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import us.usserver.domain.novel.dto.NovelSimpleInfo;
import us.usserver.domain.novel.dto.SortColumn;
import us.usserver.domain.novel.dto.req.SearchKeyword;
import us.usserver.domain.novel.dto.res.SearchNovelRes;
import us.usserver.domain.novel.dto.res.SearchPageRes;
import us.usserver.domain.novel.entity.Novel;
import us.usserver.domain.novel.repository.NovelRepository;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final NovelRepository novelRepository;
    private final KomoranService komoranService;

    private static final Integer DEFAULT_PAGE_SIZE = 6;

    @Override
    @Transactional
    public SearchNovelRes searchNovel(Long memberId, SearchKeyword searchKeyword) {
        PageRequest pageRequest = getPageRequest(searchKeyword.nextPage());

        Set<String> keywords = komoranService.tokenizeKeyword(searchKeyword.keyword());

        Slice<Novel> novelSlice = novelRepository.searchNovelList(keywords, pageRequest);
        Set<NovelSimpleInfo> novelSimpleInfos = novelSlice.map(NovelSimpleInfo::mapNovelToSimpleInfo).toSet();
        return new SearchNovelRes(novelSimpleInfos, novelSlice.getNumber() + 1, novelSlice.hasNext());
    }

    private PageRequest getPageRequest(int pageNum) {
        return PageRequest.of(
                pageNum,
                SearchServiceImpl.DEFAULT_PAGE_SIZE,
                Sort.by(Sort.Direction.DESC,SortColumn.createdAt.toString()));
    }

    @Override
    @Transactional
    public SearchPageRes getSearchPage(Long memberId) {
//        Author author = member.getAuthor();
//
//        //최신 검색어
//        ListOperations<String, String> opsForList = redisTemplate.opsForList();
//        ZSetOperations<String, String> opsForZSet = redisTemplate.opsForZSet();
//
//        //인기 검색어
//        String hot_keyword = "ranking";
//        Set<ZSetOperations.TypedTuple<String>> rankingTuples = opsForZSet.reverseRangeWithScores(hot_keyword, 0, 9);
//
//        return SearchPageRes.builder()
//                .recentSearch(opsForList.range(String.valueOf(author.getId()), 0, 9))
//                .hotSearch(rankingTuples.stream().map(set -> set.getValue()).collect(Collectors.toList()))
//                .build();
        return null;
    }

    @Override
    @Transactional
    public void deleteSearchKeyword(Long memberId) {
//        Author author = authorRepository.getAuthorByMember(member)
//                .orElseThrow(() -> new BaseException(ErrorCode.AUTHOR_NOT_FOUND));
//
//        String key = String.valueOf(author.getId());
//        Long size = redisTemplate.opsForList().size(key);
//
//        redisTemplate.opsForList().rightPop(key, size);
    }

//    private void increaseKeywordScore(String keyword) {
//        int score = 0;
//
//        try {
//            redisTemplate.opsForZSet().incrementScore("ranking", keyword,1);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        redisTemplate.opsForZSet().incrementScore("ranking", keyword, score);
//    }
//
//    private void recentKeyword(Long authorId, String keyword) {
//        if (authorId == null) {
//            return;
//        }
//
//        String key = String.valueOf(authorId);
//        String equalWord = null;
//        ListOperations<String, String> list = redisTemplate.opsForList();
//
//        for (int i = 0; i < list.size(key); i++) {
//            String frontWord = list.leftPop(key);
//            if (frontWord.equals(keyword)) {
//                equalWord = frontWord;
//            } else{
//                list.rightPush(key, frontWord);
//            }
//        }
//        if (equalWord != null) {
//            list.leftPush(key, equalWord);
//            return;
//        }
//
//        Long size = list.size(key);
//        if (size == (long) RECENT_KEYWORD_SIZE) {
//            list.rightPop(key);
//        }
//        list.leftPush(key, keyword);
//    }
}

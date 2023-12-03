package us.usserver.novel.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
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
}

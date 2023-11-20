package us.usserver.novel.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.usserver.global.ExceptionMessage;
import us.usserver.authority.AuthorityRepository;
import us.usserver.novel.Novel;
import us.usserver.novel.NovelRepository;
import us.usserver.novel.NovelService;
import us.usserver.novel.dto.DetailInfoResponse;
import us.usserver.novel.dto.NovelInfoResponse;
import us.usserver.global.exception.NovelNotFoundException;
import us.usserver.comment.novel.NoCommentRepository;
import us.usserver.stake.StakeRepository;
import us.usserver.stake.dto.StakeInfo;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NovelServiceV0 implements NovelService {
    private final NovelRepository novelRepository;
    private final AuthorityRepository authorityRepository;
    private final NoCommentRepository noCommentRepository;
    private final StakeRepository stakeRepository;

    @Override
    public NovelInfoResponse getNovelInfo(Long novelId) {
        Optional<Novel> novelById = novelRepository.getNovelById(novelId);
        if (novelById.isEmpty()) {
            throw new NovelNotFoundException(ExceptionMessage.Novel_NOT_FOUND);
        }
        Novel novel = novelById.get();

        // TODO : url 은 상의가 좀 필요함
        return NovelInfoResponse.builder()
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
        Optional<Novel> novelById = novelRepository.getNovelById(novelId);
        if (novelById.isEmpty()) {
            log.info(ExceptionMessage.Novel_NOT_FOUND);
            throw new NovelNotFoundException(ExceptionMessage.Novel_NOT_FOUND);
        }

        Novel novel = novelById.get();
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
}

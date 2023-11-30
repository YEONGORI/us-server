package us.usserver.novel.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.usserver.author.Author;
import us.usserver.global.EntityService;
import us.usserver.global.ExceptionMessage;
import us.usserver.authority.AuthorityRepository;
import us.usserver.global.exception.MainAuthorIsNotMatchedException;
import us.usserver.novel.Novel;
import us.usserver.novel.NovelService;
import us.usserver.novel.dto.AuthorDescription;
import us.usserver.novel.dto.DetailInfoResponse;
import us.usserver.novel.dto.NovelInfoResponse;
import us.usserver.comment.novel.NoCommentRepository;
import us.usserver.novel.dto.NovelSynopsis;
import us.usserver.stake.StakeRepository;
import us.usserver.stake.dto.StakeInfo;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NovelServiceV0 implements NovelService {
    private final EntityService entityService;
    private final AuthorityRepository authorityRepository;
    private final NoCommentRepository noCommentRepository;
    private final StakeRepository stakeRepository;

    @Override
    public NovelInfoResponse getNovelInfo(Long novelId) {
        Novel novel = entityService.getNovel(novelId);

        // TODO : url 은 상의가 좀 필요함
        return NovelInfoResponse.builder()
                .title(novel.getTitle())
                .createdAuthor(novel.getMainAuthor())
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
        List<StakeInfo> stakeInfos = stakeRepository.findAllByNovel(novel).stream();

        return DetailInfoResponse.builder()
                .title(novel.getTitle())
                .thumbnail(novel.getThumbnail())
                .synopsis(novel.getSynopsis())
                .authorName(novel.getMainAuthor().getNickname())
                .authorIntroduction(novel.getAuthorDescription())
                .ageRating(novel.getAgeRating())
                .genre(novel.getGenre())
                .hashtags(novel.getHashtag())
                .stakeInfos(stakeInfos)
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

}

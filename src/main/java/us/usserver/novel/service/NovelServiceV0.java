package us.usserver.novel.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.usserver.author.Author;
import us.usserver.author.AuthorRepository;
import us.usserver.global.EntityService;
import us.usserver.global.ExceptionMessage;
import us.usserver.authority.AuthorityRepository;
import us.usserver.global.exception.MemberNotFoundException;
import us.usserver.novel.Novel;
import us.usserver.novel.NovelRepository;
import us.usserver.novel.NovelService;
import us.usserver.novel.dto.DetailInfoResponse;
import us.usserver.novel.dto.NovelCreateDto;
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
    private final EntityService entityService;
    private final AuthorityRepository authorityRepository;
    private final NoCommentRepository noCommentRepository;
    private final StakeRepository stakeRepository;
    private final NovelRepository novelRepository;
    private final AuthorRepository authorRepository;

    @Override
    public Novel createNovel(NovelCreateDto novelCreateDto) {
        //TODO: 토큰 값 변경 예정
        Long authorId = 1L;
        Author author = authorRepository.findById(authorId).orElseThrow(() -> new MemberNotFoundException(ExceptionMessage.Member_NOT_FOUND));

        Novel novel = novelCreateDto.toEntity(author);
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
}

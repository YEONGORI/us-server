package us.usserver.novel.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.usserver.novel.Novel;
import us.usserver.novel.NovelRepository;
import us.usserver.novel.NovelService;
import us.usserver.novel.dto.NovelInfoResponse;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NovelServiceV0 implements NovelService {
    private final NovelRepository novelRepository;

    @Override
    public NovelInfoResponse getNovelInfo(Long novelId) {
        Optional<Novel> novelById = novelRepository.getNovelById(novelId);
        if (novelById.isEmpty()) {
            log.info("Novel for this id does not exist.");
            return null;
        }
        Novel novel = novelById.get();

        return NovelInfoResponse.builder()
                .createdAuthor(novel.getAuthor())
                .genre(novel.getGenre())
                .hashtag(novel.getHashtag())
                .joinedAuthorCnt()
                .commentCnt()
                .novelUrlForShare("")
                .detailNovelInfoUrl("")
                .build();
    }
}

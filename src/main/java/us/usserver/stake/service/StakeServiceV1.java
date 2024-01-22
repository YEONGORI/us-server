package us.usserver.stake.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.usserver.author.Author;
import us.usserver.authority.Authority;
import us.usserver.authority.AuthorityRepository;
import us.usserver.chapter.Chapter;
import us.usserver.global.EntityService;
import us.usserver.novel.Novel;
import us.usserver.stake.Stake;
import us.usserver.stake.StakeRepository;
import us.usserver.stake.StakeService;
import us.usserver.stake.dto.GetStakeResponse;
import us.usserver.stake.dto.StakeInfo;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StakeServiceV1 implements StakeService {
    private final StakeRepository stakeRepository;
    private final AuthorityRepository authorityRepository;
    private final EntityService entityService;

    @Override
    public void setStakeInfoOfNovel(Novel novel) {
        List<Chapter> chapters = novel.getChapters();
        float totalParagraphs = (float) getTotalParagraphs(chapters);

        List<Authority> authorities = authorityRepository.findAllByNovel(novel);
        Set<Author> authors = authorities.stream().map(Authority::getAuthor).collect(Collectors.toSet());

        for (Author author : authors) {
            float authorParagraphs = (float) getAuthorParagraphs(chapters, author);
            updateStake(novel, author, authorParagraphs / totalParagraphs);
        }
    }

    @Override
    public GetStakeResponse getStakeInfoOfNovel(Long novelId) {
        Novel novel = entityService.getNovel(novelId);

        List<Stake> stakes = stakeRepository.findAllByNovel(novel);
        List<StakeInfo> stakeInfos = stakes.stream().map(StakeInfo::fromStake).toList();

        return GetStakeResponse.builder().stakeInfos(stakeInfos).build();
    }

    private int getTotalParagraphs(List<Chapter> chapters) {
        return chapters.stream()
                .mapToInt(chapter -> chapter.getParagraphs().size())
                .sum();
    }

    private int getAuthorParagraphs(List<Chapter> chapters, Author author) {
        return (int) chapters.stream()
                .flatMap(chapter -> chapter.getParagraphs().stream())
                .filter(paragraph -> paragraph.getAuthor().getId().equals(author.getId()))
                .count();
    }

    private void updateStake(Novel novel, Author author, Float percentage) {
        Optional<Stake> stakeByNovelAndAuthor = stakeRepository.findByNovelAndAuthor(novel, author);
        if (stakeByNovelAndAuthor.isEmpty()) {
            stakeRepository.save(Stake.builder()
                    .novel(novel)
                    .author(author)
                    .percentage(percentage)
                    .build());
        } else {
            Stake stake = stakeByNovelAndAuthor.get();
            stake.setPercentage(percentage);
        }
    }
}


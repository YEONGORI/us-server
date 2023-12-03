package us.usserver.stake.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.usserver.author.Author;
import us.usserver.chapter.Chapter;
import us.usserver.novel.Novel;
import us.usserver.stake.Stake;
import us.usserver.stake.StakeRepository;
import us.usserver.stake.StakeService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StakeServiceV0 implements StakeService {
    private final StakeRepository stakeRepository;

    private static final float ZERO = 0.0f;

    @Override
    public void setStakeInfoOfNovel(Novel novel, Author author) {
        List<Chapter> chapters = novel.getChapters();
        int totalParagraphs = getTotalParagraphs(chapters);
        int authorParagraphs = getAuthorParagraphs(chapters, author);

        Float percentage = (totalParagraphs == 0) ? ZERO : authorParagraphs / (float)totalParagraphs;
        updateStake(novel, author, percentage);
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


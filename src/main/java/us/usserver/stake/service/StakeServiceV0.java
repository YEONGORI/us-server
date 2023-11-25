package us.usserver.stake.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.usserver.author.Author;
import us.usserver.novel.Novel;
import us.usserver.stake.StakeService;

@Slf4j
@Service
@RequiredArgsConstructor
public class StakeServiceV0 implements StakeService {


    @Override
    public void setStakeInfoOfNovel(Novel novel, Author author) {
        int sum = novel.getChapters().stream().mapToInt(chapter -> chapter.getParagraphs().size()).sum();
        novel.getChapters().stream().map(chapter ->
                chapter.getParagraphs().stream().map(paragraph ->
                        paragraph.getAuthor().
                )
        )
    }
}

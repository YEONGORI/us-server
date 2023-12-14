package us.usserver.paragraph;

import org.springframework.stereotype.Service;
import us.usserver.paragraph.dto.ParagraphsOfChapter;
import us.usserver.paragraph.dto.ParagraphInVoting;
import us.usserver.paragraph.dto.PostParagraphReq;

import java.util.List;

@Service
public interface ParagraphService {
    ParagraphsOfChapter getParagraphs(Long authorId, Long chapterId);

    List<ParagraphInVoting> getInVotingParagraphs(Long chapterId);

    ParagraphInVoting postParagraph(Long authorId, Long chapterId, PostParagraphReq req);

    void selectParagraph(Long authorId, Long novelId, Long chapterId, Long paragraphId);
}

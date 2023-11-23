package us.usserver.paragraph;

import org.springframework.stereotype.Service;
import us.usserver.paragraph.dto.ParagraphInfo;
import us.usserver.paragraph.dto.ParagraphUnSelected;
import us.usserver.paragraph.dto.PostParagraphReq;

@Service
public interface ParagraphService {
    ParagraphInfo getParagraphs(Long chapterId);

    ParagraphUnSelected postParagraph(Long authorId, Long chapterId, PostParagraphReq req);

    void selectParagraph(Long authorId, Long novelId, Long chapterId, Long paragraphId);
}

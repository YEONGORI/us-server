package us.usserver.paragraph;

import org.springframework.stereotype.Service;
import us.usserver.paragraph.dto.ParagraphInfo;
import us.usserver.paragraph.dto.ParagraphUnSelected;
import us.usserver.paragraph.dto.ParagraphsInfo;
import us.usserver.paragraph.dto.PostParagraphReq;

import java.util.List;

@Service
public interface ParagraphService {
    // TODO: 1. 이미 작성이 완료된 챕터의 한줄들을 가져오기
    //       2. 작성이 완료되지 않은 챕터의 한줄들을 가져오기

    ParagraphsInfo getParagraphs(Long authorId, Long chapterId);

    List<ParagraphInfo> getRegisteredParagraphs(Long authorId, Long chapterId);

    ParagraphUnSelected postParagraph(Long authorId, Long chapterId, PostParagraphReq req);

    void selectParagraph(Long authorId, Long novelId, Long chapterId, Long paragraphId);
}

package us.usserver.domain.paragraph.service;

import org.springframework.stereotype.Service;
import us.usserver.domain.member.entity.Member;
import us.usserver.domain.paragraph.dto.res.GetParagraphResponse;
import us.usserver.domain.paragraph.dto.req.PostParagraphReq;
import us.usserver.domain.paragraph.dto.ParagraphsOfChapter;
import us.usserver.domain.paragraph.dto.ParagraphInVoting;

@Service
public interface ParagraphService {
    ParagraphsOfChapter getParagraphs(Long authorId, Long chapterId);

    GetParagraphResponse getInVotingParagraphs(Member member, Long chapterId);

    ParagraphInVoting postParagraph(Long authorId, Long chapterId, PostParagraphReq req);

    void selectParagraph(Long authorId, Long novelId, Long chapterId, Long paragraphId);

    void reportParagraph(Long authorId, Long paragraphId);
}

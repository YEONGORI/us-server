package us.usserver.domain.member.service;

import org.springframework.stereotype.Service;
import us.usserver.domain.member.dto.UpdateAuthorReq;

@Service
public interface AuthorService {
    void updateAuthor(Long memberId, UpdateAuthorReq updateAuthorReq);

    void changeFontSize(Long authorId, int fontSize);

    void changeParagraphSpace(Long authorId, int paragraphSpace);
}

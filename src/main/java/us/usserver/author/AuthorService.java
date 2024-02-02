package us.usserver.author;

import org.springframework.stereotype.Service;
import us.usserver.author.dto.UpdateAuthorReq;

@Service
public interface AuthorService {
    void updateAuthor(Long memberId, UpdateAuthorReq updateAuthorReq);

    void changeFontSize(Long authorId, int fontSize);

    void changeParagraphSpace(Long authorId, int paragraphSpace);
}

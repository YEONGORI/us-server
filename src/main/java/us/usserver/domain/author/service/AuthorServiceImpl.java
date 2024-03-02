package us.usserver.domain.author.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.domain.author.entity.Author;
import us.usserver.domain.author.repository.AuthorRepository;
import us.usserver.domain.author.dto.req.UpdateAuthorReq;
import us.usserver.global.EntityFacade;
import us.usserver.global.response.exception.ExceptionMessage;
import us.usserver.global.response.exception.AuthorNotFoundException;
import us.usserver.global.response.exception.FontSizeOutOfRangeException;
import us.usserver.global.response.exception.ParagraphSpaceOutOfRangeException;

@Transactional
@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final EntityFacade entityFacade;
    private final AuthorRepository authorRepository;
    @Override
    public void updateAuthor(Long memberId, UpdateAuthorReq updateAuthorReq) {
        Author author = authorRepository.getAuthorByMemberId(memberId).orElseThrow(() -> new AuthorNotFoundException(ExceptionMessage.AUTHOR_NOT_FOUND));

        if (updateAuthorReq.getNickname() != null) {
            author.changeNickname(updateAuthorReq.getNickname());
        }
        if (updateAuthorReq.getIntroduction() != null) {
            author.changeIntroduction(updateAuthorReq.getIntroduction());
        }
        if (updateAuthorReq.getProfileImg() != null) {
            author.changeProfileImg(updateAuthorReq.getProfileImg());
        }
        if (updateAuthorReq.getCollectionNovelsPublic() != null) {
            author.setCollectionNovelsPublic(updateAuthorReq.getCollectionNovelsPublic());
        }
        if (updateAuthorReq.getParticipateNovelsPublic() != null) {
            author.setParticipateNovelsPublic(updateAuthorReq.getParticipateNovelsPublic());
        }
    }

    @Override
    public void changeFontSize(Long authorId, int fontSize) {
        if (fontSize < 1 || fontSize > 30) {
            throw new FontSizeOutOfRangeException(ExceptionMessage.FONT_SIZE_OUT_OF_RANGE);
        }
        Author author = entityFacade.getAuthor(authorId);
        author.changeFontSize(fontSize);
    }

    @Override
    public void changeParagraphSpace(Long authorId, int paragraphSpace) {
        if (paragraphSpace < 1 || paragraphSpace > 30) {
            throw new ParagraphSpaceOutOfRangeException(ExceptionMessage.PARAGRAPH_SPACE_OUT_OF_RANGE);
        }
        Author author = entityFacade.getAuthor(authorId);
        author.changeParagraphSpace(paragraphSpace);
    }
}

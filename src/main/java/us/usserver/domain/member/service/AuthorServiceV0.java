package us.usserver.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.domain.member.entity.Author;
import us.usserver.domain.member.repository.AuthorRepository;
import us.usserver.domain.member.dto.UpdateAuthorReq;
import us.usserver.global.EntityService;
import us.usserver.global.ExceptionMessage;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.global.exception.FontSizeOutOfRangeException;
import us.usserver.global.exception.ParagraphSpaceOutOfRangeException;

@Transactional
@RequiredArgsConstructor
@Service
public class AuthorServiceV0 implements AuthorService {
    private final EntityService entityService;
    private final AuthorRepository authorRepository;
    @Override
    public void updateAuthor(Long memberId, UpdateAuthorReq updateAuthorReq) {
        Author author = authorRepository.getAuthorByMemberId(memberId).orElseThrow(() -> new AuthorNotFoundException(ExceptionMessage.AUTHOR_NOT_FOUND));

        if (updateAuthorReq.getNickname() != null) {
            author.setNickname(updateAuthorReq.getNickname());
        }
        if (updateAuthorReq.getIntroduction() != null) {
            author.setIntroduction(updateAuthorReq.getIntroduction());
        }
        if (updateAuthorReq.getProfileImg() != null) {
            author.setProfileImg(updateAuthorReq.getProfileImg());
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
        Author author = entityService.getAuthor(authorId);
        author.setFontSize(fontSize);
    }

    @Override
    public void changeParagraphSpace(Long authorId, int paragraphSpace) {
        if (paragraphSpace < 1 || paragraphSpace > 30) {
            throw new ParagraphSpaceOutOfRangeException(ExceptionMessage.PARAGRAPH_SPACE_OUT_OF_RANGE);
        }
        Author author = entityService.getAuthor(authorId);
        author.setParagraphSpace(paragraphSpace);
    }
}

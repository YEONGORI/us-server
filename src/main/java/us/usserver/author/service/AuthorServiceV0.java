package us.usserver.author.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.author.Author;
import us.usserver.author.AuthorRepository;
import us.usserver.author.AuthorService;
import us.usserver.author.dto.UpdateAuthorReq;
import us.usserver.global.ExceptionMessage;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.novel.Novel;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class AuthorServiceV0 implements AuthorService {
    private final AuthorRepository authorRepository;
    @Override
    public void updateAuthor(Long memberId, UpdateAuthorReq updateAuthorReq) {
        Author author = authorRepository.getAuthorByMemberId(memberId).orElseThrow(() -> new AuthorNotFoundException(ExceptionMessage.Author_NOT_FOUND));

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
}

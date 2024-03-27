package us.usserver.domain.member.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.usserver.domain.author.entity.Author;
import us.usserver.domain.author.repository.AuthorRepository;
import us.usserver.domain.member.dto.token.TokenType;
import us.usserver.domain.member.entity.Member;
import us.usserver.domain.member.repository.MemberRepository;
import us.usserver.global.response.exception.BaseException;
import us.usserver.global.response.exception.ErrorCode;
import us.usserver.global.utils.RedisUtils;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MemberServiceImpl implements MemberService {
    private final RedisUtils redisUtils;
    private final MemberRepository memberRepository;
    private final AuthorRepository authorRepository;
    private final TokenProvider tokenProvider;

    @Override
    public void logout(String accessToken, String refreshToken) {
        DecodedJWT decodedJWT = tokenProvider.decodeJWT(accessToken, refreshToken);
        String memberId = decodedJWT.getClaim("id").asString();

        if (redisUtils.getData(memberId) != null)
            redisUtils.deleteData(memberId);
    }

    @Override
    public void withdraw(Member member) {
        Author author = authorRepository.getAuthorByMemberId(member.getId())
                .orElseThrow(() -> new BaseException(ErrorCode.AUTHOR_NOT_FOUND));
        authorRepository.deleteById(author.getId());
        memberRepository.deleteById(member.getId());
    }
}

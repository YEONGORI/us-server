package us.usserver.member;

import org.springframework.data.jpa.repository.JpaRepository;
import us.usserver.global.oauth.oauthEnum.OauthProvider;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByOauthProviderAndSocialId(OauthProvider oauthProvider, String socialId);

    Optional<Member> getMemberById(Long memberId);

    Optional<Member> findBySocialId(String socialId);
}

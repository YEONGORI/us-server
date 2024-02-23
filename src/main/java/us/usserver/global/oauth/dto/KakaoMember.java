package us.usserver.global.oauth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import us.usserver.global.oauth.oauthEnum.OauthProvider;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoMember implements OauthMember{

    @JsonProperty("id")
    private String id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class KakaoAccount {
        private Profile profile;
        private String email;

        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public class Profile{
            private String nickname;
        }
    }

    @Override
    public String getSocialId() {
        return id;
    }

    @Override
    public String getEmail() {
        return kakaoAccount.email;
    }

    @Override
    public String getNickname() {
        return kakaoAccount.profile.nickname;
    }

    @Override
    public OauthProvider getOauthProvider() {
        return OauthProvider.KAKAO;
    }
}

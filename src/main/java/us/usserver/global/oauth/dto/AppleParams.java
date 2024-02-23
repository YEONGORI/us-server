package us.usserver.global.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import us.usserver.global.oauth.oauthEnum.OauthProvider;

@Getter
@AllArgsConstructor
public class AppleParams implements OauthParams{
    private String authorizationCode;

    @Override
    public OauthProvider oauthProvider() {
        return OauthProvider.APPLE;
    }

    @Override
    public String getAuthorizationCode() {
        return authorizationCode;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        return body;
    }
}

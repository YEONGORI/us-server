package us.usserver.global.oauth.dto;

import org.springframework.util.MultiValueMap;
import us.usserver.global.oauth.oauthEnum.OauthProvider;

public interface OauthParams {
    public OauthProvider oauthProvider();
    public String getAuthorizationCode();
    public MultiValueMap<String, String> makeBody();
}

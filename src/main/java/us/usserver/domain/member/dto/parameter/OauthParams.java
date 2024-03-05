package us.usserver.domain.member.dto.parameter;

import org.springframework.util.MultiValueMap;
import us.usserver.domain.member.constant.OauthProvider;

public interface OauthParams {
    public OauthProvider oauthProvider();
    public String getAuthorizationCode();
    public MultiValueMap<String, String> makeBody();
}

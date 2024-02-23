package us.usserver.global.oauth.client;

import us.usserver.global.oauth.dto.OauthMember;
import us.usserver.global.oauth.dto.OauthParams;
import us.usserver.global.oauth.oauthEnum.OauthProvider;

public interface OauthClient {
    public OauthProvider oauthProvider();

    public String getOauthLoginToken(OauthParams oauthParams);

    public OauthMember getMemberInfo(String accessToken);
}

package us.usserver.global.oauth.dto;


import us.usserver.global.oauth.oauthEnum.OauthProvider;

public interface OauthMember {
    public String getSocialId();

    public String getEmail();

    public String getNickname();

    public OauthProvider getOauthProvider();
}

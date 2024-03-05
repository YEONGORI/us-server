package us.usserver.domain.member.dto.member;


import us.usserver.domain.member.constant.OauthProvider;

public interface OauthMember {
    public String getSocialId();

    public String getEmail();

    public String getNickname();

    public OauthProvider getOauthProvider();
}

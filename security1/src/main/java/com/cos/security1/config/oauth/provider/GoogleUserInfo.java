package com.cos.security1.config.oauth.provider;

import java.util.Map;

public class GoogleUserInfo implements OAuth2UserInfo{


    private Map<String, Object> attributes; //OAuth2User의 attributes

    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return this.attributes.get("sub").toString();
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getEmail() {
        return this.attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return this.attributes.get("name").toString();
    }
}

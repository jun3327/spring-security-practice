package com.cos.security1.config.oauth.provider;

//oauth2user의 attributes에서 provider 값의 map 키가 서비스 제공자(구글이나 페이스북) 마다 다르기 때문에
//인터페이스 하나 생성해서 각각 구현
public interface OAuth2UserInfo {

    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();

}

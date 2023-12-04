package com.cos.security1.config.oauth;


import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    //구글로부터 받은 userRequest 데이터에 대한 후처리(액세스 토큰 발급, 사용자정보 받기)를 해주는 메서드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        System.out.println("userRequest getAccessToken = " + userRequest.getAccessToken().getTokenValue());

        //registrationId로 어떤 oauth로 로그인했는지 확인 가능.
        System.out.println("userRequest getClientRegistration = " + userRequest.getClientRegistration());

        //구글로그인버튼클릭 -> 구글로그인창 -> 로그인 완료 -> oauth client 라이브러리에 code 리턴 -> Access Token 요청 ==> userRequest 정보
        //userRequest 정보 -> 회원 프로필 받아야 됨(loadUser함수 호출) -> 구글로부터 회원 프로필 받기
        //아래 getAttributes() 에서 sub이 구글에서 식별되는 고유 번호.
        System.out.println("userRequest getAccessToken = " + super.loadUser(userRequest).getAttributes());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        return super.loadUser(userRequest);
    }
}

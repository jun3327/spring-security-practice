package com.cos.security1.config.oauth;


import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.config.oauth.provider.FacebookUserInfo;
import com.cos.security1.config.oauth.provider.GoogleUserInfo;
import com.cos.security1.config.oauth.provider.OAuth2UserInfo;
import com.cos.security1.domain.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    //구글로부터 받은 userRequest 데이터에 대한 후처리(액세스 토큰 발급, 사용자정보 받기)를 해주는 메서드
    // 메소드 종료 시 컨트롤러의 @AuthenticationPrincipal이 만들어짐.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        System.out.println("userRequest getAccessToken = " + userRequest.getAccessToken().getTokenValue());

        //registrationId로 어떤 oauth로 로그인했는지 확인 가능.
        System.out.println("userRequest getClientRegistration = " + userRequest.getClientRegistration());

        //구글로그인버튼클릭 -> 구글로그인창 -> 로그인 완료 -> oauth client 라이브러리에 code 리턴 -> Access Token 요청 ==> userRequest 정보
        //userRequest 정보 -> 회원 프로필 받아야 됨(loadUser함수 호출) -> 구글로부터 회원 프로필 받기
        OAuth2User oAuth2User = super.loadUser(userRequest);

        //아래 getAttributes() 에서 sub이 구글에서 식별되는 고유 번호.
        //oAuth2User.getAttributes()의 결과는 아래와 같음(예시)
        //oAuth2User1 = {sub=1067785750038111111, name=김땡떙, given_name=땡떙, family_name=김, picture=https://lh3.googleusercontent.com/a/ACg8ocJaSiBWIht4_Suutf3WERhvsaDEIHD, email=ssss@gmail.com, email_verified=true, locale=ko}
        System.out.println("userRequest getAccessToken = " + oAuth2User.getAttributes());

        //OAuth2 서비스 제공자에 따른 분기
        OAuth2UserInfo oAuth2UserInfo = null;
        switch (userRequest.getClientRegistration().getRegistrationId()) {
            case "google":
                oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
                break;
            case "facebook":
                oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
                break;
            default:
                throw new IllegalArgumentException("구글하고 페이스북만 지원해요");
        }

        //oAuth2UserInfo를 이용한 나머지 로직 수행
        //회원 가입 강제 진행
        String provider = oAuth2UserInfo.getProvider(); // ex)google
        String providerId = oAuth2UserInfo.getProviderId(); //ex) 1067785750038111111
        String username = provider + "_" + providerId; //provider와 providerId를 조합(중복 불가)
        String password = bCryptPasswordEncoder.encode("겟인데어"); //password는 Oauth라 크게 의미 없다.
        String email = oAuth2UserInfo.getEmail(); // ex) ssss@gmail.com
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        }


        //아래 PrincipalDetails 객체가 Authenticaion 객체 안으로 들어가고 시큐리티 세션에 저장된다.
        //지금은 Oauth2 과정이기 때문에, PrncipalDetails 안에 User 정보와 Oauth2User의 attributes 정보가 들어간다.
        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}

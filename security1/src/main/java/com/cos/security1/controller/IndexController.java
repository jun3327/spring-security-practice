package com.cos.security1.controller;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.domain.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //Authenticaion 객체를 받아서 유저 정보를 얻을수도 있고, @AuthenticationPrincipal 어노테이션을 통해서 받을 수도 있다.
    //아래 메소드는 일반 로그인의 경우고, oauth2는 따로 만들어줘야됨 -> DefaultOauth2User 객체가 캐스팅이 안되기 때문
    @GetMapping("/test/login")
    public @ResponseBody String testLogin(Authentication authentication,
                                          @AuthenticationPrincipal PrincipalDetails principalDetails2) { //AUthentication은 DI로 주입, 만약 로그인 안한상태로 위 url 접속시 Authenticaion NUll 오류 발생
        PrincipalDetails principalDetails1 = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("principalDetails1 = " + principalDetails1.getUser());
        System.out.println("principalDetails2 = " + principalDetails2.getUser());

        return "세션 정보 확인하기";
    }

    //일반 로그인과 마찬가지로, 아래 Authentication 객체를 사용하거나, 어노테이션을 사용해서 받을 수 있다.
    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOauthLogin(Authentication authentication,
                                               @AuthenticationPrincipal OAuth2User oAuth2User2) { //AUthentication은 DI로 주입, 만약 로그인 안한상태로 위 url 접속시 Authenticaion NUll 오류 발생
        OAuth2User oAuth2User1 = (OAuth2User) authentication.getPrincipal();
        System.out.println("oAuth2User1 = " + oAuth2User1.getAttributes());
        System.out.println("oAuth2User2 = " + oAuth2User2.getAttributes());

        return "Oauth 세션 정보 확인하기";
    }
    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }

    // PrincipalDetails가 UserDetails와 Oauth2User을 모두 구현하기 때문에,
    // 일반 로그인을 하던, Oauth2 로그인을 하던, 모두 PrincipalDetails로 받을 수 있다.
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("principalDetails = " + principalDetails.getUser());
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user) {
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword); //비번 암호화
        user.setPassword(encPassword);
        userRepository.save(user);
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }

    //권한 여러개 걸고 싶으면, 아래와 같이 @PreAuthorize 사용
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터 정보";
    }
}

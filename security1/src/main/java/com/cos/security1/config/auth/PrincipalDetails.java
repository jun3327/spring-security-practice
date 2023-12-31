package com.cos.security1.config.auth;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
// 로그인 과정이 완료되면 시큐리티 session을 Security ContextHolder에 만들어 준다.
// 시큐리티 세션에 들어갈 수 있는 정보, 오브젝트는 Authentication 타입의 객체여야 한다.
// Authentication 안에 User 정보가 있어야 됨.
// User 객체 타입 --> UserDetails 객체 타입 이어야 함.

// 정리하면, Security Session 영역에 들어갈 수 있는 정보는 Authentication 타입이어야 된다.
// 근데 Authentication 안에 User 정보는 UserDetails(PrincipalDetails) 타입이어야 된다.

import com.cos.security1.domain.User;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

//UserDetails, OAuth2User를 모두 상속하여 구현한 PrincipalDetails 객체를 Authentication 객체 안에 들어가도록 구현한다.
//위 주석과 관련해서 중요한점.txt에 메모해 두었다.
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user;
    private Map<String, Object> attributes;

    //일반 로그인 생성자
    public PrincipalDetails(User user) {
        this.user = user;
    }

    //OAuth 로그인 생성자
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    //해당 유저의 권한을 return
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add((GrantedAuthority) () -> user.getRole());
        //collect.add(new GrantedAuthority() {
        //    @Override
        //    public String getAuthority() {
        //        return user.getRole();
        //    }
        //});
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 예를 들어서 회원이 1년 동안 로그인을 안하면 휴면 계정으로 전환해야한다고 가정.
        // 그 접속 안한 시간을 넘어가면 아래 return을 false로 하는 식으로 동작할 수 있음.

        return true;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }
}

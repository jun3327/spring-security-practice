package com.cos.jwt.auth;

import com.cos.jwt.entity.User;
import com.cos.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// http://localhost:8080/login을 통해 로그인을 안함(disable 해놔서) 따라서 아래 서비스가 동작하기 위한
// 필터를 하나 만들어야 된다(JwtAuthenticationFilter)
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername실행");
        User userEntity = userRepository.findByUsername(username);
        System.out.println("userEntity = " + userEntity);
        return new PrincipalDetails(userEntity);
    }
}

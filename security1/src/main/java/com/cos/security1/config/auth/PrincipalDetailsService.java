package com.cos.security1.config.auth;

import com.cos.security1.domain.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl("/login") 해놓음.
// /login 요청이 오면 UserDetailsService 타입으로 IoC에 있는 아래 구현체의 loadUserByUsername 메소드 실행
// loadUserByUsername 의 username 인자는, 클라이언티에서 입력해서 들어오는 username에 해당.
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // 'username' 형식을 잘 지켜야 된다.
    // return 되는 PrincipalDetails(userEntity)는 Authentication 안에 들어간다.
    // 그리고 Authentication은 시큐리티 session에 들어감. -> 로그인 끝
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username);
        if(userEntity != null) {
            return new PrincipalDetails(userEntity);
        }

        return null;
     }
}

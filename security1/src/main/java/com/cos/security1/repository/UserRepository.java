package com.cos.security1.repository;

import com.cos.security1.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

//spring data jpa interface
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);
}

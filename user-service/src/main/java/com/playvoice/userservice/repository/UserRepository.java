package com.playvoice.userservice.repository;

import com.playvoice.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);                     // 로그인용
    boolean existByUsername(String username);                           // 중복검사
}

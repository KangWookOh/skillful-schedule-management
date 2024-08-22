package com.sparta.schedulemanagement.Repository;

import com.sparta.schedulemanagement.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일 중복 체크
    boolean existsByEmail(String email);
    // 이메일을 기준으로 유저를 찾는 메서드
    Optional<User> findByEmail(String email);

    // 유저 이름을 기준으로 유저를 찾는 메서드
    Optional<User> findByUserName(String userName);
}

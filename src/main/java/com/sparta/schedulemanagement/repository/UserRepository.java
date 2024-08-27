package com.sparta.schedulemanagement.repository;

import com.sparta.schedulemanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    default User findByIdOrElseThrow(Long uid) {
        return findById(uid).orElseThrow(()-> new NoSuchElementException("유저를 찾을 수 없습니다"));
    }

    // 이메일을 기준으로 유저를 찾는 메서드
    Optional<User> findByEmail(String email);

}

package com.sparta.schedulemanagement.Repository;

import com.sparta.schedulemanagement.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserName(String username);
    boolean existsByEmail(String email);
}

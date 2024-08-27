package com.sparta.schedulemanagement.repository;

import com.sparta.schedulemanagement.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.NoSuchElementException;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    default Reply findByIdOrElseThrow(Long rid) {
        return findById(rid).orElseThrow(()-> new NoSuchElementException("댓글을 찾을 수 없습니다."));
    }
}

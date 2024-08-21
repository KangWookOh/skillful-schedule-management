package com.sparta.schedulemanagement.Repository;

import com.sparta.schedulemanagement.Entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT s FROM Schedule s")
    Page<Schedule> findAll(Pageable pageable);
}

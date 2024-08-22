package com.sparta.schedulemanagement.Controller;

import com.sparta.schedulemanagement.Config.Util.JwtUtil;
import com.sparta.schedulemanagement.Dto.Schedule.SchedulePageResponseDto;
import com.sparta.schedulemanagement.Dto.Schedule.ScheduleRequestDto;
import com.sparta.schedulemanagement.Dto.Schedule.ScheduleResponseDto;
import com.sparta.schedulemanagement.Entity.Schedule;
import com.sparta.schedulemanagement.Service.Schedule.ScheduleServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
@Slf4j
public class ScheduleController {

    private final ScheduleServiceImpl scheduleService;


    @PostMapping(value="/register", produces="application/json")
   public ResponseEntity<ScheduleResponseDto> register(@RequestBody ScheduleRequestDto scheduleRequestDto) {
        ScheduleResponseDto scheduleResponseDto = scheduleService.createSchedule(scheduleRequestDto);
       return ResponseEntity.ok().body(scheduleResponseDto);
    }

    @GetMapping("/{sid}")
    public ResponseEntity<ScheduleResponseDto> getSchedule(@PathVariable Long sid){
        Optional<ScheduleResponseDto> schedule = scheduleService.getScheduleById(sid);
        return schedule.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<SchedulePageResponseDto>> getSchedules( @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size){
       Page<SchedulePageResponseDto> schedule = scheduleService.getAllSchedules(page,size);
       return ResponseEntity.ok(schedule);
    }

    @PutMapping("/update/{sid}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable Long sid, @RequestBody ScheduleRequestDto scheduleRequestDto){
        ScheduleResponseDto updateSchedule = scheduleService.updateSchedule(sid, scheduleRequestDto);
        return ResponseEntity.ok(updateSchedule);
    }

    @DeleteMapping("/delete/{sid}")
    public ResponseEntity<ScheduleResponseDto> deleteSchedule(@PathVariable Long sid){
       scheduleService.deleteSchedule(sid);
       return ResponseEntity.noContent().build();
    }




}

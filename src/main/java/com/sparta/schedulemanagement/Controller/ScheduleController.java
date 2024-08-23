package com.sparta.schedulemanagement.Controller;

import com.sparta.schedulemanagement.Dto.Schedule.SchedulePageResponseDto;
import com.sparta.schedulemanagement.Dto.Schedule.ScheduleRequestDto;
import com.sparta.schedulemanagement.Dto.Schedule.ScheduleResponseDto;
import com.sparta.schedulemanagement.Service.Schedule.ScheduleServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
@Slf4j
public class ScheduleController {

    private final ScheduleServiceImpl scheduleService;

    /**
     * 새로운 일정을 등록합니다.
     *
     * @param scheduleRequestDto 일정 등록 요청 DTO
     * @return 생성된 일정 응답 DTO와 HTTP 상태 코드 200 (OK)
     */
    @PostMapping(value="/register", produces="application/json")
    public ResponseEntity<ScheduleResponseDto> register(@RequestBody ScheduleRequestDto scheduleRequestDto) {
        ScheduleResponseDto scheduleResponseDto = scheduleService.createSchedule(scheduleRequestDto);
        log.info("getRegister : {}", scheduleResponseDto);
        return ResponseEntity.ok().body(scheduleResponseDto);
    }

    /**
     * ID로 특정 일정을 조회합니다.
     *
     * @param sid 일정의 ID
     * @return 조회된 일정 응답 DTO를 포함한 HTTP 상태 코드 200 (OK) 또는 일정이 없을 경우 HTTP 상태 코드 404 (Not Found)
     */
    @GetMapping("/{sid}")
    public ResponseEntity<ScheduleResponseDto> getSchedule(@PathVariable Long sid){
        Optional<ScheduleResponseDto> schedule = scheduleService.getScheduleById(sid);
        log.info("getSchedule : {}", schedule);
        return schedule.map(dto -> ResponseEntity.ok().body(dto))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 페이지네이션을 통해 모든 일정을 조회합니다.
     *
     * @param page 조회할 페이지 번호 (기본값: 0)
     * @param size 페이지당 항목 수 (기본값: 10)
     * @return 일정 응답 DTO의 페이지 목록을 포함한 HTTP 상태 코드 200 (OK)
     */
    @GetMapping
    public ResponseEntity<Page<SchedulePageResponseDto>> getSchedules( @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size){
       Page<SchedulePageResponseDto> schedule = scheduleService.getAllSchedules(page,size);
       log.info("Pagination Schedule : {}", schedule);
       return ResponseEntity.ok().body(schedule);
    }

    /**
     * 특정 일정을 업데이트합니다.
     *
     * @param sid 일정의 ID
     * @param scheduleRequestDto 업데이트할 일정 요청 DTO
     * @return 업데이트된 일정 응답 DTO와 HTTP 상태 코드 200 (OK)
     */
    @PutMapping("/update/{sid}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable Long sid, @RequestBody ScheduleRequestDto scheduleRequestDto){
        ScheduleResponseDto updateSchedule = scheduleService.updateSchedule(sid, scheduleRequestDto);
        log.info("updateSchedule : {}", updateSchedule);
        return ResponseEntity.ok().body(updateSchedule);
    }

    /**
     * 특정 일정을 삭제합니다.
     *
     * @param sid 일정의 ID
     * @return HTTP 상태 코드 204 (No Content)
     */
    @DeleteMapping("/delete/{sid}")
    public ResponseEntity<ScheduleResponseDto> deleteSchedule(@PathVariable Long sid){
       scheduleService.deleteSchedule(sid);
       log.info("deleteSchedule : {}", sid);
       return ResponseEntity.noContent().build();
    }




}

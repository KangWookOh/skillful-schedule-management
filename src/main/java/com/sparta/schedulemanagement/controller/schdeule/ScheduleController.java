package com.sparta.schedulemanagement.controller.schdeule;

import com.sparta.schedulemanagement.common.response.ApiResponse;
import com.sparta.schedulemanagement.common.util.LoggerUtil;
import com.sparta.schedulemanagement.dto.schedule.SchedulePageResponseDto;
import com.sparta.schedulemanagement.dto.schedule.ScheduleRequestDto;
import com.sparta.schedulemanagement.dto.schedule.ScheduleResponseDto;
import com.sparta.schedulemanagement.service.schedule.ScheduleServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class ScheduleController {

    private final ScheduleServiceImpl scheduleService;

    /**
     * 새로운 일정을 등록합니다.
     *
     * @param scheduleRequestDto 일정 등록 요청 DTO
     * @return 생성된 일정 응답 DTO와 HTTP 상태 코드 200 (OK)
     */
    @PostMapping(value="/v1/schedules", produces="application/json")
    public ResponseEntity<ApiResponse<ScheduleResponseDto>> register(@RequestBody ScheduleRequestDto scheduleRequestDto) {
        ScheduleResponseDto scheduleResponseDto = scheduleService.createSchedule(scheduleRequestDto);
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(scheduleResponseDto));
        } catch (Exception e) {
            LoggerUtil.logError("일정 생성", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("일정 생성 실패", e.getMessage()));
        }
    }


    /**
     * ID로 특정 일정을 조회합니다.
     *
     * @param sid 일정의 ID
     * @return 조회된 일정 응답 DTO를 포함한 HTTP 상태 코드 200 (OK) 또는 일정이 없을 경우 HTTP 상태 코드 404 (Not Found)
     */
    @GetMapping("/v1/schedules/{sid}")
    public ResponseEntity<ApiResponse<ScheduleResponseDto>> getSchedule(@PathVariable Long sid){
        Optional<ScheduleResponseDto> schedule = Optional.ofNullable(scheduleService.getSchedule(sid));
        return schedule.map(dto -> {
            LoggerUtil.logInfo("getSchedule", dto);
            return ResponseEntity.ok(ApiResponse.success(dto));
        }).orElseGet(() -> {
            LoggerUtil.logError("일정 조회", new NoSuchElementException("일정을 찾을 수 없습니다."));
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("일정을 찾을 수 없습니다.", "일정 ID: " + sid + "에 해당하는 일정이 존재하지 않습니다."));
        });
    }

    /**
     * 페이지네이션을 통해 모든 일정을 조회합니다.
     *
     * @param page 조회할 페이지 번호 (기본값: 0)
     * @param size 페이지당 항목 수 (기본값: 10)
     * @return 일정 응답 DTO의 페이지 목록을 포함한 HTTP 상태 코드 200 (OK)
     */
    @GetMapping("/v1/schedules")
    public ResponseEntity<ApiResponse<Page<SchedulePageResponseDto>>> getSchedules( @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size){
       Page<SchedulePageResponseDto> schedule = scheduleService.getSchedules(page,size);
       return ResponseEntity.ok(ApiResponse.success(schedule));
    }

    /**
     * 특정 일정을 업데이트합니다.
     *
     * @param sid 일정의 ID
     * @param scheduleRequestDto 업데이트할 일정 요청 DTO
     * @return 업데이트된 일정 응답 DTO와 HTTP 상태 코드 200 (OK)
     */
    @PutMapping("/v1/schedules/{sid}")
    public ResponseEntity<ApiResponse<ScheduleResponseDto>> updateSchedule(@PathVariable Long sid, @RequestBody ScheduleRequestDto scheduleRequestDto){
        try {
            ScheduleResponseDto updatedSchedule = scheduleService.updateSchedule(sid, scheduleRequestDto);
            return ResponseEntity.ok(ApiResponse.success(updatedSchedule));
        } catch (Exception e) {
            LoggerUtil.logError("일정 업데이트 실패", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("일정 업데이트 실패", e.getMessage()));
        }
    }

    /**
     * 특정 일정을 삭제합니다.
     *
     * @param sid 일정의 ID
     * @return HTTP 상태 코드 204 (No Content)
     */
    @DeleteMapping("/v1/schedules/{sid}")
    public ResponseEntity<ApiResponse<ScheduleResponseDto>> deleteSchedule(@PathVariable Long sid){
        try {
            scheduleService.deleteSchedule(sid);
            return ResponseEntity.noContent().build(); // No Content는 본문이 없으므로, ApiResponse 사용 필요 없음
        } catch (Exception e) {
            LoggerUtil.logError("일정 삭제 실패", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("일정 삭제 실패", e.getMessage()));
        }
    }




}

package com.sparta.schedulemanagement.Service.Schedule;

import com.sparta.schedulemanagement.Dto.Schedule.SchedulePageResponseDto;
import com.sparta.schedulemanagement.Dto.Schedule.ScheduleRequestDto;
import com.sparta.schedulemanagement.Dto.Schedule.ScheduleResponseDto;
import com.sparta.schedulemanagement.Dto.Schedule.WeatherResponseDto;
import com.sparta.schedulemanagement.Entity.Schedule;
import com.sparta.schedulemanagement.Entity.User;
import com.sparta.schedulemanagement.Repository.ScheduleRepository;
import com.sparta.schedulemanagement.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImpl implements ScheduleService{

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Value("${weather.api.url}")
    private String weatherApiUrl;


    /**
     * 새로운 일정을 생성하는 메서드입니다.
     * 일정의 소유자와 담당자를 추가하고, 현재 날씨 정보를 함께 저장합니다.
     *
     * @param scheduleRequestDto 일정 요청 DTO
     * @return 생성된 일정의 응답 DTO
     */
    @Transactional
    @Override
    public ScheduleResponseDto createSchedule(ScheduleRequestDto scheduleRequestDto) {
        User owner = userRepository.findById(scheduleRequestDto.getOwnerId()).orElseThrow(()->new IllegalArgumentException("사용자가 없습니다"));
        WeatherResponseDto weather = getWeatherForToday();
        Schedule schedule = Schedule.builder()
                .title(scheduleRequestDto.getTitle())
                .contents(scheduleRequestDto.getContents())
                .weather(weather.getWeather())
                .owner(owner)
                .build();
        if (scheduleRequestDto.getAssigneeIds() != null) {
            for (Long assigneeId : scheduleRequestDto.getAssigneeIds()) {
                User assignee = userRepository.findById(assigneeId)
                        .orElseThrow(() -> new IllegalArgumentException("Assignee not found"));
                schedule.addAssignedUser(assignee);
            }
        }
        return ScheduleResponseDto.from(scheduleRepository.save(schedule));
    }



    /**
     * 일정 ID로 특정 일정을 조회하는 메서드입니다.
     * 담당자가 없는 경우 일정을 삭제합니다.
     *
     * @param sid 일정 ID
     * @return 조회된 일정의 응답 DTO(Optional)
     */
    @Override
    @Transactional
    public Optional<ScheduleResponseDto> getScheduleById(Long sid){
        Schedule schedule = scheduleRepository.findById(sid)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다"));

        if(schedule.getAssignees() == null || schedule.getAssignees().isEmpty()){
            throw new NoSuchElementException("담당자가 비어있어 스케줄을 조회 할수 없습니다.");
        }
        return Optional.of(ScheduleResponseDto.from(schedule));
    }

    /**
     * 모든 일정을 페이지 단위로 조회하는 메서드입니다.
     *
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 일정 페이지 응답 DTO
     */
    @Override
    public Page<SchedulePageResponseDto> getAllSchedules(int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "updateDate"));
        return scheduleRepository.findAll(pageable).map(schedule -> {
            int replyCount = schedule.getReplies().size();
            return SchedulePageResponseDto.from(schedule,replyCount);
        });

    }


    /**
     * 특정 일정을 수정하는 메서드입니다.
     * 일정의 소유자와 담당자 목록을 업데이트합니다.
     *
     * @param sid 일정 ID
     * @param scheduleRequestDto 일정 요청 DTO
     * @return 수정된 일정의 응답 DTO
     */
    @Override
    @Transactional
    public ScheduleResponseDto updateSchedule(Long sid, ScheduleRequestDto scheduleRequestDto) {
        Schedule schedule = scheduleRepository.findById(sid)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다."));

        if (scheduleRequestDto.getOwnerId() != null) {
            User newOwner = userRepository.findById(scheduleRequestDto.getOwnerId())
                    .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
            schedule.updateOwner(newOwner);  // 메소드를 통해 업데이트
        }

        // Schedule 객체의 필드 업데이트
        schedule.updateSchedule(scheduleRequestDto);
        // 기존 담당자 제거
        schedule.getAssignees().clear();

        // 새로운 담당자 추가
        if (scheduleRequestDto.getAssigneeIds() != null) {
            for (Long assigneeId : scheduleRequestDto.getAssigneeIds()) {
                User assignee = userRepository.findById(assigneeId)
                        .orElseThrow(() -> new IllegalArgumentException("담당자를 찾을 수 없습니다."));
                schedule.addAssignedUser(assignee);
            }
        }
        return ScheduleResponseDto.from(scheduleRepository.save(schedule));
    }

        /**
         * 특정 일정을 삭제하는 메서드입니다.
         *
         * @param sid 일정 ID
         */
        @Override
        public void deleteSchedule (Long sid){
            Schedule schedule = scheduleRepository.findById(sid).orElseThrow(() -> new IllegalArgumentException("일정을 찾을수 없습니다"));
            scheduleRepository.delete(schedule);
        }

        /**
         * 오늘의 날씨 정보를 가져오는 메서드입니다.
         * 외부 API에서 날씨 정보를 가져와 현재 날짜에 해당하는 날씨를 반환합니다.
         *
         * @return 오늘의 날씨 응답 DTO
         */
        @Override
        public WeatherResponseDto getWeatherForToday () {
            WeatherResponseDto[] response = restTemplate.getForObject(weatherApiUrl, WeatherResponseDto[].class);
            if (response == null || response.length == 0) {
                throw new RuntimeException("날씨 정보를 가져오는데 실패 했습니다.");
            }
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd"));

            return Arrays.stream(response)
                    .filter(weather -> today.equals(weather.getDate()))
                    .findFirst()
                    .orElseGet(() -> {
                        WeatherResponseDto weatherResponseDto = new WeatherResponseDto();
                        weatherResponseDto.setDate(today);
                        weatherResponseDto.setWeather("날씨 정보 없음");
                        return weatherResponseDto;

                    });

        }


}


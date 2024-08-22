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
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImpl implements ScheduleService{

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Value("${weather.api.url}")
    private String weatherApiUrl;


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



    @Override
    @Transactional(readOnly = true)
    public Optional<ScheduleResponseDto> getScheduleById(Long sid){
        Schedule schedule = scheduleRepository.findById(sid)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다"));

        if(schedule.getAssignees() == null || schedule.getAssignees().isEmpty()){
            scheduleRepository.delete(schedule);
            throw new NoSuchElementException("담당자가 비어있어 스케줄이 삭제 되었습니다.");
        }
        return Optional.of(ScheduleResponseDto.from(schedule));
    }

    @Override
    public Page<SchedulePageResponseDto> getAllSchedules(int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "updateDate"));
        return scheduleRepository.findAll(pageable).map(schedule -> {
            int replyCount = schedule.getReplies().size();
            return SchedulePageResponseDto.from(schedule,replyCount);
        });

    }

    @Override
    @Transactional
    public ScheduleResponseDto updateSchedule(Long sid, ScheduleRequestDto scheduleRequestDto) {
        Schedule schedule = scheduleRepository.findById(sid)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다."));

        User newOwner = userRepository.findById(scheduleRequestDto.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        // Schedule 객체의 필드 업데이트
        schedule.updateSchedule(scheduleRequestDto, newOwner);
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

    @Override
    public void deleteSchedule(Long sid) {
        Schedule schedule = scheduleRepository.findById(sid).orElseThrow(() -> new IllegalArgumentException("일정을 찾을수 없습니다"));
        scheduleRepository.delete(schedule);
    }

    @Override
    public WeatherResponseDto getWeatherForToday() {
        WeatherResponseDto[] response = restTemplate.getForObject(weatherApiUrl, WeatherResponseDto[].class);
        if(response == null || response.length == 0){
            throw new RuntimeException("날씨 정보를 가져오는데 실패 했습니다.");
        }
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd"));

        return Arrays.stream(response)
                .filter(weather ->today.equals(weather.getDate()))
                .findFirst()
                .orElseGet(() -> {
                    WeatherResponseDto weatherResponseDto = new WeatherResponseDto();
                    weatherResponseDto.setDate(today);
                    weatherResponseDto.setWeather("날씨 정보 없음");
                    return weatherResponseDto;

                });

    }


}

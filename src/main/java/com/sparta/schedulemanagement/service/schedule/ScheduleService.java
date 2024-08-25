package com.sparta.schedulemanagement.service.schedule;

import com.sparta.schedulemanagement.dto.schedule.SchedulePageResponseDto;
import com.sparta.schedulemanagement.dto.schedule.ScheduleRequestDto;
import com.sparta.schedulemanagement.dto.schedule.ScheduleResponseDto;
import com.sparta.schedulemanagement.dto.schedule.WeatherResponseDto;
import com.sparta.schedulemanagement.entity.Schedule;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.Set;

public interface ScheduleService {
     ScheduleResponseDto createSchedule(ScheduleRequestDto scheduleRequestDto);

     Optional<ScheduleResponseDto> getSchedule(Long sid);

     Page<SchedulePageResponseDto> getSchedules(int page, int size);

     ScheduleResponseDto updateSchedule(Long sid,ScheduleRequestDto scheduleRequestDto);

     void deleteSchedule(Long sid);

     WeatherResponseDto getWeatherForToday();

     void addNewAssignees(Schedule schedule, Set<Long> assigneeIds);



}

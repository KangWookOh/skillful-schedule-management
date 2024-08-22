package com.sparta.schedulemanagement.Service.Schedule;

import com.sparta.schedulemanagement.Dto.Schedule.SchedulePageResponseDto;
import com.sparta.schedulemanagement.Dto.Schedule.ScheduleRequestDto;
import com.sparta.schedulemanagement.Dto.Schedule.ScheduleResponseDto;
import com.sparta.schedulemanagement.Dto.Schedule.WeatherResponseDto;
import com.sparta.schedulemanagement.Dto.User.Login.LoginRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ScheduleService {
     ScheduleResponseDto createSchedule(ScheduleRequestDto scheduleRequestDto);

     Optional<ScheduleResponseDto> getScheduleById(Long sid);

     Page<SchedulePageResponseDto> getAllSchedules(int page, int size);

     ScheduleResponseDto updateSchedule(Long sid,ScheduleRequestDto scheduleRequestDto);

     void deleteSchedule(Long sid);

     WeatherResponseDto getWeatherForToday();



}

package com.sparta.schedulemanagement.Dto.Schedule;

import com.sparta.schedulemanagement.Entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class ScheduleResponseDto {

    private Long sid;

    private String title;

    private String contents;

    private Long userId;

    private String userName;

    private String email;

    private String weather;

    private String createDate;

    private String updateDate;

    private List<Long> assigneeIds;

    public static ScheduleResponseDto from(Schedule schedule) {
        return new ScheduleResponseDto(
        schedule.getSid(),
        schedule.getTitle(),
        schedule.getContents(),
        schedule.getOwner().getUid(),
        schedule.getOwner().getUserName(),
        schedule.getOwner().getEmail(),
        schedule.getWeather(),
        schedule.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")),
        schedule.getUpdateDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")),
                schedule.getAssignees().stream()
                        .map(assignee -> assignee.getUser().getUid())
                        .collect(Collectors.toList()));
    }
}

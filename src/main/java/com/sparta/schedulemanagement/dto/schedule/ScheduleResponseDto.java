package com.sparta.schedulemanagement.dto.schedule;

import com.sparta.schedulemanagement.entity.Reply;
import com.sparta.schedulemanagement.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class ScheduleResponseDto {

    private Long sid;

    private String title;

    private String contents;

    private String userName;

    private String email;

    private String weather;

    private String createDate;

    private String updateDate;

    private String comment;

    private List<Long> assigneeIds;

    public static ScheduleResponseDto from(Schedule schedule) {
        // Reply 리스트에서 comment만 추출
        List<String> comment = schedule.getReplies().stream()
                .map(Reply::getComment) // Reply 객체의 comment 필드 추출
                .toList();

        return new ScheduleResponseDto(
        schedule.getSid(),
        schedule.getTitle(),
        schedule.getContents(),
        schedule.getOwner().getUserName(),
        schedule.getOwner().getEmail(),
        schedule.getWeather(),
        schedule.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")),
        schedule.getUpdateDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")),
                comment.toString(),
                schedule.getAssignees().stream()
                        .map(assignee -> assignee.getUser().getUid())
                        .distinct()
                        .collect(Collectors.toList()));
    }

}

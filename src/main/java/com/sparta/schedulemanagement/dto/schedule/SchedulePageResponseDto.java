package com.sparta.schedulemanagement.dto.schedule;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sparta.schedulemanagement.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties({"number", "sort", "numberOfElements", "pageable", "empty"}) // JSON 응답에서 이 필드를 제외합니다.
public class SchedulePageResponseDto {

    private Long sid;

    private String title;

    private String contents;

    private int replyCount;

    private String createDate;

    private String updateDate;


    public static SchedulePageResponseDto from(Schedule schedule) {
        return new SchedulePageResponseDto(
                schedule.getSid(),
                schedule.getTitle(),
                schedule.getContents(),
                schedule.getReplies().size(),
                schedule.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")),
                schedule.getUpdateDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분"))
                
        );
    }

}

package com.sparta.schedulemanagement.Dto.Schedule;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sparta.schedulemanagement.Entity.Schedule;
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


    public static SchedulePageResponseDto from(Schedule schedule, int replyCount) {
        return new SchedulePageResponseDto(
                schedule.getSid(),
                schedule.getTitle(),
                schedule.getContents(),
                replyCount,
                schedule.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")),
                schedule.getUpdateDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분"))
                
        );
    }

}

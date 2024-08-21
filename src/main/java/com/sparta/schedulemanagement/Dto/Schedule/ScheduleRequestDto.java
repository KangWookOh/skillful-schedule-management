package com.sparta.schedulemanagement.Dto.Schedule;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class ScheduleRequestDto {

    private String title;

    private String contents;

    private Long ownerId;

    private List<Long> assigneeIds;  // 담당자 IDs



    @Builder
    public ScheduleRequestDto(String title, String contents, Long ownerId, List<Long> assigneeIds) {
        this.title = title;
        this.contents = contents;
        this.ownerId = ownerId;
        this.assigneeIds = assigneeIds;

    }
}

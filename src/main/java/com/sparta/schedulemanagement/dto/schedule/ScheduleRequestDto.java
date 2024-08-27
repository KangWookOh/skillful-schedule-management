package com.sparta.schedulemanagement.dto.schedule;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ScheduleRequestDto {

    private String title;

    private String contents;

    private Long ownerId;

    private Set<Long> assigneeIds;  // 담당자 IDs



    @Builder
    public ScheduleRequestDto(String title, String contents, Long ownerId, Set<Long> assigneeIds) {
        this.title = title;
        this.contents = contents;
        this.ownerId = ownerId;
        this.assigneeIds = assigneeIds;

    }

    public ScheduleRequestDto() {

    }
}

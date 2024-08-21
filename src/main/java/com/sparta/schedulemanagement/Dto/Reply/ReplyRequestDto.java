package com.sparta.schedulemanagement.Dto.Reply;

import com.sparta.schedulemanagement.Entity.Schedule;
import lombok.Builder;
import lombok.Data;

@Data
public class ReplyRequestDto {
    private String comment;
    private String userName;

    @Builder
    public ReplyRequestDto(String comment, String userName) {
        this.comment = comment;
        this.userName = userName;
    }
}

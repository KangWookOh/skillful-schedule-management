package com.sparta.schedulemanagement.Dto.Reply;

import lombok.Builder;
import lombok.Data;

@Data
public class ReplyRequestDto {
    private String comment;
    private Long ownerId;

    @Builder
    public ReplyRequestDto(String comment, Long ownerId) {
        this.comment = comment;
        this.ownerId = ownerId;

    }
}

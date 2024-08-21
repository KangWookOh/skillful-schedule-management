package com.sparta.schedulemanagement.Dto.Reply;

import com.sparta.schedulemanagement.Entity.Reply;
import com.sparta.schedulemanagement.Entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@Getter
public class ReplyResponseDto {

    private Long rid;

    private String comment;

    private String userName;

    private String createDate;

    private String updateDate;


    public static ReplyResponseDto from(Reply reply) {
        return new ReplyResponseDto(
                reply.getRid(),
                reply.getComment(),
                reply.getUserName(),
                reply.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")),
                reply.getUpdateDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분"))
        );
    }

}

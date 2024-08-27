package com.sparta.schedulemanagement.dto.reply;

import com.sparta.schedulemanagement.entity.Reply;
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
                reply.getOwner().getUserName(),
                reply.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")),
                reply.getUpdateDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분"))
        );
    }

    public ReplyResponseDto(Reply reply) {
        this.rid = reply.getRid();
        this.comment = reply.getComment();
        this.userName = reply.getOwner().getUserName();
        this.createDate = reply.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.updateDate = reply.getUpdateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}

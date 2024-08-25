package com.sparta.schedulemanagement.entity;

import com.sparta.schedulemanagement.dto.reply.ReplyRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@Getter
public class Reply extends BaseTime{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rid;

    private String comment;

    private String userName;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id",nullable = false)
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User owner;


    @Builder
    public Reply(String comment, String userName, User owner, Schedule schedule) {
        this.comment = comment;
        this.owner = owner;
        this.userName = userName;
        this.schedule = schedule;
        schedule.getReplies().add(this);


    }

    public void replyUpdate(ReplyRequestDto replyRequestDto){
        this.comment = replyRequestDto.getComment();
    }



}

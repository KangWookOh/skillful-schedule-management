package com.sparta.schedulemanagement.Entity;

import com.sparta.schedulemanagement.Dto.Reply.ReplyRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

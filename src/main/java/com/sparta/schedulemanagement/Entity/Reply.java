package com.sparta.schedulemanagement.Entity;

import com.sparta.schedulemanagement.Dto.Reply.ReplyRequestDto;
import com.sparta.schedulemanagement.Dto.Schedule.ScheduleRequestDto;
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

    @Builder
    public Reply(String comment,String userName, Schedule schedule) {
        this.comment = comment;
        this.userName = userName;
        if (schedule != null) {
            this.schedule = schedule;
            schedule.getReplies().add(this);
        }

    }

    public void replyUpdate(ReplyRequestDto replyRequestDto){
        this.comment = replyRequestDto.getComment();
        this.userName = replyRequestDto.getUserName();
    }



}

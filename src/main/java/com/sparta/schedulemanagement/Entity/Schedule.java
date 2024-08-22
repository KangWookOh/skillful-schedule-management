package com.sparta.schedulemanagement.Entity;

import com.sparta.schedulemanagement.Dto.Schedule.ScheduleRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Schedule extends BaseTime{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sid;

    private String title;

    private String contents;

    private String weather;

    //orphanRemoval 자식엔티티의 변경이 있다면  기존 고아 삭제
    @OneToMany(mappedBy = "schedule",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Reply> replies = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleAssignee> assignees = new ArrayList<>();


    public void updateSchedule(ScheduleRequestDto scheduleRequestDto, User newOwner) {
        this.title = scheduleRequestDto.getTitle();
        this.contents = scheduleRequestDto.getContents();
        if (newOwner != null) {
            this.owner = newOwner;
        }
    }

    @Builder
    public Schedule(String title, String contents,String weather, User owner) {
        this.title = title;
        this.contents = contents;
        this.weather = weather;
        this.owner = owner;
        owner.addOwnedSchedule(this);
    }

    // 연관관계 편의 메서드
    public void addAssignedUser(User user) {
        ScheduleAssignee assignee = new ScheduleAssignee(user, this);
        this.assignees.add(assignee);
        if (!user.getAssignedSchedules().contains(assignee)) {
            user.addAssignedSchedule(assignee);
        }
    }


    // 댓글 추가 메소드
    public void addReply(Reply reply) {
        replies.add(reply);
        // 연관관계 설정
        if (reply.getSchedule() != this) {
            reply = Reply.builder()
                    .comment(reply.getComment())
                    .userName(reply.getUserName())
                    .schedule(this)
                    .build();
        }
    }



}

package com.sparta.schedulemanagement.Entity;

import com.sparta.schedulemanagement.Dto.User.UserRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
public class User extends BaseTime{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    private String userName;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> ownedSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleAssignee> assignedSchedules = new ArrayList<>();

    @Builder
    public User(String userName,String email,String password,UserRole role){
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.role = role;
    }
    public void updateUser(UserRequestDto userRequestDto){
        this.userName = userRequestDto.getUserName();
        this.email = userRequestDto.getEmail();
    }

    // 연관관계 편의 메서드
    public void addOwnedSchedule(Schedule schedule) {
        this.ownedSchedules.add(schedule);
    }

    public void addAssignedSchedule(ScheduleAssignee scheduleAssignee) {
        this.assignedSchedules.add(scheduleAssignee);
    }



}

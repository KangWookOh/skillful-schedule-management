package com.sparta.schedulemanagement.entity;

import com.sparta.schedulemanagement.dto.user.UserRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
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

    @NotEmpty(message = "이메일 입력은 필수 입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
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

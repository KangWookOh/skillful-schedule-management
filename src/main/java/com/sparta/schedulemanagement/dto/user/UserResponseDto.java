package com.sparta.schedulemanagement.dto.user;

import com.sparta.schedulemanagement.entity.User;
import com.sparta.schedulemanagement.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long uid;
    private String userName;
    private String email;
    private UserRole userRole;
    private String createDate;
    private String updateDate;

    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getUid(),
                user.getUserName(),
                user.getEmail(),
                user.getRole(),
                user.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")),
                user.getUpdateDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분"))
        );
    }
}

package com.sparta.schedulemanagement.Dto.User;

import com.sparta.schedulemanagement.Entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRequestDto {

    private String userName;
    private String email;
    private String password;
    private UserRole userRole;

    @Builder
    public UserRequestDto(String userName, String email,String password, UserRole userRole) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }
}

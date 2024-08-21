package com.sparta.schedulemanagement.Dto.User;

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

    @Builder
    public UserRequestDto(String userName, String email,String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }
}

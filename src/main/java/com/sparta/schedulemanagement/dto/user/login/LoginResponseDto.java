package com.sparta.schedulemanagement.dto.user.login;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {

    private String token;


    public static LoginResponseDto getLoginResponseDto(String token){
        return new LoginResponseDto(token);
    }
}

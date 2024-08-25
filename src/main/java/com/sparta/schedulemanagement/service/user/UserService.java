package com.sparta.schedulemanagement.service.user;

import com.sparta.schedulemanagement.dto.user.login.LoginRequestDto;
import com.sparta.schedulemanagement.dto.user.UserRequestDto;
import com.sparta.schedulemanagement.dto.user.UserResponseDto;
import com.sparta.schedulemanagement.dto.user.UserTokenResponseDto;

import java.util.List;

public interface UserService {
    UserTokenResponseDto createUser(UserRequestDto userRequestDto);

    UserResponseDto getUser(Long uid);

    List<UserResponseDto> getUsers();

    UserResponseDto updateUser(Long uid, UserRequestDto userRequestDto);

    void deleteUser(Long uid);


    String loginUser (LoginRequestDto loginRequestDto);

}

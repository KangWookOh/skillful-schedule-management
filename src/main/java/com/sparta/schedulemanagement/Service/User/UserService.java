package com.sparta.schedulemanagement.Service.User;

import com.sparta.schedulemanagement.Dto.User.Login.LoginRequestDto;
import com.sparta.schedulemanagement.Dto.User.UserRequestDto;
import com.sparta.schedulemanagement.Dto.User.UserResponseDto;
import com.sparta.schedulemanagement.Dto.User.UserTokenResponseDto;

import java.util.List;

public interface UserService {
    UserTokenResponseDto createUser(UserRequestDto userRequestDto);

    UserResponseDto getUserById(Long uid);

    List<UserResponseDto> getAllUsers();

    UserResponseDto updateUser(Long uid, UserRequestDto userRequestDto);

    void deleteUser(Long uid);

    String authenticateUser(LoginRequestDto loginRequestDto);

    String loginUser (LoginRequestDto loginRequestDto);

}

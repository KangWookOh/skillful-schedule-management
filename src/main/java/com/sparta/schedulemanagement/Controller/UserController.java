package com.sparta.schedulemanagement.Controller;

import com.sparta.schedulemanagement.Config.Util.JwtUtil;
import com.sparta.schedulemanagement.Dto.User.Login.LoginRequestDto;
import com.sparta.schedulemanagement.Dto.User.Login.LoginResponseDto;
import com.sparta.schedulemanagement.Dto.User.UserRequestDto;
import com.sparta.schedulemanagement.Dto.User.UserResponseDto;
import com.sparta.schedulemanagement.Dto.User.UserTokenResponseDto;
import com.sparta.schedulemanagement.Service.User.UserService;
import com.sparta.schedulemanagement.Service.User.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final JwtUtil jwtUtil;

    private final UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<UserTokenResponseDto> register(@RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(userService.createUser(userRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login (@RequestBody LoginRequestDto loginRequestDto){
        String token = userService.loginUser(loginRequestDto);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/{uid}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long uid) {
        return ResponseEntity.ok(userService.getUserById(uid));
    }
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers(@PathVariable Long uid, @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(Collections.singletonList(userService.updateUser(uid, userRequestDto)));
    }



}

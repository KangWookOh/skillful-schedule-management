package com.sparta.schedulemanagement.Controller;

import com.sparta.schedulemanagement.Dto.User.Login.LoginRequestDto;
import com.sparta.schedulemanagement.Dto.User.UserRequestDto;
import com.sparta.schedulemanagement.Dto.User.UserResponseDto;
import com.sparta.schedulemanagement.Dto.User.UserTokenResponseDto;
import com.sparta.schedulemanagement.Service.User.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserServiceImpl userService;


    /**
     * 사용자 등록을 처리합니다.
     *
     * @param userRequestDto 사용자 등록 요청 데이터 전송 객체
     * @return 등록된 사용자 정보와 JWT 토큰을 포함한 응답
     */

    @PostMapping("/register")
    public ResponseEntity<UserTokenResponseDto> register(@RequestBody UserRequestDto userRequestDto) {
        log.info("회원가입: {}", userRequestDto);
        return ResponseEntity.ok(userService.createUser(userRequestDto));
    }

    /**
     * 사용자 로그인을 처리합니다.
     *
     * @param loginRequestDto 로그인 요청 데이터 전송 객체
     * @return 인증된 사용자의 JWT 토큰
     */
    @PostMapping("/login")
    public ResponseEntity<String> login (@RequestBody LoginRequestDto loginRequestDto){
        String token = userService.loginUser(loginRequestDto);
        log.info("로그인 정보: {}", token);
        return ResponseEntity.ok(token);
    }

    /**
     * 주어진 사용자 ID에 대한 사용자 정보를 조회합니다.
     *
     * @param uid 사용자 ID
     * @return 조회된 사용자 정보
     */
    @GetMapping("/{uid}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long uid) {
        log.info("User: {}",userService.getUserById(uid));
        return ResponseEntity.ok(userService.getUserById(uid));
    }

    /**
     * 모든 사용자 정보를 조회합니다.
     *
     * @return 모든 사용자 정보 목록
     */
    @GetMapping("/list")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        // 모든 사용자 정보를 조회하고, 응답으로 반환
        List<UserResponseDto> users = userService.getAllUsers();
        log.info("get All Users: {}", users);
        return ResponseEntity.ok(users);
    }


    /**
     * 사용자 정보를 업데이트합니다.
     *
     * @param uid 사용자 ID
     * @param userRequestDto 사용자 업데이트 요청 데이터 전송 객체
     * @return 업데이트된 사용자 정보
     */
    @PutMapping("/update/{uid}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long uid, @RequestBody UserRequestDto userRequestDto) {
        log.info("updateUser: {}",userRequestDto);
        log.info("updateInfo: {}", userService.updateUser(uid,userRequestDto));
        return ResponseEntity.ok(userService.updateUser(uid, userRequestDto));
    }

    /**
     * 사용자를 삭제합니다.
     *
     * @param uid 사용자 ID
     * @return 요청이 성공적으로 처리되었음을 나타내는 응답
     */
    @DeleteMapping("/remove")
    public ResponseEntity<Void> deleteUser(@PathVariable Long uid) {
        userService.deleteUser(uid);
        log.info("deleteUser: {}", uid);
        return ResponseEntity.noContent().build();
    }



}

package com.sparta.schedulemanagement.service.user;

import com.sparta.schedulemanagement.common.util.JwtUtils;
import com.sparta.schedulemanagement.common.util.PasswordUtils;
import com.sparta.schedulemanagement.dto.user.login.LoginRequestDto;
import com.sparta.schedulemanagement.dto.user.UserRequestDto;
import com.sparta.schedulemanagement.dto.user.UserResponseDto;
import com.sparta.schedulemanagement.dto.user.UserTokenResponseDto;
import com.sparta.schedulemanagement.entity.User;
import com.sparta.schedulemanagement.entity.UserRole;
import com.sparta.schedulemanagement.exception.InvalidCredentialsException;
import com.sparta.schedulemanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;


    /**
     * 새로운 사용자를 생성하고 JWT 토큰을 반환합니다.
     *
     * @param userRequestDto 사용자 요청 DTO
     * @return 생성된 사용자와 JWT 토큰을 포함하는 응답 DTO
     */
    @Transactional
    @Override
    public UserTokenResponseDto createUser(UserRequestDto userRequestDto) {
        if(userRepository.findByEmail(userRequestDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("사용중인 이메일 입니다.");
        }
        String password = PasswordUtils.hashPassword(userRequestDto.getPassword());
        UserRole role = userRequestDto.getUserRole() != null ? userRequestDto.getUserRole() : UserRole.USER;
        User user = User.builder()
                .userName(userRequestDto.getUserName())
                .email(userRequestDto.getEmail())
                .password(password)
                .role(role)
                .build();
        userRepository.save(user);
        String token = jwtUtils.generateToken(user.getUserName(), user.getRole());
        return UserTokenResponseDto.from(user,token);
    }

    /**
     * 사용자를 로그인하고 JWT 토큰을 반환합니다.
     *
     * @param loginRequestDto 로그인 요청 DTO
     * @return 생성된 JWT 토큰
     */
    @Transactional
    @Override
    public String loginUser(LoginRequestDto loginRequestDto) {
        User user =userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(()-> new IllegalArgumentException("이메일이 일치 하지 않습니다."));
        if (!PasswordUtils.checkPassword(loginRequestDto.getPassword(),user.getPassword())){
            throw new InvalidCredentialsException("잘못된 비밀번호 입니다.");
        }
        return jwtUtils.generateToken(user.getEmail(),user.getRole());
    }

    /**
     * ID로 특정 사용자를 조회합니다.
     *
     * @param uid 사용자 ID
     * @return 조회된 사용자 응답 DTO
     */
    @Override
    public UserResponseDto getUser(Long uid) {
        User user = userRepository.findById(uid)
                .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다"));
        return UserResponseDto.from(user);
    }

    /**
     * 모든 사용자를 조회합니다.
     *
     * @return 모든 사용자 응답 DTO 목록
     */
    @Override
    public List<UserResponseDto> getUsers() {
        return userRepository.findAll().stream()
                .map(UserResponseDto :: from)
                .collect(Collectors.toList());
    }

    /**
     * 사용자를 업데이트합니다.
     *
     * @param uid 사용자 ID
     * @param userRequestDto 사용자 요청 DTO
     * @return 업데이트된 사용자 응답 DTO
     */
    @Override
    @Transactional
    public UserResponseDto updateUser(Long uid, UserRequestDto userRequestDto) {
        User user = userRepository.findById(uid)
                .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다"));
        user.updateUser(userRequestDto);
        return UserResponseDto.from(userRepository.save(user));
    }

    /**
     * 사용자를 삭제합니다.
     *
     * @param uid 사용자 ID
     */
    @Override
    @Transactional
    public void deleteUser(Long uid) {
        userRepository.findById(uid)
                .orElseThrow(()->new IllegalArgumentException("유저를 찾을 수 없습니다."));
        userRepository.deleteById(uid);

    }
}

package com.sparta.schedulemanagement.Service.User;

import com.sparta.schedulemanagement.Config.Util.JwtUtil;
import com.sparta.schedulemanagement.Config.Util.PasswordUtil;
import com.sparta.schedulemanagement.Dto.User.Login.LoginRequestDto;
import com.sparta.schedulemanagement.Dto.User.UserRequestDto;
import com.sparta.schedulemanagement.Dto.User.UserResponseDto;
import com.sparta.schedulemanagement.Dto.User.UserTokenResponseDto;
import com.sparta.schedulemanagement.Entity.User;
import com.sparta.schedulemanagement.Entity.UserRole;
import com.sparta.schedulemanagement.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    /**
     * 새로운 사용자를 생성하고 JWT 토큰을 반환합니다.
     *
     * @param userRequestDto 사용자 요청 DTO
     * @return 생성된 사용자와 JWT 토큰을 포함하는 응답 DTO
     */
    @Override
    public UserTokenResponseDto createUser(UserRequestDto userRequestDto) {
        if(userRepository.findByEmail(userRequestDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("사용중인 이메일 입니다.");
        }
        String password = PasswordUtil.hashPassword(userRequestDto.getPassword());
        User user = User.builder()
                .userName(userRequestDto.getUserName())
                .email(userRequestDto.getEmail())
                .password(password)
                .role(UserRole.USER)
                .build();
        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getUserName(), user.getRole());
        return UserTokenResponseDto.from(user,token);
    }

    /**
     * 사용자를 로그인하고 JWT 토큰을 반환합니다.
     *
     * @param loginRequestDto 로그인 요청 DTO
     * @return 생성된 JWT 토큰
     */
    @Override
    public String loginUser(LoginRequestDto loginRequestDto) {
        User user =userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(()-> new IllegalArgumentException("이메일이 일치 하지 않습니다."));
        if (!PasswordUtil.checkPassword(loginRequestDto.getPassword(),user.getPassword())){
            throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
        }
        return jwtUtil.generateToken(user.getEmail(),user.getRole());
    }

    /**
     * ID로 특정 사용자를 조회합니다.
     *
     * @param uid 사용자 ID
     * @return 조회된 사용자 응답 DTO
     */
    @Override
    public UserResponseDto getUserById(Long uid) {
        User user = userRepository.findById(uid)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));
        return UserResponseDto.from(user);
    }

    /**
     * 모든 사용자를 조회합니다.
     *
     * @return 모든 사용자 응답 DTO 목록
     */
    @Override
    public List<UserResponseDto> getAllUsers() {
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
    public UserResponseDto updateUser(Long uid, UserRequestDto userRequestDto) {
        User user = userRepository.findById(uid)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));
        user.updateUser(userRequestDto);
        return UserResponseDto.from(userRepository.save(user));
    }

    /**
     * 사용자를 삭제합니다.
     *
     * @param uid 사용자 ID
     */
    @Override
    public void deleteUser(Long uid) {
        userRepository.findById(uid)
                .orElseThrow(()->new IllegalArgumentException("유저를 찾을 수 없습니다."));
        userRepository.deleteById(uid);

    }
}

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;


    @Override
    public String authenticateUser(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail()).
                orElseThrow(() -> new IllegalArgumentException("잘못된 이메일 입니다."));
        if(!PasswordUtil.checkPassword(loginRequestDto.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
        }
        return jwtUtil.generateToken(user.getEmail(),user.getRole());
    }


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

    @Override
    public String loginUser(LoginRequestDto loginRequestDto) {
        User user =userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(()-> new IllegalArgumentException("이메일이 일치 하지 않습니다."));
        if (!PasswordUtil.checkPassword(loginRequestDto.getPassword(),user.getPassword())){
            throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
        }
        return jwtUtil.generateToken(user.getEmail(),user.getRole());
    }

    @Override
    public UserResponseDto getUserById(Long uid) {
        User user = userRepository.findById(uid).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));
        return UserResponseDto.from(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserResponseDto :: from).collect(Collectors.toList());
    }

    @Override
    public UserResponseDto updateUser(Long uid, UserRequestDto userRequestDto) {
        User user = userRepository.findById(uid).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));
        user.updateUser(userRequestDto);
        return UserResponseDto.from(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long uid) {
        userRepository.findById(uid).orElseThrow(()->new IllegalArgumentException("유저를 찾을 수 없습니다."));
        userRepository.deleteById(uid);

    }
}

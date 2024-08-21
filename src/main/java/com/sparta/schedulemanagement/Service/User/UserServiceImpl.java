package com.sparta.schedulemanagement.Service.User;

import com.sparta.schedulemanagement.Config.Util.JwtUtil;
import com.sparta.schedulemanagement.Config.Util.PasswordUtil;
import com.sparta.schedulemanagement.Dto.User.UserRequestDto;
import com.sparta.schedulemanagement.Dto.User.UserResponseDto;
import com.sparta.schedulemanagement.Dto.User.UserTokenResponseDto;
import com.sparta.schedulemanagement.Entity.User;
import com.sparta.schedulemanagement.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserRepository userRepository, PasswordUtil passwordUtil, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordUtil = passwordUtil;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserTokenResponseDto createUser(UserRequestDto userRequestDto) {
        String password = passwordUtil.hashPassword(userRequestDto.getPassword());
        User user = User.builder()
                .userName(userRequestDto.getUserName())
                .email(userRequestDto.getEmail())
                .password(password)
                .build();
        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getUserName());
        return UserTokenResponseDto.from(user,token);
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

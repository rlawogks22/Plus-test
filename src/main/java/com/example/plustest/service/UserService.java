package com.example.plustest.service;

import com.example.plustest.dto.UserRequestDto;
import com.example.plustest.entity.User;
import com.example.plustest.jwt.JwtUtil;
import com.example.plustest.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public User signUp(String username,String nickname, String password) {
        // 아이디 중복 체크
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 아이디가 존재합니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);

        // 회원 객체 생성 및 저장
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(encodedPassword);
        newUser.setNickname(nickname);

        return userRepository.save(newUser);
    }

    public void login(UserRequestDto requestDto,HttpServletResponse response) {
            String username = requestDto.getUsername();
            String password = requestDto.getPassword();

            User user = userRepository.findByUsername(username).orElseThrow(
                    ()-> new IllegalArgumentException("등록된 사용자가 없습니다.")
            );

            if(!passwordEncoder.matches(password,user.getPassword())){
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }

            String token = jwtUtil.createToken((user.getUsername()));
            jwtUtil.addJwtToCookie(token, response);


    }
}


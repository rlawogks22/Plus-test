package com.example.plustest.service;

import com.example.plustest.entity.User;
import com.example.plustest.repository.UserRepository;
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
    public boolean isUsernameUnique(String username) {
        Optional<User> existingMember = userRepository.findByUsername(username);
        return existingMember.isEmpty();
    }

    public User signUp(String username, String password) {
        // 아이디 중복 체크
        if (!isUsernameUnique(username)) {
            throw new IllegalArgumentException("중복된 아이디가 존재합니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);

        // 회원 객체 생성 및 저장
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(encodedPassword);

        return userRepository.save(newUser);
    }
}


package com.playvoice.userservice.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.playvoice.userservice.dto.ChangePasswordRequest;
import com.playvoice.userservice.entity.PasswordStatus;
import com.playvoice.userservice.entity.User;
import com.playvoice.userservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    public void changePassword(String username, ChangePasswordRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Current password does not match");
        }

        String newEncodedPassword = passwordEncoder.encode(request.getNewPassword());
        user.changePassword(newEncodedPassword);
        userRepository.save(user);
    }

    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId).orElseThrow();
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        if (!isValidPassword(newPassword)) {
            throw new IllegalArgumentException("비밀번호 규칙에 맞지 않습니다.");
        }
        user.changePassword(passwordEncoder.encode(newPassword));
        user.setPasswordStatus(PasswordStatus.CHANGED);
        userRepository.save(user);
    }

    public void changeEmail(Long userId, String password, String newEmail) {
        User user = userRepository.findById(userId).orElseThrow();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        user.changeEmail(newEmail);
        userRepository.save(user);
    }

    public void changeName(Long userId, String password, String newName) {
        User user = userRepository.findById(userId).orElseThrow();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        user.changeName(newName);
        userRepository.save(user);
    }

    public void deleteAccount(Long userId, String password) {
        User user = userRepository.findById(userId).orElseThrow();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        userRepository.delete(user);
    }

    public void banStudent(Long studentId) {
        User user = userRepository.findById(studentId).orElseThrow();
        user.setIsBanned(true);
        userRepository.save(user);
    }

    public void unbanStudent(Long studentId) {
        User user = userRepository.findById(studentId).orElseThrow();
        user.setIsBanned(false);
        userRepository.save(user);
    }

    public void resetStudentPassword(Long studentId) {
        User user = userRepository.findById(studentId).orElseThrow();
        String tempPassword = generateTempPassword();
        user.changePassword(passwordEncoder.encode(tempPassword));
        user.setPasswordStatus(PasswordStatus.INIT);
        userRepository.save(user);
        mailService.sendPasswordResetEmail(user.getEmail(), tempPassword);
    }

    private String generateTempPassword() {
        return "Temp" + System.currentTimeMillis() % 10000 + "!";
    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=]).{8,}$");
    }
}

package com.playvoice.userservice.service;

import com.playvoice.userservice.dto.ChangePasswordRequest;
import com.playvoice.userservice.dto.CreateUserRequest;
import com.playvoice.userservice.dto.UserResponseDTO;
import com.playvoice.userservice.entity.PasswordStatus;
import com.playvoice.userservice.entity.User;
import com.playvoice.userservice.entity.UserRole;
import com.playvoice.userservice.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;
import static org.bouncycastle.asn1.x509.Extensions.getExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.web.multipart.MultipartFile;


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

    public User createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 username입니다.");
        }
        if (userRepository.findAll().stream()
            .anyMatch(u -> u.getEmail().equals(request.getEmail()))) {
            throw new IllegalArgumentException("이미 사용 중인 email입니다.");
        }
        if (!isValidPassword(request.getPassword())) {
            throw new IllegalArgumentException("비밀번호 규칙에 맞지 않습니다.");
        }
        UserRole role = UserRole.valueOf(request.getRole().toUpperCase());
        User user = User.builder()
            .email(request.getEmail())
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(role)
            .course(request.getCourse())
            .isBanned(false)
            .passwordStatus(PasswordStatus.INIT)
            .createdAt(LocalDateTime.now())
            .lastChangedPassword(LocalDateTime.now())
            .lastLogin(LocalDateTime.now())
            .name(request.getName())
            .build();
        return userRepository.save(user);
    }

    private String generateTempPassword() {
        return "Temp" + System.currentTimeMillis() % 10000 + "!";
    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=]).{8,}$");
    }

    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        return new UserResponseDTO(user.getId(), user.getEmail(), user.getUsername(),
            user.getName(), user.getRole(), user.getCourse(), user.getIsBanned(),
            user.getPasswordStatus(), user.getCreatedAt(), user.getLastChangedPassword(),
            user.getLastLogin(), user.getProfileImageUrl());
    }

    public String uploadProfileImage(Long userId, MultipartFile file) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User Not Found"));

            // 확장자 및 용량 체크 (보안)
            String originalName = file.getOriginalFilename();
            String ext = "";
            if (originalName != null && originalName.contains(".")) {
                ext = originalName.substring(originalName.lastIndexOf(".")).toLowerCase();
            } else {
                throw new IllegalArgumentException("파일 확장자가 없습니다. jpg, png, svg 파일만 업로드 가능합니다.");
            }
            if (!ext.matches("\\.(jpg|png|svg)$")) {
                throw new IllegalArgumentException("jpg, png, svg 파일만 업로드 가능합니다.");
            }
            if (file.getSize() > 2 * 1024 * 1024) {
                throw new IllegalArgumentException("2MB 이하 파일만 업로드 가능합니다.");
            }

            String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";
            Files.createDirectories(Paths.get(uploadDir));

            // 기존 이미지 삭제
            if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().isEmpty() && !user.getProfileImageUrl().contains("default")) {
                File oldFile = new File(uploadDir + File.separator + user.getProfileImageUrl().substring(user.getProfileImageUrl().lastIndexOf("/") + 1));
                if (oldFile.exists()) oldFile.delete();
            }

            // 파일명에 확장자 반드시 포함
            String fileName;
            if (!ext.isEmpty() && (ext.equals(".png") || ext.equals(".jpg") || ext.equals(".svg"))) {
                fileName = "user_" + userId + "_" + System.currentTimeMillis() + ext;
            } else {
                throw new IllegalArgumentException("파일 확장자가 올바르지 않습니다. (jpg, png, svg)");
            }
            java.nio.file.Path savePath = Paths.get(uploadDir, fileName);
            file.transferTo(savePath.toFile());

            String url = "/uploads/" + fileName;
            user.setProfileImageUrl(url);
            userRepository.save(user);
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("이미지 저장 실패: " + e.getMessage());
        }
    }

    public String deleteProfileImage(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";

        // 기존 파일 삭제
        if (user.getProfileImageUrl() != null && user.getProfileImageUrl().startsWith("/uploads/")) {
            java.nio.file.Path filePath = java.nio.file.Paths.get(uploadDir, java.nio.file.Paths.get(user.getProfileImageUrl()).getFileName().toString());
            try { java.nio.file.Files.deleteIfExists(filePath); } catch (IOException ignored) {}
        }

        user.setProfileImageUrl(null); // 기본 이미지 경로가 아니라 null로 저장
        userRepository.save(user);

        return null; // 반환값도 null
    }
}

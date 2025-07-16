package com.playvoice.userservice.controller;

import com.playvoice.userservice.dto.*;
import com.playvoice.userservice.entity.User;
import com.playvoice.userservice.repository.UserRepository;
import com.playvoice.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

// 매니저 간단 정보 DTO
record ManagerSimpleDTO(Long id, String name, String profileImageUrl) {}

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    // 🔐 1. 비밀번호 변경 (직접 구현한 방식)
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request,
                                            Principal principal) {
        log.info("changePassword: {}", request);
        String username = principal.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Current password does not match.");
        }

        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+])[A-Za-z\\d!@#$%^&*()_+]{8,}$";
        if (!request.getNewPassword().matches(passwordRegex)) {
            return ResponseEntity.badRequest().body(
                    "Password must be at least 8 characters long and contain uppercase, lowercase, number, and special character.");
        }

        user.changePassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Password changed successfully.");
    }

    // ✉️ 2. 이메일 변경
    @PostMapping("/change-email")
    public ResponseEntity<?> changeEmail(@RequestBody ChangeEmailRequest request,
                                         Principal principal) {
        String username = principal.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Password Not Match");
        }

        if (userRepository.findAll().stream().anyMatch(u -> u.getEmail().equals(request.getNewEmail()))) {
            return ResponseEntity.badRequest().body("Email Already In Use");
        }

        user.changeEmail(request.getNewEmail());
        userRepository.save(user);

        return ResponseEntity.ok("Email changed successfully");
    }

    // 🧾 3. 이름 변경
    @PostMapping("/change-name")
    public ResponseEntity<?> changeName(@RequestBody ChangeNameRequest request,
                                        Principal principal) {
        String username = principal.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Password Not Match");
        }

        user.changeName(request.getNewName());
        userRepository.save(user);

        return ResponseEntity.ok("Name changed successfully");
    }

    // 🗑️ 4. 계정 삭제 (직접 구현한 방식)
    @DeleteMapping("/delete-account")
    public ResponseEntity<?> deleteAccount(@RequestBody DeleteAccountRequest request,
                                           Principal principal) {
        String username = principal.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Password Not Match");
        }

        userRepository.delete(user);
        return ResponseEntity.ok("Account deleted successfully");
    }

    // 👤 5. 내 정보 관련 API (권장 방식)
    @PutMapping("/me/password")
    public ResponseEntity<Void> changePasswordV2(@RequestBody ChangePasswordRequest request,
                                                 @AuthenticationPrincipal String userId) {
        userService.changePassword(Long.valueOf(userId), request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me/email")
    public ResponseEntity<Void> changeEmailV2(@RequestBody ChangeEmailRequest request,
                                              @AuthenticationPrincipal String userId) {
        userService.changeEmail(Long.valueOf(userId), request.getPassword(), request.getNewEmail());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me/name")
    public ResponseEntity<Void> changeNameV2(@RequestBody ChangeNameRequest request,
                                             @AuthenticationPrincipal String userId) {
        userService.changeName(Long.valueOf(userId), request.getPassword(), request.getNewName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteAccountV2(@RequestBody DeleteAccountRequest request,
                                                @AuthenticationPrincipal String userId) {
        userService.deleteAccount(Long.valueOf(userId), request.getPassword());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyProfile(@AuthenticationPrincipal String userId) {
        UserResponseDTO dto = userService.findById(Long.valueOf(userId));
        return ResponseEntity.ok(dto);
    }

    // 🔍 6. 관리자 또는 외부 사용자의 ID로 조회
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO dto = userService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/managers")
    public ResponseEntity<List<ManagerSimpleDTO>> getAllManagers() {
        List<ManagerSimpleDTO> managers = userService.findAllManagers().stream()
            .map(m -> new ManagerSimpleDTO(m.id(), m.name(), m.profileImageUrl()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(managers);
    }

    @PostMapping("/me/img")
    public ResponseEntity<String> uploadProfileImage(@RequestParam("image")MultipartFile file,
                                                     @AuthenticationPrincipal String userId) {
        String uploadedUrl = userService.uploadProfileImage(Long.valueOf(userId), file);
        return ResponseEntity.ok(uploadedUrl);
    }

    @DeleteMapping("/me/img")
    public ResponseEntity<String> deleteProfileImage(@AuthenticationPrincipal String userId) {
        String url = userService.deleteProfileImage(Long.valueOf(userId));
        return ResponseEntity.ok(url);
    }
}

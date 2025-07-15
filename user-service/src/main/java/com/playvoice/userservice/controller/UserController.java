package com.playvoice.userservice.controller;

import com.playvoice.userservice.dto.ChangeEmailRequest;
import com.playvoice.userservice.dto.ChangeNameRequest;
import com.playvoice.userservice.dto.ChangePasswordRequest;
import com.playvoice.userservice.dto.DeleteAccountRequest;
import com.playvoice.userservice.dto.UserResponseDTO;
import com.playvoice.userservice.entity.User;
import com.playvoice.userservice.repository.UserRepository;
import com.playvoice.userservice.service.UserService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

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

        // 비밀번호 정규식: 최소 8자, 대소문자, 숫자, 특수문자 포함
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+])[A-Za-z\\d!@#$%^&*()_+]{8,}$";
        if (!request.getNewPassword().matches(passwordRegex)) {
            return ResponseEntity.badRequest().body(
                "Password must be at least 8 characters long and contain uppercase, lowercase, number, and special character."
            );
        }

        user.changePassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Password changed successfully.");
    }

    @PostMapping("/change-email")
    public ResponseEntity<?> changeEmail(@RequestBody ChangeEmailRequest request,
        Principal principal) {
        String username = principal.getName();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User Not Found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Password Not Match");
        }

        if (userRepository.findAll().stream()
            .anyMatch(u -> u.getEmail().equals(request.getNewEmail()))) {
            return ResponseEntity.badRequest().body("Email Already In Use");
        }

        user.changeEmail(request.getNewEmail());
        userRepository.save(user);

        return ResponseEntity.ok("Email changed successfully");
    }

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

    @PutMapping("/me/password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest request,
        @AuthenticationPrincipal String userId) {
        userService.changePassword(Long.valueOf(userId), request.getOldPassword(),
            request.getNewPassword());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me/email")
    public ResponseEntity<Void> changeEmail(@RequestBody ChangeEmailRequest request,
        @AuthenticationPrincipal String userId) {
        userService.changeEmail(Long.valueOf(userId), request.getPassword(), request.getNewEmail());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me/name")
    public ResponseEntity<Void> changeName(@RequestBody ChangeNameRequest request,
        @AuthenticationPrincipal String userId) {
        userService.changeName(Long.valueOf(userId), request.getPassword(), request.getNewName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyProfile(@AuthenticationPrincipal String userId) {
        Long id = Long.valueOf(userId);
        UserResponseDTO userInfo = userService.findById(id);
        return ResponseEntity.ok(userInfo);
    }


    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteAccount(@RequestBody DeleteAccountRequest request,
        @AuthenticationPrincipal String userId) {
        userService.deleteAccount(Long.valueOf(userId), request.getPassword());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        UserResponseDTO dto = userService.findById(id);

        return ResponseEntity.ok(
            dto
        );
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getUser(@AuthenticationPrincipal String userId) {
        UserResponseDTO dto = userService.findById(Long.valueOf(userId));

        return ResponseEntity.ok(
            dto
        );
    }
}

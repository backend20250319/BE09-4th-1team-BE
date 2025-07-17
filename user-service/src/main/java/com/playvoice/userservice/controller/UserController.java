package com.playvoice.userservice.controller;

import com.playvoice.userservice.dto.*;
import com.playvoice.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

// 매니저 간단 정보 DTO
record ManagerSimpleDTO(Long id, String name, String profileImageUrl) {}

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PutMapping("/me/password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest request,
                                               @AuthenticationPrincipal String userId) {
        userService.changePassword(Long.valueOf(userId), request.getOldPassword(), request.getNewPassword());
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

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteAccount(@RequestBody DeleteAccountRequest request,
                                              @AuthenticationPrincipal String userId) {
        userService.deleteAccount(Long.valueOf(userId), request.getPassword());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyProfile(@AuthenticationPrincipal String userId) {
        UserResponseDTO dto = userService.findById(Long.valueOf(userId));
        return ResponseEntity.ok(dto);
    }

    // 🔍 관리자/외부 조회
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
    public ResponseEntity<String> uploadProfileImage(@RequestParam("image") MultipartFile file,
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

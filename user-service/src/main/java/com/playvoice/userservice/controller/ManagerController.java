package com.playvoice.userservice.controller;

import com.playvoice.userservice.dto.CreateUserRequest;
import com.playvoice.userservice.dto.ResetPasswordRequest;
import com.playvoice.userservice.entity.PasswordStatus;
import com.playvoice.userservice.entity.User;
import com.playvoice.userservice.repository.UserRepository;
import com.playvoice.userservice.service.UserService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manager")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MANAGER')")
public class ManagerController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @GetMapping("/students")
    public ResponseEntity<List<User>> getAllStudents() {
        List<User> students = userRepository.findAll()
            .stream()
            .filter(user -> user.getRole().name().equals("STUDENT"))
            .toList();

        return ResponseEntity.ok(students);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetStudentPassword(@RequestBody ResetPasswordRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("Student Not Found"));

        if (!user.getRole().name().equals("STUDENT")) {
            return ResponseEntity.badRequest().body("Only students can be reset");
        }

        String tempPassword = UUID.randomUUID().toString().substring(0, 10);
        user.changePassword(passwordEncoder.encode(tempPassword));
        user.setPasswordStatus(PasswordStatus.INIT);
        userRepository.save(user);

        return ResponseEntity.ok("Password has been reset and sent to email.");
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        User user = userService.createUser(request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/students/{id}/ban")
    public ResponseEntity<Void> banStudent(@PathVariable Long id) {
        userService.banStudent(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/students/{id}/unban")
    public ResponseEntity<Void> unbanStudent(@PathVariable Long id) {
        userService.unbanStudent(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/students/{id}/reset-password")
    public ResponseEntity<Void> resetStudentPassword(@PathVariable Long id) {
        userService.resetStudentPassword(id);
        return ResponseEntity.ok().build();
    }
}

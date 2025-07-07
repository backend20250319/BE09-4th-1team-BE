package com.playvoice.userservice.controller;

import com.playvoice.userservice.dto.LoginRequest;
import com.playvoice.userservice.dto.LoginResponse;
import com.playvoice.userservice.entity.User;
import com.playvoice.userservice.jwt.JwtTokenProvider;
import com.playvoice.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Password Not Match");
        }

        String token = jwtTokenProvider.createToken(user.getUsername(), user.getRole());

        return ResponseEntity.ok(
                new LoginResponse(token, user.getUsername(), user.getRole())
        );
    }
}

package com.playvoice.userservice.controller;

import com.playvoice.userservice.dto.LoginRequest;
import com.playvoice.userservice.dto.LoginResponse;
import com.playvoice.userservice.entity.User;
import com.playvoice.userservice.jwt.JwtTokenProvider;
import com.playvoice.userservice.repository.UserRepository;
import com.playvoice.userservice.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Password Not Match");
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getUsername(), user.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUsername(), user.getRole());

        refreshTokenService.save(user.getUsername(), refreshToken);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ofHours(1))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new LoginResponse(accessToken, user.getUsername(), user.getRole(), user.getPasswordStatus()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue("refreshToken") String refreshToken) {
        String username = jwtTokenProvider.extractUsername(refreshToken);

        if (!refreshTokenService.isValid(username, refreshToken)) {
            return ResponseEntity.status(401).body("Invalid or expired refresh token");
        }

        refreshTokenService.extend(username);

        String newAccessToken = jwtTokenProvider.createAccessToken(username, jwtTokenProvider.extractRole(refreshToken));

        return ResponseEntity.ok(new LoginResponse(newAccessToken, username, jwtTokenProvider.extractRole(refreshToken), null));
    }
}

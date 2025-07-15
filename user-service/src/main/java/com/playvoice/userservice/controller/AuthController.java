package com.playvoice.userservice.controller;

import com.playvoice.userservice.dto.LoginRequest;
import com.playvoice.userservice.dto.LoginResponse;
import com.playvoice.userservice.entity.User;
import com.playvoice.userservice.jwt.JwtTokenProvider;
import com.playvoice.userservice.repository.UserRepository;
import com.playvoice.userservice.service.RefreshTokenService;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
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

        String accessToken = jwtTokenProvider.createAccessToken(user.getUsername(), user.getId(),
            user.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUsername(), user.getId(),
            user.getRole());

        refreshTokenService.save(user.getUsername(), refreshToken);

        ResponseCookie responseCookie = createRefreshTokenCookie(refreshToken);

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
            .body(new LoginResponse(accessToken, user.getUsername(), user.getRole(),
                user.getPasswordStatus(), user.getCourse(), user.getId(), user.getName()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue("refreshToken") String refreshToken) {
        String username = jwtTokenProvider.extractUsername(refreshToken);

        if (!refreshTokenService.isValid(username, refreshToken)) {
            return ResponseEntity.status(401).body("Invalid or expired refresh token");
        }

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User Not Found"));

        String newRefreshToken = jwtTokenProvider.createRefreshToken(user.getUsername(),
            user.getId(), user.getRole());
        String newAccessToken = jwtTokenProvider.createAccessToken(username, user.getId(),
            jwtTokenProvider.extractRole(refreshToken));

        refreshTokenService.save(user.getUsername(), newRefreshToken);

        ResponseCookie responseCookie = createRefreshTokenCookie(newRefreshToken);

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
            .body(new LoginResponse(newAccessToken, user.getUsername(), user.getRole(),
                user.getPasswordStatus(), user.getCourse(), user.getId(), user.getName()));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue("refreshToken") String refreshToken) {
        String username = jwtTokenProvider.extractUsername(refreshToken);

        refreshTokenService.delete(username);

        ResponseCookie deleteCookie = createDeleteRefreshTokenCookie();
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
            .body("Logout successful");
    }


    private ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
            .httpOnly(true)                     // HttpOnly 속성 설정 (JavaScript 에서 접근 불가)
            // .secure(true)                    // HTTPS 환경일 때만 전송 (운영 환경에서 활성화 권장)
            .path("/")                          // 쿠키 범위 : 전체 경로
            .maxAge(Duration.ofDays(7))         // 쿠키 만료 기간 : 7일
            .sameSite("Strict")                 // CSRF 공격 방어를 위한 SameSite 설정
            .build();
    }

    /* 쿠키 삭제용 설정
     * 빈 값 + maxAge=0 으로 즉시 만료시켜 브라우저에서 삭제
     */
    private ResponseCookie createDeleteRefreshTokenCookie() {
        return ResponseCookie.from("refreshToken", "")
            .httpOnly(true)     // HttpOnly 유지
            // .secure(true)    // HTTPS 환경에서만 사용 시 주석 해제
            .path("/")          // 동일 path 범위
            .maxAge(0)          // 즉시 만료
            .sameSite("Strict") // SameSite 유지
            .build();
    }
}

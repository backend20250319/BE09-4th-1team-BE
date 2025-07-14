package com.playvoice.userservice.service;

import com.playvoice.userservice.entity.RefreshToken;
import com.playvoice.userservice.repository.RefreshTokenRepository;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    //TODO
    public void save(String username, String token) {
        Date now = new Date();
        refreshTokenRepository.save(
            new RefreshToken(username, token, new Date(now.getTime() + 15 * 60 * 1000)));
    }

    public void delete(String username) {
        refreshTokenRepository.deleteById(username);
    }

    public boolean isValid(String username, String token) {
        RefreshToken refreshToken = refreshTokenRepository.findById(username)
            .orElseThrow(() -> new IllegalArgumentException("invalid token"));

        if (refreshToken.getToken().equals(token)) {
            return true;
        }

        return false;
    }


}

package com.playvoice.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    public void save(String username, String token) {
        redisTemplate.opsForValue().set("refresh:" + username, token, Duration.ofHours(1));
    }

    public void delete(String username) {
        redisTemplate.delete("refresh:" + username);
    }

    public boolean isValid(String username, String token) {
        String stored = redisTemplate.opsForValue().get("refresh:" + username);
        return token.equals(stored);
    }

    public void extend(String username) {
        redisTemplate.expire("refresh:" + username, Duration.ofHours(1));
    }





}

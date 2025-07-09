package com.playvoice.userservice.redis;

import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisDao {

    private final RedisTemplate<String, String> redisTemplate;

    public void setRefreshToken(String key, String token, long minutes) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(token.getClass()));
        redisTemplate.opsForValue().set(key, token, minutes, TimeUnit.MINUTES);
    }

    public String getRefreshToken(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteRefreshToken(String key) {
        redisTemplate.delete(key);
    }

    public void setBlackList(String token, String msg, Long minutes) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(msg.getClass()));
        redisTemplate.opsForValue().set(token, msg, minutes, TimeUnit.MINUTES);
    }

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}

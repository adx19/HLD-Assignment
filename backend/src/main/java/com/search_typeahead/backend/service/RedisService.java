package com.search_typeahead.backend.service;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public RedisService(StringRedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public String get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key, String value){
        redisTemplate.opsForValue().set(key, value);
    }
}

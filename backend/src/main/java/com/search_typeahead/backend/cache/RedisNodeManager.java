package com.search_typeahead.backend.cache;

import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class RedisNodeManager {

    private final HashMap<String, StringRedisTemplate> templates = new HashMap<>();

    public RedisNodeManager(){

        templates.put("redis-node-1", createTemplate(6379));
        templates.put("redis-node-2", createTemplate(6380));
        templates.put("redis-node-3", createTemplate(6381));
    }

    private StringRedisTemplate createTemplate(int port){

        LettuceConnectionFactory factory = new LettuceConnectionFactory("localhost", port);

        factory.afterPropertiesSet();

        return new StringRedisTemplate(factory);
    }

    public StringRedisTemplate getTemplate(String nodeId){
        return templates.get(nodeId);
    }
}

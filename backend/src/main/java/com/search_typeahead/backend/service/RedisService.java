package com.search_typeahead.backend.service;
import com.search_typeahead.backend.cache.CacheNode;
import com.search_typeahead.backend.cache.ConsistentHashingRing;
import com.search_typeahead.backend.cache.RedisNodeManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final RedisNodeManager nodeManager;
    private final ConsistentHashingRing ring;
    private final RedisTemplate<Object, Object> redisTemplate;

    public RedisService(RedisNodeManager nodeManager, ConsistentHashingRing ring, RedisTemplate<Object, Object> redisTemplate){
        this.nodeManager = nodeManager;
        this.ring = ring;
        this.redisTemplate = redisTemplate;
    }

    public String get(String key){
        CacheNode node = ring.getNode(key);

        System.out.println("CACHE READ: " + key + " -> " + node.getNodeId());

        StringRedisTemplate template = nodeManager.getTemplate(node.getNodeId());

        return template.opsForValue().get(key);
    }

    public void set(String key, String value){
        CacheNode node = ring.getNode(key);

        System.out.println("CACHE WRITE: " + key + " ->" + node.getNodeId());

        StringRedisTemplate template = nodeManager.getTemplate(node.getNodeId());

        template.opsForValue().set(key, value);
    }

    public void delete(String key){
        redisTemplate.delete(key);
    }


}

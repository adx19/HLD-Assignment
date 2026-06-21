package com.search_typeahead.backend.cache;

import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.SortedMap;
import java.util.TreeMap;

@Component
public class ConsistentHashingRing {

    private final TreeMap<Long, CacheNode> ring = new TreeMap<>();

    private static final int VIRTUAL_NODES = 500;

    public ConsistentHashingRing(){
        addNode(new CacheNode("redis-node-1", "localhost", 6379));

        addNode(new CacheNode("redis-node-2", "localhost", 6380));

        addNode(new CacheNode("redis-node-3", "localhost", 6381));
    }

    public void addNode(CacheNode node){
        for (int i = 0; i<VIRTUAL_NODES; i++){
            String virtualNode = node.getNodeId() + '#' + i;

            ring.put(hash(virtualNode), node);
        }
    }

    public CacheNode getNode(String key){
        long hash = hash(key);

        SortedMap<Long, CacheNode> tailMap = ring.tailMap(hash);

        Long nodeHash;

        if(tailMap.isEmpty()){
            nodeHash = ring.firstKey();
        } else{
            nodeHash = tailMap.firstKey();
        }

        return ring.get(nodeHash);
    }

    private long hash(String key){

        try{
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] digest = md.digest(key.getBytes());

            return ByteBuffer.wrap(digest).getInt() & 0xffffffffL;

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}

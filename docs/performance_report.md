# Performance Report

## Objective

The objective of this project was to improve search suggestion performance and reduce database load using caching and distributed system design techniques.

---

## Redis Caching

The system uses Redis as a cache layer in front of PostgreSQL.

### Benefits

* Faster response times
* Reduced database load
* Frequently searched prefixes are served directly from cache

### Observation

For repeated searches, cache hits were observed instead of database lookups, resulting in lower latency.

Example:

```text
First Search:
Cache Miss → PostgreSQL → Redis

Repeated Search:
Cache Hit → Redis
```

---

## Consistent Hashing

The cache layer uses consistent hashing to distribute search prefixes across multiple Redis nodes.

### Benefits

* Even distribution of cache entries
* Easy addition or removal of cache nodes
* Reduced data movement compared to traditional hashing

---

## Virtual Nodes

Initially, cache entries were unevenly distributed across Redis nodes.

### Distribution Without Virtual Nodes

```json
{
  "redis-node-3": 8474,
  "redis-node-2": 7471,
  "redis-node-1": 6788
}
```

### Distribution With 500 Virtual Nodes

```json
{
  "redis-node-3": 7545,
  "redis-node-2": 7596,
  "redis-node-1": 7592
}
```

### Observation

Virtual nodes significantly improved load balancing by creating a more uniform distribution of cache entries across Redis nodes.

---

## Batch Writes

Instead of updating PostgreSQL on every search click, updates are collected in memory and periodically flushed to the database.

### Benefits

* Reduced database write operations
* Lower database load
* Improved scalability

### Example

Instead of:

```text
5 Search Clicks
→ 5 Database Updates
```

the system performs:

```text
5 Search Clicks
→ 1 Batch Update
```

---

## Trending Score Decay

A decay mechanism is applied to trending scores to ensure that recent searches have greater influence than older searches.

### Benefits

* Trending searches remain relevant
* Older searches gradually lose influence
* Better reflection of current user behavior

---

## Conclusion

The combination of Redis caching, consistent hashing, virtual nodes, batch writes, and trending score decay improved system scalability and reduced database load while maintaining fast search response times.

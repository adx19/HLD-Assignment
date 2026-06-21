# Design Choices and Trade-offs

## 1. Why Redis?

### Choice

Redis was used as a cache layer between the application and PostgreSQL.

### Benefits

* Very fast read operations
* Reduced database load
* Improved response time for frequently searched prefixes

### Trade-off

* Additional infrastructure to manage
* Cached data can become stale if not updated properly

---

## 2. Why Consistent Hashing?

### Choice

Consistent hashing was used to distribute cache entries across multiple Redis nodes.

### Benefits

* Better scalability
* Easy addition of new cache nodes
* Minimal key redistribution when nodes change

### Trade-off

* More complex than using a single Redis instance
* Additional logic required for node selection

---

## 3. Why Virtual Nodes?

### Choice

Virtual nodes were added to improve load balancing across Redis nodes.

### Benefits

* More even distribution of cache entries
* Better utilization of cache resources
* Reduced hotspot formation

### Trade-off

* Slightly larger hash ring
* Additional memory used for virtual node mappings

---

## 4. Why Batch Writes?

### Choice

Search count updates are collected and periodically written to PostgreSQL.

### Benefits

* Fewer database write operations
* Improved scalability
* Reduced database workload

### Trade-off

* Database values are not updated immediately
* Small risk of losing in-memory updates if the application crashes before a flush occurs

---

## 5. Why Trending Score Decay?

### Choice

Trending searches are ranked using a score that gradually decreases over time.

### Benefits

* Recent searches receive more importance
* Trending results stay relevant
* Prevents older popular searches from permanently dominating the ranking

### Trade-off

* Requires periodic background processing
* Trending scores change over time even without new searches

---

## Overall Design Decision

The system prioritizes low latency and scalability by combining Redis caching, consistent hashing, virtual nodes, and batch writes. While these techniques add implementation complexity, they significantly reduce database load and improve performance for a large number of search requests.

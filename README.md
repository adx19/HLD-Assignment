# Distributed Search TypeAhead System

**Name:** Abhijit P

**Batch:** A

**Roll No:** 24BCS10175

## Project Details:

**This project is a distributed search typeahead system. The system provides real-time search suggestions as users type in the search box. The main focus of the project is not only fast search suggestions but also the design decisions used to improve scalability and performance.**

**PostgreSQL is used to store search data, while Redis is used as a cache layer to reduce database load and improve response time. Multiple Redis nodes are connected using consistent hashing with virtual nodes to distribute cached data evenly across the cluster.**

**The system also includes trending searches, search history, keyboard navigation, batch writes to reduce database updates, and a decay mechanism to ensure that trending searches reflect recent user activity**

**The project demonstrates several HLD concepts including caching, cache distribution, load balancing through virtual nodes, write optimization using batching, and trending data management**

---

## Features:

- Real-time search suggestions
- Prefix-based search
- Trending searches
- Keyboard navigation
- Search latency tracking
- Redis caching
- Consistent hashing
- Virtual nodes
- Batch writes
- Trending score decay

---

## HLD Concepts Implemented:

- Distributed caching using Redis
- Consistent hashing for cache distribution
- Virtual nodes for improved load balancing 
 - Batch writes for reducing database updates
 - Cache partitioning across multiple Redis nodes
 - Trending score decay for dynamic trending searches

 ---

 ## Tech Stack:

 ### Frontend:
 - *React*
 - *Vite*

 ### Backend:
 - *Spring Boot*
 - *Spring Data JPA*

 ### Database:
 - *PostgreSQL*

 ### Cache Layer:
 - *Redis*

### Other Tools:
- *Docker*
- *Python*

---

## System Architecture:

**The user interacts with the React frontend, which sends requests to the Spring Boot backend.**

**When a search request is received, the backend first checks the Redis cache cluster. If the result is found in Redis, it is returned immediately. If the results is not found, the data is fetched from PostgreSQL and stored in the Redis cache for future requests.**

**To distribute cache data scross multiple Redis nodes, consistent hashing with virtual nodes is used. This help sachieve a more balances distribution of cache entries.**

**Search count updates are collected in memory and periodically written to PostgreSQL using batch writes. Trending searches are maintained using a trending score, and a decay mechanism is applied periodically so that recent searches have more influence than older searches.**

---

## Project Structure:
***backend/ - Spring Boot backend source code***

***frontend/ - React frontend source code***

***dataset/ - AOL dataset and preprocessing scripts***

***README.md - Main project documentation*** 

---

## Dataset:

**This project uses the AOL Search Query Dataset.**

**The original dataset is stored in the dataset folder. A Python preprocessing script was used to extract search queries and their frequencies from the dataset.**

**The generated queries.csv file is loaded into PostgreSQL during application startup if the database is empty.**

---

## Setup Instructions

### Start PostgreSQL

```bash
docker start search-postgres
```

### Start Redis Nodes

```bash
docker start redis-cache
docker start redis-node-2
docker start redis-node-3
```

### Run Backend

```bash
cd backend
./mvnw spring-boot:run
```

### Run Frontend

```bash
cd frontend
npm install
npm run dev
```

After starting both services, open the frontend URL shown by Vite in your browser.

---

## API Endpoints

| Method | Endpoint                    | Description                   |
| ------ | --------------------------- | ----------------------------- |
| GET    | /search?q={prefix}          | Returns search suggestions    |
| POST   | /search/click?query={query} | Records a selected search     |
| GET    | /trending                   | Returns trending searches     |
| GET    | /history                    | Returns recent search history |

---

## Future Improvements

* Add more Redis cache nodes
* Deploy cache nodes on separate machines
* Implement personalized search suggestions
* Improve search ranking algorithms
* Add advanced analytics and monitoring

```
```

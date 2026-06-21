# API Documentation

## Base URL

```text id="nyxaq8"
http://localhost:8080
```

---

## 1. Search Suggestions

### Endpoint

```http id="6kjyzz"
GET /search?q={prefix}
```

### Description

Returns search suggestions and trending results for a given prefix.

### Example Request

```http id="nvntq6"
GET /search?q=goo
```

### Example Response

```json id="xg0zq8"
{
  "suggestions": [
    {
      "query": "google",
      "count": 32403
    }
  ],
  "trending": [
    {
      "query": "google",
      "trendingScore": 29162.7
    }
  ],
  "latencyMs": 4
}
```

---

## 2. Record Search Click

### Endpoint

```http id="zqvw3e"
POST /search/click?query={query}
```

### Description

Records a selected search query. The query count and trending score are updated and added to the batch write queue.

### Example Request

```http id="6i7mq1"
POST /search/click?query=google
```

### Example Response

```text id="4n55te"
Search recorded successfully
```

---

## 3. Trending Searches

### Endpoint

```http id="6pjxt7"
GET /trending
```

### Description

Returns the top trending searches based on trending score.

### Example Request

```http id="l9v9cl"
GET /trending
```

### Example Response

```json id="3bcgii"
[
  {
    "query": "google",
    "trendingScore": 29162.7
  }
]
```

---

## 4. Search History

### Endpoint

```http id="hjlwm0"
GET /history
```

### Description

Returns the most recent search history.

### Example Request

```http id="yvgvy0"
GET /history
```

### Example Response

```json id="e00sd0"
[
  {
    "query": "google",
    "searchedAt": "2026-06-21T12:16:11.894937"
  }
]
```

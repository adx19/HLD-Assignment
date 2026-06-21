import { useState, useEffect } from "react";
import "./App.css";

const BACKEND_URL = import.meta.env.VITE_BACKEND_URL || 'http://localhost:8080';

function App() {
  const [query, setQuery] = useState("");
  const [suggestions, setSuggestions] = useState([]);
  const [prefixTrending, setPrefixTrending] = useState([]);
  const [searchedTerm, setSearchedTerm] = useState("");
  const [results, setResults] = useState([]);
  const [trending, setTrending] = useState([]);
  const [latency, setLatency] = useState(0);

  const filteredSuggestions = suggestions.filter(
    suggestion => !prefixTrending.some(trendingItem => trendingItem.query.toLowerCase() === suggestion.query.toLowerCase())
  );
  
  useEffect(() => {
    fetch(`${BACKEND_URL}/trending`)
    .then((res) => res.json())
    .then((data) => setTrending(data))
    .catch(console.error);
  }, []);

  useEffect(() => {
    if (!query.trim()){
      setSuggestions([]);
      return;
    }

    fetch(`${BACKEND_URL}/search?q=${query}`).then((res) => res.json()).then((data) => { 
      setSuggestions(data.suggestions || []);
      setPrefixTrending(data.trending || []);
      setLatency(data.latencyMs || 0);
    })
    .catch((err) => console.error(err));
  }, [query]);

  const performSearch = async (searchQuery) => {
    console.log("Searching: ", searchQuery);

    await fetch(`${BACKEND_URL}/search/click?query=${encodeURIComponent(searchQuery)}`, {
      method: "POST",
    });
    
    setResults(
      suggestions.map(item => ({
        title:item.query, 
        url: `https://www.google.com/search?q=${encodeURIComponent(item.query)}`
      }))
    )
    setSearchedTerm(searchQuery);
    setQuery("");
    setSuggestions([]);
  }

  return (
    <div className="container">
      <h1 className="title">Search Typeahead</h1>

      <div className="search-container">
       <div className="search-bar">
          <input
            type="text"
            placeholder="Search..."
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === "Enter") {
                performSearch(query);
              }
            }}
            className="search-input"
          />

          <button
            className="search-button"
            onClick={() => performSearch(query)}
          >
            Search
          </button>
        </div>
      
      {query && (
        <div className="suggestions">
            {prefixTrending.length > 0 && (
              <>
                <div className="trending-header">
                  Trending Searches
                </div>

                {prefixTrending.map((item, index) => (
                  <div
                    key={`trend-${index}`}
                    className="trending-item"
                    onClick={() => performSearch(item.query)}
                  >
                    📈 {item.query}
                  </div>
                ))}
              </>
            )}
            {filteredSuggestions.length > 0 ? (
              filteredSuggestions.map((item, index) => (
                <div key={index} className="suggestion-item" onClick={() => performSearch(item.query)}>
                  {item.query}
                </div>
              ))
            ) : (
              <div className="suggestion-item">
                No suggestions found
              </div>
            )}
          </div>
        )}
      </div>
      {searchedTerm && (
        <div className="search-result-card">
          ✓ Returned results for <strong>{searchedTerm}</strong> in <strong>{latency}ms</strong>
        </div>
      )}

      {results.length > 0 && (
        <div className="results-container">
          <h3>Results</h3>

          {results.map((result, index) =>(
            <div key={index} className="result-item">
              <a href={result.url} target="_blank" rel="noreferrer">{result.title}</a>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default App;
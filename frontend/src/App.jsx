import { useState, useEffect } from "react";
import "./App.css";

function App() {
  const [query, setQuery] = useState("");
  const [suggestions, setSuggestions] = useState([]);

  useEffect(() => {
    if (!query.trim()){
      setSuggestions([]);
      return;
    }

    fetch(`http://localhost:8080/search?q=${query}`).then((res) => res.json()).then((data) => setSuggestions(data)).catch((err) => console.error(err));
  }, [query]);

  return (
    <div className="container">
      <h1 className="title">Search Typeahead</h1>

      <div className="search-container">
        <input
          type="text"
          placeholder="Search..."
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          className="search-input"
        />

        {query && (
          <div className="suggestions">
            {suggestions.length > 0 ? (
              suggestions.map((item, index) => (
                <div key={index} className="suggestion-item">
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
    </div>
  );
}

export default App;
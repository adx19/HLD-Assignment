function SuggestionList({ suggestions }) {
  return (
    <ul>
      {suggestions.map((item, index) => (
        <li key={index}>{item}</li>
      ))}
    </ul>
  );
}

export default SuggestionList;
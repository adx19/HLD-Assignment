from collections import Counter
import csv

counts = Counter()

with open("../../../Downloads/aol_query_dataset/user-ct-test-collection-02.txt", "r", encoding="utf-8") as f:
	next(f)

	for line in f:
		parts = line.strip().split("\t")

		if len(parts) >= 2:

			query = parts[1].strip().lower()

			if query:
				counts[query] += 1

with open("queries.csv", "w", newline="", encoding="utf-8") as f:
	writer = csv.writer(f)

	writer.writerow(["query", "count"])

	for query, count in counts.most_common():
		writer.writerow([query, count])

print("Done")


# Dataset Information

## Source

This project uses the AOL Search Query Dataset.

The original dataset contains user search queries collected from AOL search logs.

## Preprocessing

The file `process_aol.py` was used to process the original dataset.

The script extracts:

* Search query
* Query frequency

The processed output is stored as `queries.csv`.

## Generated Dataset

The generated dataset contains:

* query
* count

where:

* query = search text
* count = number of occurrences in the dataset

## Usage in the Project

The generated `queries.csv` file is loaded into PostgreSQL when the Spring Boot application starts.

The search typeahead system uses this data to generate suggestions, trending searches, and search rankings.

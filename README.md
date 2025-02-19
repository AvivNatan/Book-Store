# Books Server Project

This repository contains a comprehensive implementation of a Books Server developed as part of a computer science degree program. The project is divided into three main exercises that build upon each other to create a full-featured books management system.

## Project Overview

The Books Server is an HTTP server that manages a books inventory with various operations:
- Create, Read, Update, and Delete (CRUD) operations for books
- Filtering and searching capabilities
- Logging system
- Database persistence using both SQL (PostgreSQL) and NoSQL (MongoDB) databases

## Repository Structure

The repository is organized into branches:
- `main` branch: Contains the first two exercises (HTTP Server and Logging)
- `database` branch: Contains the third exercise (Database Integration)

## Exercise 1: HTTP Server Implementation

The first exercise implements a basic HTTP server with the following endpoints:

### Endpoints:

1. **Health Check**
   - `GET /books/health`
   - Returns: `OK` string response

2. **Create a New Book**
   - `POST /book`
   - Body: JSON with book details (title, author, year, price, genres)
   - Returns: New book ID or error message

3. **Get Total Books Count**
   - `GET /books/total`
   - Optional query parameters for filtering
   - Returns: Count of books matching criteria

4. **Get Books Data**
   - `GET /books`
   - Optional query parameters for filtering
   - Returns: Array of book objects sorted by title

5. **Get Single Book Data**
   - `GET /book?id=<bookId>`
   - Returns: Single book object

6. **Update Book Price**
   - `PUT /book?id=<bookId>&price=<newPrice>`
   - Returns: Old price

7. **Delete Book**
   - `DELETE /book?id=<bookId>`
   - Returns: Number of remaining books

### Book Properties:
- ID: Unique integer identifier
- Title: String (case-insensitive for comparisons)
- Author: String
- Print Year: Integer (1940-2100)
- Price: Positive integer
- Genres: Array of strings from predefined list ["SCI_FI", "NOVEL", "HISTORY", "MANGA", "ROMANCE", "PROFESSIONAL"]

## Exercise 2: Logging System

The second exercise extends the first by adding a comprehensive logging system:

### Logging Features:
- Request logging with timestamps and request numbers
- Operation logging for all book operations
- Configurable log levels (ERROR, INFO, DEBUG)
- Multiple log targets (console and files)

### New Endpoints:
1. **Get Logger Level**
   - `GET /logs/level?logger-name=<loggerName>`
   - Returns: Current log level

2. **Set Logger Level**
   - `PUT /logs/level?logger-name=<loggerName>&logger-level=<loggerLevel>`
   - Returns: Updated log level

### Loggers:
- **request-logger**: Logs all incoming requests
  - Target: requests.log and console
  - Default level: INFO
  
- **books-logger**: Logs book inventory operations
  - Target: books.log
  - Default level: INFO

## Exercise 3: Database Integration

The third exercise (in the `database` branch) integrates database persistence using ORM technology.

### Database Features:
- Dual persistence in PostgreSQL and MongoDB
- Query parameter (`persistenceMethod`) to select data source
- Synchronized data between both databases
- Docker containerization for all components

### Database Configuration:
- **PostgreSQL**:
  - Port: 5432
  - Username: postgres
  - Password: docker
  - Database: books
  - Table: books

- **MongoDB**:
  - Port: 27017
  - Database: books
  - Collection: books

## Running the Project

### Prerequisites:
- For basic server: Node.js/Python/Java or your preferred language runtime
- For database integration: Docker and docker-compose

### Running the Basic Server (Exercise 1 & 2):
1. Clone the repository and ensure you're on the `main` branch
2. Run the server using the provided script:
```bash
./run.bat
```
3. Server will be available at http://localhost:8574

### Running with Database Integration (Exercise 3):
1. Switch to the `database` branch
2. Start the containers using docker-compose:
```bash
docker-compose up
```
3. Server will be available at http://localhost:4785

## Testing
The repository includes Postman collections for testing each exercise. Import these collections along with their environment files to test the functionality.

## Technical Stack
- Server Framework: Custom implementation
- Logging: Custom implementation using industry-standard logging libraries
- Database: PostgreSQL and MongoDB with ORM
- Containerization: Docker and docker-compose

## Development Notes
- The server handles all requests serially (no multi-threading)
- Books are uniquely identified by their titles (case-insensitive)
- Error handling follows HTTP status code conventions
- Docker images are stored on Docker Hub

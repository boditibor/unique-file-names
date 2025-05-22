# Unique File Names Application

## Overview

The Unique File Names Application is a Spring Boot-based REST API for managing and analyzing file structures. It
provides endpoints to:

- Retrieve unique file names and their counts from a directory
- View the history of file operations
- Generate folder/file structures on the server
- Access generated Javadoc documentation
- Explore OpenAPI/Swagger UI for API documentation

## Technologies Used

- Java 21
- Spring Boot 3
- Gradle (Kotlin DSL)
- PostgreSQL
- Docker & Podman
- Springdoc OpenAPI (Swagger UI)
- JUnit & Mockito

## Endpoints

### 1. Get Unique Files

- **GET** `/api/v1/get_unique?path=<directory_path>`
- **Description:** Returns a map of unique file names and their occurrence counts in the specified directory.
- **Example Response:**
  ```json
  {
    "file1.txt": 2,
    "file2.txt": 1
  }
  ```

### 2. Get History

- **GET** `/api/v1/history`
- **Description:** Returns a list of file operation history records.
- **Example Response:**
  ```json
  [
    {
      "userName": "root",
      "timestamp": "2024-05-22T12:34:56",
      "requestedPath": "/app/generated_structure"
    }
  ]
  ```

### 3. Generate Folder Structure

- **POST** `/api/v1/gen`
- **Description:** Generates folders and files based on the provided structure list.
- **Request Body Example:**
  ```json
  [
    "file1.txt",
    "folder1/",
    "folder1/folder2/file2.txt"
  ]
  ```
- **Response:**
    - `"Folder structure created successfully"` on success

### 4. Javadoc Documentation

- **GET** `/api/v1/doc`
- **Description:** Redirects to the generated Javadoc index page.
- **GET** `/api/v1/doc/index.html`
- **Description:** Serves the Javadoc HTML documentation.

### 5. Swagger UI

- **GET** `/swagger-ui`
- **Description:** Opens the Swagger UI for exploring and testing the API.

## How to Build and Run

### Prerequisites

- [Podman](https://podman.io/) or Docker
- [Podman Compose](https://github.com/containers/podman-compose) or Docker Compose
- GNU Make

### Environment Variables

Create a `.env` file in the project root with:

```
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
```

### Build and Run (using Podman)

1. **Build the application and Docker image:**
   ```sh
   make build
   ```
2. **Start the containers:**
   ```sh
   make run
   ```
3. **View logs:**
   ```sh
   make logs
   ```
4. **Stop the containers:**
   ```sh
   make stop
   ```

### Accessing the Application

- **Swagger UI:** [http://localhost:8081/swagger-ui](http://localhost:8081/swagger-ui)
- **Javadoc:** [http://localhost:8081/api/v1/doc](http://localhost:8081/api/v1/doc)
- **API Endpoints:** Use Swagger UI or any HTTP client (e.g., curl, Postman)

**Note:**

- The application runs two instances (app1 and app2) on ports 8081 and 8082, both connected to the same PostgreSQL
  database. You can access either instance at [http://localhost:8081](http://localhost:8081)
  or [http://localhost:8082](http://localhost:8082).

## Example Usage

**Get unique files:**

```sh
curl "http://localhost:8081/api/v1/get_unique?path=/app/generated_structure"
```

**Generate folder structure:**

```sh
curl -X POST "http://localhost:8081/api/v1/gen" \
  -H "Content-Type: application/json" \
  -d '["file1.txt","folder1/","folder1/folder2/file2.txt"]'
```

**Get history:**

```sh
curl "http://localhost:8081/api/v1/history"
```

## Notes

- All file operations inside the container are relative to `/app/generated_structure`.
- The application is fully containerized; you do not need Java or Gradle installed on your host.
- The Javadoc is generated and included in the Docker image during the build process.


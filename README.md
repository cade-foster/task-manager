# Task Manager Service

A lightweight Task Manager service built with a Spring Boot backend and React TypeScript frontend, fully containerized with Docker.

## Architecture Overview

- **Backend**: Spring Boot REST API with H2 in-memory database
- **Frontend**: React TypeScript application with Axios for API communication
- **Containerization**: Docker containers orchestrated with Docker Compose
- **Testing**: Unit tests for service layer and integration tests for controllers

## Persistence Layer Choice

**H2 In-Memory Database** was chosen for this implementation because:
- **Simplicity**: No external database setup required
- **Development Speed**: Quick startup and testing
- **Lightweight**: Perfect for internal use and demos
- **Spring Boot Integration**: Seamless integration with JPA
- **Testing**: Ideal for unit and integration tests

For production use, this can easily be switched to PostgreSQL or MySQL by changing the database configuration in `application.properties`.

## Project Structure

```
task-manager/
├── backend/                 # Spring Boot API
│   ├── src/main/java/
│   │   └── com/taskmanager/
│   │       ├── controller/  # REST Controllers
│   │       ├── model/       # Entity models
│   │       ├── repository/  # Data access layer
│   │       └── service/     # Business logic
│   └── src/test/java/       # Unit and integration tests
|
├── frontend/                # React TypeScript UI
│   ├── src/
│   │   ├── components/      # React components
│   │   ├── services/        # API service layer
│   │   └── types/           # TypeScript type definitions
│   └── public/              # Static assets
└── docker-compose.yml       # Container orchestration
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/tasks` | Get all tasks |
| POST | `/tasks` | Create a new task |
| PUT | `/tasks/{id}` | Update a task |
| DELETE | `/tasks/{id}` | Delete a task |

### Task Object
```json
{
  "id": "uuid",
  "title": "string (required)",
  "description": "string (optional)",
  "status": "TODO | IN_PROGRESS | DONE"
}
```

## Prerequisites

- **Docker** and **Docker Compose** installed
- **Java 17+** (for local development)
- **Node.js 18+** (for local development)
- **Maven** (for local development)

## Quick Start with Docker

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd taskmanager
   ```

2. **Build and run with Docker Compose**
   ```bash
   docker-compose up --build
   ```
   _on mac_os this may first require `brew install docker-compose` and `brew install docker-buildx`_


3. **Access the application**
   - Frontend UI: http://localhost:3000
   - Backend API: http://localhost:8080
   - H2 Console: http://localhost:8080/h2-console
     - JDBC URL: `jdbc:h2:mem:testdb`
     - Username: `sa`
     - Password: `password`

## Local Development

### Backend Development

1. **Navigate to backend directory**
   ```bash
   cd backend
   ```

2. **Run tests**
   ```bash
   ./mvnw test
   ```

3. **Start the application**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Access the API**
   - API Base URL: http://localhost:8080
   - H2 Console: http://localhost:8080/h2-console

### Frontend Development

1. **Navigate to frontend directory**
   ```bash
   cd frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Start development server**
   ```bash
   npm start
   ```

4. **Access the UI**
   - Frontend URL: http://localhost:3000

## Testing

### Backend Tests

The backend includes comprehensive test coverage:

- **Unit Tests**: Service layer tests with mocked dependencies
- **Integration Tests**: Controller tests with `@WebMvcTest`

```bash
cd backend
./mvnw test
```

### Test Coverage Includes:
- Task creation, retrieval, update, and deletion
- Input validation (e.g., title must not be blank)
- HTTP status code validation
- Service layer business logic

## Building for Production

### Backend
```bash
cd backend
./mvnw clean package
```

### Frontend
```bash
cd frontend
npm run build
```

## Docker Commands

### Build individual containers
```bash
# Backend
docker build -t taskmanager-backend ./backend

# Frontend
docker build -t taskmanager-frontend ./frontend
```

### Run with Docker Compose
```bash
# Start services
docker-compose up

# Start services in background
docker-compose up -d

# Stop services
docker-compose down

# Rebuild and start
docker-compose up --build
```

## API Usage Examples

### Create a Task
```bash
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Hire Cade",
    "description": "He sure knocked this project out of the park!",
    "status": "TODO"
  }'
```

### Get All Tasks
```bash
curl http://localhost:8080/tasks
```

### Update a Task
```bash
curl -X PUT http://localhost:8080/tasks/{task-id} \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Updated task title",
    "description": "Updated description",
    "status": "DONE"
  }'
```

### Delete a Task
```bash
curl -X DELETE http://localhost:8080/tasks/{task-id}
```

## Frontend Features

- **View Tasks**: Display all tasks with status indicators
- **Add Tasks**: Create new tasks with title input
- **Mark as Done**: Update task status to completed
- **Delete Tasks**: Remove tasks from the list
- **Responsive Design**: Works on desktop and mobile devices

## Configuration

### Backend Configuration
Edit `backend/src/main/resources/application.properties`:
```properties
# Database configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=password

# Server configuration
server.port=8080
```

### Frontend Configuration
The frontend automatically connects to the backend API at `http://localhost:8080`.

## Troubleshooting

### Common Issues

1. **Port conflicts**: Ensure ports 3000 and 8080 are available
2. **Docker issues**: Run `docker-compose down` and `docker-compose up --build`
3. **CORS issues**: Backend includes CORS configuration for localhost:3000

### Logs
```bash
# View container logs
docker-compose logs backend
docker-compose logs frontend

# Follow logs
docker-compose logs -f
```

## Future Enhancements

- Add user authentication
- Implement task due dates
- Add task categories/tags
- Integrate with external database (PostgreSQL)
- Add task search and filtering
- Implement task assignments
- Add email notifications

## Technology Stack

- **Backend**: Java 17, Spring Boot 3.1, Spring Data JPA, H2 Database, Maven
- **Frontend**: React 18, TypeScript, Axios, CSS3
- **Testing**: JUnit 5, Mockito, Spring Boot Test
- **Containerization**: Docker, Docker Compose
- **Build Tools**: Maven, npm

## License

This project is licensed under the MIT License.

# Globetrotter

Globetrotter is a full-stack web application where users guess destinations based on cryptic clues. It includes animated feedback, score tracking, and a 'Challenge a Friend' feature with dynamic invite links.

## Tech Stack

### Backend
- **Language:** Java
- **Framework:** Spring Boot
- **Database:** H2 (In-Memory for Development)
- **ORM:** Hibernate (JPA)
- **Build Tool:** Maven
- **API Documentation:** Swagger
- **Authentication:** Spring Security (JWT if needed)

### Frontend
- **Framework:** React (or vanilla HTML/CSS/JS)
- **State Management:** Context API / Redux
- **Animations:** Framer Motion
- **Styling:** TailwindCSS / Bootstrap

### DevOps
- **Version Control:** Git & GitHub
- **Containerization:** Docker (Optional)

## Folder Structure
```
/globetrotter-challenge
│── globetrotter-be/        # Spring Boot Backend
│── globetrotter-frontend/       # React or HTML/CSS/JS Frontend
│── README.md       # Documentation
```

## Installation

### Prerequisites
- **Java 17+**
- **Maven**
- **Node.js & npm**
- **Git**

### Backend Setup
```sh
git clone <repo-url>
cd globetrotter/backend
```

#### Configure Database
H2 (In-Memory) is used for development. No manual setup is required.
Update `application.properties` if needed:
```properties
spring.datasource.url=jdbc:h2:mem:globetrotter
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

Run the application:
```sh
mvn spring-boot:run
```
Verify API is running at `http://localhost:8080/api`. H2 console is available at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:globetrotter`).

### Frontend Setup
```sh
cd globetrotter-frontend
npm install
```

#### Configure API Endpoint
Update API calls in the frontend to use `http://localhost:8080/api`:
```javascript
const API_BASE_URL = "http://localhost:8080/api";
```

Start the frontend server:
```sh
npm start
```
Access the app at `http://localhost:3000`.

## Contributing
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to your branch
5. Open a Pull Request


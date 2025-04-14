# Expense Tracker API

## Overview
The Expense Tracker API is a Spring Boot application designed to help users manage their expenses. It provides functionalities for user registration, login, expense management (create, read, update, delete), and generating monthly expense reports. The application uses an MySql in-memory database for data persistence and includes basic exception handling and unit tests.

## Features
- **User Management**:
  - Register a new user with name, email, and password.
  - Login using email and password.
- **Expense Management**:
  - Create, read, update, and delete expenses.
  - Each expense includes amount, category, description (optional), and date (defaults to current date).
- **Business Logic**:
  - Calculate total expenses for a user within a specified date range.
  - Summarize expenses by category.
- **Reporting**:
  - Generate a monthly expense report with total expenses and category breakdown.
- **Data Storage**:
  - Uses MySql in-memory database with Spring Data JPA.
- **Exception Handling**:
  - Global exception handling for user-friendly error messages.
- **Testing**:
  - Basic unit tests for the service layer using JUnit and Mockito.

## Prerequisites
- Java 17 or higher
- Maven 3.6+
- IDE (e.g., IntelliJ IDEA, Eclipse) or terminal for building/running the project

## Setup Instructions
1. **Clone the Repository**:
   ```bash
   git clone <repository-url>
   cd expense-tracker-api
   ```

2. **Build the Project**:
   ```bash
   mvn clean install
   ```

3. **Run the Application**:
   ```bash
   mvn spring-boot:run
   ```
   The application will start on `http://localhost:8080`.

4. **Configure MySQL database**:
   ```application.properties
   spring.application.name=expenseTracker
   server.port=8080
   server.servlet.context-path=/expense-tracker/api
   spring.datasource.name=expense_tracker
   spring.datasource.url=jdbc:mysql://localhost:3306/expense_tracker
   spring.datasource.username=root
   spring.datasource.password=your_password
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   ```


## API Endpoints
### Authentication
- **Register User**:
  - `POST expense-tracker/api/auth/register`
  - Body: `{"name": "John Doe", "email": "john@example.com", "password": "pass123"}`
- **Login**:
  - `POST expense-tracker/api/auth/login`
  - Body: `{"email": "john@example.com", "password": "pass123"}`

### Expenses : Pass Basic Auth To Access End Points
- **Create Expense**:
  - `POST expense-tracker/api/expense`
  - Body: `{"amount": 10.0, "category": "Food", "description": "Lunch", "date": "2023-10-01"}`
- **Get All Expenses**:
  - `GET expense-tracker/api/expense`
- **Get Expense by ID**:
  - `GET expense-tracker/api/expense/<id>`
- **Update Expense**:
  - `PUT expense-tracker/api/expense/<id>`
  - Body: `{"amount": 15.0, "category": "Travel", "description": "Taxi"}`
- **Delete Expense**:
  - `DELETE expense-tracker/api/expense/<id>`
- **Get Total Expenses**:
  - `GET expense-tracker/api/expense/total?startDate=2023-10-01&endDate=2023-10-31`
- **Get Category Totals**:
  - `GET expense-tracker/api/expense/categories`

### Reports : Pass Basic Auth To Access End Points
- **Monthly Report**:
  - `GET expense-tracker/api/reports/monthly?year=2023&month=10`

## Testing
- Unit tests are located in `src/test/java`.
- Run tests using:
  ```bash
  mvn test
  ```

## Project Structure
```
src
├── main
│   ├── java
│   │   └── com.example.expensetracker
    |       ├── config        # Security Config
│   │       ├── controller    # REST controllers
│   │       ├── entity        # JPA entities (User, Expense)
│   │       ├── exception     # Global exception handler
│   │       ├── repository    # Spring Data JPA repositories
│   │       ├── service       # Business logic
│   ├── resources
│   │   └── application.properties  # mySql database configuration
├── test
│   └── java
│       └── com.example.expensetracker
│           └── service       # Unit tests
```



## Contact
For any questions, please reach out to the repository collaborators.


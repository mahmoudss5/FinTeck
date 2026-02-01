# AzBank - FinTech Banking System

A full-stack banking application built with Spring Boot and React, providing digital wallet management, fund transfers, loan applications, and customer support features.

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Backend Setup](#backend-setup)
  - [Frontend Setup](#frontend-setup)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Security](#security)

## Features

- **User Management**: Registration, authentication, and profile management
- **Digital Wallets**: Multi-currency wallet support with real-time balance tracking
- **Fund Transfers**: Secure money transfers between wallets with optimistic locking
- **Loan Applications**: Submit and track loan applications with status updates
- **Transaction History**: View transaction history with monthly report generation
- **Support Tickets**: Customer support system with ticket tracking and responses
- **Audit Logging**: Track user actions for security and compliance

## Tech Stack

### Backend

| Technology        | Version | Purpose                          |
| ----------------- | ------- | -------------------------------- |
| Java              | 21      | Programming Language             |
| Spring Boot       | 3.4.1   | Application Framework            |
| Spring Security   | -       | Authentication & Authorization   |
| Spring Data JPA   | -       | Data Persistence                 |
| MySQL             | 8.x     | Production Database              |
| Flyway            | -       | Database Migrations              |
| Lombok            | -       | Code Generation                  |
| SpringDoc OpenAPI | 2.7.0   | API Documentation                |
| Spring Retry      | 2.0.6   | Retry Mechanism for Transactions |

### Frontend

| Technology   | Version | Purpose             |
| ------------ | ------- | ------------------- |
| React        | 19.2.0  | UI Framework        |
| Vite         | 7.2.4   | Build Tool          |
| React Router | 7.13.0  | Client-side Routing |
| Tailwind CSS | 4.1.18  | Styling             |

## Project Structure

```
finTech/
├── BackEnd/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/BankSystem/demo/
│   │   │   │   ├── BusinessLogic/
│   │   │   │   │   ├── Services/          # Service interfaces
│   │   │   │   │   └── ServicesImp/       # Service implementations
│   │   │   │   ├── Config/                # Security, Exception handling
│   │   │   │   ├── Controllers/           # REST API endpoints
│   │   │   │   └── DataAccessLayer/
│   │   │   │       ├── DTOs/              # Data Transfer Objects
│   │   │   │       ├── Entites/           # JPA Entities
│   │   │   │       └── Repositories/      # Spring Data Repositories
│   │   │   └── resources/
│   │   │       ├── application.properties # App configuration
│   │   │       └── db/migration/          # Flyway migrations
│   │   └── test/                          # Unit & Integration tests
│   └── pom.xml
│
└── FrontEnd/cashMe/
    ├── src/
    │   ├── Components/                    # Reusable UI components
    │   ├── Pages/                         # Page components
    │   ├── App.jsx                        # Main app component
    │   └── main.jsx                       # Entry point
    ├── package.json
    └── vite.config.js
```

## Getting Started

### Prerequisites

- **Java 21** or higher
- **Node.js 18+** and npm
- **MySQL 8.x**
- **Maven 3.8+** (or use included wrapper)

### Backend Setup

1. **Clone the repository**

   ```bash
   git clone <repository-url>
   cd finTech
   ```

2. **Configure the database**

   Create a MySQL database:

   ```sql
   CREATE DATABASE azbanking;
   ```

3. **Update database credentials**

   Edit `BackEnd/src/main/resources/application.properties`:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/azbanking
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

4. **Run the application**

   ```bash
   cd BackEnd
   ./mvnw spring-boot:run
   ```

   The backend will start on `http://localhost:8080`

5. **Access Swagger UI**

   Open `http://localhost:8080/swagger-ui.html` for API documentation

### Frontend Setup

1. **Install dependencies**

   ```bash
   cd FrontEnd/cashMe
   npm install
   ```

2. **Start development server**

   ```bash
   npm run dev
   ```

   The frontend will start on `http://localhost:5173`

## API Documentation

Interactive API documentation is available via Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```

## Database Schema

### Core Entities

| Entity                    | Description                                 |
| ------------------------- | ------------------------------------------- |
| **User**                  | User accounts with authentication details   |
| **Role**                  | User roles for access control               |
| **Wallet**                | Digital wallets with multi-currency support |
| **Transaction**           | Fund transfer records                       |
| **LoanApplication**       | Loan request details and status             |
| **SupportTicket**         | Customer support tickets                    |
| **SupportTicketResponse** | Responses to support tickets                |
| **AuditLog**              | System activity audit trail                 |

### Database Migrations

Migrations are managed by Flyway and located in `src/main/resources/db/migration/`:

| Migration                          | Description                                                      |
| ---------------------------------- | ---------------------------------------------------------------- |
| V1\_\_init_schema.sql              | Initial schema (users, roles, wallets, transactions, audit_logs) |
| V2\_\_Add_version_to_wallet.sql    | Optimistic locking support                                       |
| V3\_\_Add_Reports_Table.sql        | Support ticket system                                            |
| V4\_\_Add_ReportResponse_table.sql | Ticket responses                                                 |
| V5\_\_Add_Loan_Table.sql           | Loan applications                                                |

## API Endpoints

### Authentication (`/auth/api`)

| Method | Endpoint    | Description       |
| ------ | ----------- | ----------------- |
| POST   | `/register` | Register new user |
| POST   | `/login`    | User login        |

### Users (`/user/api`)

| Method | Endpoint          | Description     |
| ------ | ----------------- | --------------- |
| GET    | `/{userId}`       | Get user by ID  |
| GET    | `/all`            | Get all users   |
| POST   | `/updatePassword` | Update password |
| DELETE | `/{userId}`       | Delete user     |

### Wallets (`/wallets/api`)

| Method | Endpoint             | Description      |
| ------ | -------------------- | ---------------- |
| POST   | `/create`            | Create wallet    |
| GET    | `/{walletId}`        | Get wallet by ID |
| GET    | `/all`               | Get all wallets  |
| PUT    | `/update/{walletId}` | Update wallet    |
| DELETE | `/delete/{walletId}` | Delete wallet    |
| PUT    | `/transfer`          | Transfer funds   |

### Transactions (`/transactions/api`)

| Method | Endpoint                     | Description             |
| ------ | ---------------------------- | ----------------------- |
| GET    | `/wallet/{walletId}`         | Get wallet transactions |
| GET    | `/all`                       | Get all transactions    |
| GET    | `/status/{status}`           | Get by status           |
| GET    | `/report/{walletId}/{month}` | Monthly report          |

### Loan Applications (`/loan-applications/api`)

| Method | Endpoint           | Description          |
| ------ | ------------------ | -------------------- |
| POST   | `/`                | Create application   |
| GET    | `/{id}`            | Get by ID            |
| GET    | `/`                | Get all applications |
| GET    | `/user/{userId}`   | Get by user          |
| GET    | `/status/{status}` | Get by status        |
| PATCH  | `/status`          | Update status        |
| DELETE | `/{id}`            | Delete application   |

### Support Tickets (`/support-tickets/api`)

| Method | Endpoint               | Description     |
| ------ | ---------------------- | --------------- |
| POST   | `/`                    | Create ticket   |
| GET    | `/{ticketId}`          | Get by ID       |
| GET    | `/`                    | Get all tickets |
| GET    | `/user/{userId}`       | Get by user     |
| GET    | `/status/{status}`     | Get by status   |
| GET    | `/category/{category}` | Get by category |
| PUT    | `/{ticketId}`          | Update ticket   |
| PATCH  | `/{ticketId}/status`   | Update status   |
| DELETE | `/{ticketId}`          | Delete ticket   |

### Support Ticket Responses (`/support-ticket-responses/api`)

| Method | Endpoint             | Description     |
| ------ | -------------------- | --------------- |
| POST   | `/`                  | Create response |
| GET    | `/{responseId}`      | Get by ID       |
| GET    | `/ticket/{ticketId}` | Get by ticket   |
| GET    | `/sender/{senderId}` | Get by sender   |
| DELETE | `/{responseId}`      | Delete response |

## Testing

### Run All Tests

```bash
cd BackEnd
./mvnw test
```

### Run Specific Test Class

```bash
./mvnw test -Dtest=UserServiceImpTest
```

### Test Coverage

The project includes tests for:

- Service layer (business logic)
- Controller layer (API endpoints)
- Repository layer (data access)

Test database: H2 (in-memory) for isolation

## Security

### Features

- **Password Encryption**: BCrypt hashing
- **CSRF Protection**: Disabled for API (stateless)
- **Role-Based Access**: User roles for authorization
- **Optimistic Locking**: Prevents concurrent update conflicts on wallets
- **Audit Logging**: Tracks user actions

### Public Endpoints

- `/auth/api/**` - Authentication
- `/swagger-ui/**` - API Documentation
- `/v3/api-docs/**` - OpenAPI specs

### Protected Endpoints

All other endpoints require authentication.

## Environment Variables

For production, configure these environment variables:

| Variable      | Description                         |
| ------------- | ----------------------------------- |
| `DB_URL`      | Database connection URL             |
| `DB_USERNAME` | Database username                   |
| `DB_PASSWORD` | Database password                   |
| `JWT_SECRET`  | JWT signing secret  |

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is for educational purposes.

---

**Note**: This is a development project. For production deployment, ensure proper security configurations, environment variable management, and database credentials protection.

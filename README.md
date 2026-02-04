# AzBank - FinTech Banking System

A full-stack banking application built with Spring Boot and React, providing digital wallet management, fund transfers, loan applications, and customer support features.

## 🌟 Features

- **User Authentication**: JWT-based registration and login with role-based access
- **OAuth2 Integration**: Secure Login with GitHub
- **Admin Dashboard**: Comprehensive admin panel for user, wallet, and loan management
- **Digital Wallets**: Multi-currency wallet support (USD, EUR, GBP, JPY, CAD, AUD, CHF, EGP)
- **Fund Transfers**: Secure money transfers between wallets with optimistic locking
- **Loan Applications**: Submit and track loan applications with calculator
- **Transaction History**: View, filter, and search transactions with monthly reports
- **Support Tickets**: Customer support system with ticket tracking
- **Modern UI**: Dark theme with amber/gold accents, responsive design

## 🛠 Tech Stack

### Backend

| Technology        | Version | Purpose                                                     |
| ----------------- | ------- | ----------------------------------------------------------- |
| Java              | 21      | Programming Language                                        |
| Spring Boot       | 3.4.1   | Application Framework                                       |
| Spring Security   | -       | Authentication & Authorization                              |
| Spring Data JPA   | -       | Data Persistence                                            |
| Spring AOP        | -       | Aspect-Oriented Programming (Logging, Validation, Security) |
| MySQL             | 8.x     | Database                                                    |
| Flyway            | -       | Database Migrations                                         |
| Lombok            | -       | Code Generation                                             |
| SpringDoc OpenAPI | 2.7.0   | API Documentation                                           |

### Frontend

| Technology   | Version | Purpose             |
| ------------ | ------- | ------------------- |
| React        | 19.2.0  | UI Framework        |
| Vite         | 7.2.4   | Build Tool          |
| React Router | 7.13.0  | Client-side Routing |
| Tailwind CSS | 4.1.18  | Styling             |

## 📁 Project Structure

```
finTech/
├── BackEnd/
│   ├── src/main/java/BankSystem/demo/
│   │   ├── BusinessLogic/
│   │   │   ├── Services/           # Service interfaces
│   │   │   └── ServicesImp/        # Service implementations
│   │   ├── Config/                 # Security, JWT, Exception handling
│   │   ├── Controllers/            # REST API endpoints
│   │   └── DataAccessLayer/
│   │       ├── DTOs/               # Data Transfer Objects
│   │       ├── Entites/            # JPA Entities
│   │       └── Repositories/       # Spring Data Repositories
│   └── src/main/resources/
│       ├── application.properties  # App configuration
│       └── db/migration/           # Flyway migrations
│
└── FrontEnd/cashMe/
    └── src/
        ├── Components/             # Reusable UI components
        │   ├── Nav.jsx             # Navigation bar
        │   ├── Footer.jsx          # Footer component
        │   ├── ProtectedRoute.jsx  # Auth route guard
        │   └── ...
        ├── Pages/                  # Page components
        │   ├── Login.jsx           # Login page
        │   ├── Register.jsx        # Registration page
        │   ├── Dashboard.jsx       # Main dashboard
        │   ├── Wallets.jsx         # Wallet management
        │   ├── Transaction.jsx     # Transaction history
        │   ├── Loan.jsx            # Loan applications
        │   └── ComingSoon.jsx      # Placeholder page
        └── services/               # API & Auth services
            ├── AuthProvider.jsx    # Auth context provider
            ├── authService.js      # API calls (login, register)
            └── config.jsx          # API configuration
```

## 🚀 Getting Started

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

2. **Create MySQL database**

   ```sql
   CREATE DATABASE azbanking;
   ```

3. **Configure database credentials**

   Edit `BackEnd/src/main/resources/application.properties`:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/azbanking
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   jwt.secret=your-jwt-secret-key
   ```

4. **Run the application**

   ```bash
   cd BackEnd
   ./mvnw spring-boot:run
   ```

   Backend starts at `http://localhost:8080`

5. **Access Swagger UI**

   Open `http://localhost:8080/swagger-ui.html`

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
   Frontend starts at `http://localhost:5173`

## 📡 API Endpoints

### Authentication (`/api/v1/auth`)

| Method | Endpoint    | Description       |
| ------ | ----------- | ----------------- |
| POST   | `/register` | Register new user |
| POST   | `/login`    | User login        |

### Users (`/user/api`)

| Method | Endpoint          | Description              |
| ------ | ----------------- | ------------------------ |
| GET    | `/me`             | Get current user profile |
| GET    | `/{userId}`       | Get user by ID           |
| GET    | `/all`            | Get all users            |
| POST   | `/updatePassword` | Update password          |
| DELETE | `/{userId}`       | Delete user              |

### Wallets (`/wallets/api`)

| Method | Endpoint                 | Description       |
| ------ | ------------------------ | ----------------- |
| POST   | `/create`                | Create wallet     |
| GET    | `/{walletId}`            | Get wallet by ID  |
| GET    | `/all`                   | Get all wallets   |
| PUT    | `/deactivate/{walletId}` | Deactivate wallet |
| DELETE | `/delete/{walletId}`     | Delete wallet     |
| PUT    | `/transfer`              | Transfer funds    |

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

| Method | Endpoint             | Description   |
| ------ | -------------------- | ------------- |
| POST   | `/`                  | Create ticket |
| GET    | `/{ticketId}`        | Get by ID     |
| GET    | `/user/{userId}`     | Get by user   |
| GET    | `/status/{status}`   | Get by status |
| PATCH  | `/{ticketId}/status` | Update status |
| DELETE | `/{ticketId}`        | Delete ticket |

## 🗄 Database Schema

### Core Entities

| Entity              | Description                                 |
| ------------------- | ------------------------------------------- |
| **User**            | User accounts with authentication details   |
| **Role**            | User roles (ROLE_USER, ROLE_ADMIN)          |
| **Wallet**          | Digital wallets with multi-currency support |
| **Transaction**     | Fund transfer records                       |
| **LoanApplication** | Loan request details and status             |
| **SupportTicket**   | Customer support tickets                    |
| **AuditLog**        | System activity audit trail                 |

### Migrations (Flyway)

| Migration | Description                                                      |
| --------- | ---------------------------------------------------------------- |
| V1        | Initial schema (users, roles, wallets, transactions, audit_logs) |
| V2        | Add version column for optimistic locking                        |
| V3        | Add support ticket system                                        |
| V4        | Add ticket response table                                        |
| V5        | Add loan applications table                                      |

## 🔒 Security

- **JWT Authentication**: Token-based stateless authentication
- **Password Encryption**: BCrypt hashing
- **CORS Configuration**: Configured for frontend origin
- **Role-Based Access**: User roles for authorization
- **Optimistic Locking**: Prevents concurrent wallet update conflicts
- **Protected Routes**: Frontend route guards for authenticated pages

### Public Endpoints

- `/api/v1/auth/**` - Authentication
- `/swagger-ui/**` - API Documentation

### Protected Endpoints

All other endpoints require JWT token in Authorization header.

## 🎨 Frontend Features

- **Dark Theme**: Modern dark UI with amber/gold accents
- **Responsive Design**: Works on desktop and mobile
- **Protected Routes**: Automatic redirect to login for unauthenticated users
- **Loading States**: Styled loading spinners
- **Error Handling**: User-friendly error messages
- **Authentication Context**: Global auth state management

## 📝 License

This project is for educational purposes.

---

**Note**: This is a development/educational project. For production deployment, ensure proper security configurations and environment variable management.

# AzBank FinTech - Project Roadmap

What still needs to be implemented in the project.

---

## 🔴 Not Started

### Backend :

### 1-Use web sockets for real time notifications and realtime chat between users

### 2-Use redis for caching and message queue

### 3- Add owner role capabilities (create permissions for others)

### Frontend Pages

| Page                      | Current Status      | What's Needed                                           |
| ------------------------- | ------------------- | ------------------------------------------------------- |
| **Analytics**             | Placeholder only    | Charts, graphs, financial insights, spending trends     |
| **Support Tickets**       | Not created         | Create/view tickets page for customer support           |
| **User Profile/Settings** | Not created         | Edit profile, change password, notification preferences |
| **Notifications**         | UI only (bell icon) | Real notification system with backend integration       |

### Frontend Features

| Feature                   | Status | Details                                  |
| ------------------------- | ------ | ---------------------------------------- |
| Forgot password flow      | ❌     | Password reset via email                 |
| Email verification        | ❌     | Verify email on registration             |
| Transaction details modal | ❌     | Click on transaction to see full details |
| PDF transaction reports   | ❌     | Download monthly/yearly statements       |
| Dark/Light theme toggle   | ❌     | Currently only dark theme                |
| Mobile responsive menu    | ❌     | Hamburger menu for mobile navigation     |

---

## 🟡 Partially Implemented

### Frontend Services

| Service                     | Implemented         | Missing                           |
| --------------------------- | ------------------- | --------------------------------- |
| **SupportTicketService.js** | ❌ Not created      | Create, get, update tickets       |
| **UserService.js**          | ✅ `getUserDetails` | `updatePassword`, `updateProfile` |
| **LoanService.js**          | ✅ Complete         | -                                 |
| **TransactionService.js**   | ✅ Complete         | -                                 |
| **WalletService.js**        | ✅ Complete         | -                                 |

### Admin Features

| Feature            | Backend                            | Frontend           |
| ------------------ | ---------------------------------- | ------------------ |
| View all tickets   | ✅ `/support-tickets/api`          | ❌ Not implemented |
| Respond to tickets | ✅ `/support-ticket-responses/api` | ❌ Not implemented |
| Advanced analytics | ❌ Need new endpoints              | ❌ Not implemented |

---

## 🟢 Completed

### Authentication

- [Completed] Login / Register
- [Completed] JWT token handling
- [Completed] Protected routes
- [Completed] Role-based access (Admin badge in nav)
- [Completed] OAuth2 (GitHub Integration)

### Wallets

- [Completed] View all wallets
- [Completed] Create wallet (Fixed 500 error)
- [Completed] Wallet details page
- [Completed] Deactivate wallet
- [Completed] View wallet transactions

### Transactions

- [Completed] View all transactions
- [Completed] Filter by type (sent/received)
- [x] Search transactions
- [Completed] Create new transfer

### Loans

- [Completed] View my loan applications
- [Completed] Loan calculator
- [Completed] Apply for new loan
- [Completed] Status badges

### Dashboard

- [Completed] Wallet count & total balance
- [Completed] Recent transactions
- [Completed] Income/expenses summary

### Admin Dashboard

- [Completed] Full Admin Dashboard UI
- [Completed] User Management (View, Search, Delete)
- [Completed] Role Management (Promote to Admin, Demote)
- [Completed] Wallet Management (View, Deactivate)
- [Completed] Loan Management (View, Approve, Reject)
- [Completed] System Stats (Users, Wallets, Balance, Loans)

---

## 📋 Suggested Next : add any missing features if you want to help me ::)

### Priority 1 - Support System

1. [ ] Create Support Tickets page
2. [ ] SupportTicketService.js with CRUD operations
3. [ ] Admin ticket response interface

### Priority 2 - User Settings

4. [ ] Add user profile page with `updatePassword` functionality
5. [ ] Fix wallet selection dropdown in CreateTransaction (use user's wallets)

### Priority 3 - Polish

6. [ ] Analytics page with charts (consider Chart.js or Recharts)
7. [ ] Notification system
8. [ ] Mobile responsive navigation
9. [ ] PDF export for transaction reports

---

## 🔧 Backend Enhancements Needed

| Feature                | Endpoint Needed                  | Purpose                                 |
| ---------------------- | -------------------------------- | --------------------------------------- |
| Dashboard stats        | `/api/stats`                     | Total users, transactions, loan amounts |
| User profile update    | `PUT /user/api/profile`          | Update name, email (without password)   |
| Transaction pagination | Query params                     | For large transaction lists             |
| Wallet reactivation    | `PUT /wallets/api/activate/{id}` | Reactivate deactivated wallets          |

---

_Last Updated: February 4, 2026_

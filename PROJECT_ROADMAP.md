# AzBank FinTech - Project Roadmap

What still needs to be implemented in the project.

---

## 🔴 Not Started

### Backend :
1-Use web sockets for real time notifications and realtime chat between users
2-Use redis for caching and message queue
3-...... 


### Frontend Pages

| Page                      | Current Status      | What's Needed                                                    |
| ------------------------- | ------------------- | ---------------------------------------------------------------- |
| **Admin Dashboard**       | Placeholder only    | Full admin panel with user management, loan approvals, analytics |
| **Analytics**             | Placeholder only    | Charts, graphs, financial insights, spending trends              |
| **Support Tickets**       | Not created         | Create/view tickets page for customer support                    |
| **User Profile/Settings** | Not created         | Edit profile, change password, notification preferences          |
| **Notifications**         | UI only (bell icon) | Real notification system with backend integration                |

### Frontend Features

| Feature                       | Status | Details                                                    |
| ----------------------------- | ------ | ---------------------------------------------------------- |
| Wallet selection in transfers | ❌     | Show dropdown of user's wallets instead of manual ID input |
| Forgot password flow          | ❌     | Password reset via email                                   |
| Email verification            | ❌     | Verify email on registration                               |
| Transaction details modal     | ❌     | Click on transaction to see full details                   |
| PDF transaction reports       | ❌     | Download monthly/yearly statements                         |
| Dark/Light theme toggle       | ❌     | Currently only dark theme                                  |
| Mobile responsive menu        | ❌     | Hamburger menu for mobile navigation                       |

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

| Feature              | Backend                            | Frontend           |
| -------------------- | ---------------------------------- | ------------------ |
| View all users       | ✅ `/user/api/all`                 | ❌ Not implemented |
| Delete users         | ✅ `/user/api/{userId}`            | ❌ Not implemented |
| Approve/Reject loans | ✅ `/loan-applications/api/status` | ❌ Not implemented |
| View all tickets     | ✅ `/support-tickets/api`          | ❌ Not implemented |
| Respond to tickets   | ✅ `/support-ticket-responses/api` | ❌ Not implemented |
| System analytics     | ❌ Need new endpoints              | ❌ Not implemented |

---

## 🟢 Completed

### Authentication

- [Completed] Login / Register
- [Completed] JWT token handling
- [Completed] Protected routes
- [Completed] Role-based access (Admin badge in nav)

### Wallets

- [Completed] View all wallets
- [Completed] Create wallet
- [Completed] Wallet details page
- [x] Deactivate wallet
- [Completed] View wallet transactions

### Transactions

- [Completed] View all transactions
- [Completed] Filter by type (sent/received)
- [x] Search transactions
- [x] Create new transfer

### Loans

- [Completed] View my loan applications
- [Completed] Loan calculator
- [Completed] Apply for new loan
- [Completed] Status badges

### Dashboard

- [Completed] Wallet count & total balance
- [Completed] Recent transactions
- [Completed] Income/expenses summary

---

## 📋 Suggested Next  : add any missing features if you want to help me ::)

### Priority 1 - Quick Wins

1. [ ] Fix wallet selection dropdown in CreateTransaction (use user's wallets)
2. [ ] Add user profile page with `updatePassword` functionality
3. [ ] Add transaction details view on click
4. [ ] Fix the error in the transfer money page
5. [ ] Fix the error in the deactive wallet page

### Priority 2 - Admin Features

4. [ ] Build Admin Dashboard with:
   - User management table
   - Loan approval/rejection
   - System stats (total users, total transactions, etc.)
   - All users page
   - User details page
   - delete user buttons
   - deactive wallet buttons
    

### Priority 3 - Support System

5. [ ] Create Support Tickets page
6. [ ] SupportTicketService.js with CRUD operations
7. [ ] Admin ticket response interface

### Priority 4 - Polish

8. [ ] Analytics page with charts (consider Chart.js or Recharts)
9. [ ] Notification system
10. [ ] Mobile responsive navigation
11. [ ] PDF export for transaction reports

---

## 🔧 Backend Enhancements Needed

| Feature                | Endpoint Needed                  | Purpose                                 |
| ---------------------- | -------------------------------- | --------------------------------------- |
| Dashboard stats        | `/api/stats`                     | Total users, transactions, loan amounts |
| User profile update    | `PUT /user/api/profile`          | Update name, email (without password)   |
| Transaction pagination | Query params                     | For large transaction lists             |
| Wallet reactivation    | `PUT /wallets/api/activate/{id}` | Reactivate deactivated wallets          |

---

_Last Updated: February 2, 2026_

# AzBank FinTech - Project Roadmap

What still needs to be implemented in the project.

---

## 🔴 Not Started

### Backend :

### 1-Use web sockets for real time notifications and realtime chat between users (in the future probably the next month)

### 2- handle token expiration time and refresh token
### 3- handle idempotency for transactions

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

### Admin Features

| Feature            | Backend                            | Frontend           |
| ------------------ | ---------------------------------- | ------------------ |
| View all tickets   | ✅ `/support-tickets/api`          | ❌ Not implemented |
| Respond to tickets | ✅ `/support-ticket-responses/api` | ❌ Not implemented |
| Advanced analytics | ❌ Need new endpoints              | ❌ Not implemented |

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
10. [ ] Refactor: Use Context API in frontend for data that fails to change frequently

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

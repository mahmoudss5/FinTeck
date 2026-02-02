# API Testing Tracker

This document tracks all available APIs in the FinTech Banking System and their testing status in Swagger UI.

**Legend:**

- [ ] Not tested
- [done] Tested and working
- [!] Tested with issues

---

## 1. Authentication Controller (`/api/v1/auth`)

| #   | Method | Endpoint                | Description       | Tested | Notes    |
| --- | ------ | ----------------------- | ----------------- | ------ | -------- |
| 1   | POST   | `/api/v1/auth/register` | Register new user | [Done] | no notes |
| 2   | POST   | `/api/v1/auth/login`    | Login to account  | [Done] | working  |

---

## 2. User Controller (`/user/api`)

| #   | Method | Endpoint                   | Description            | Tested |
| --- | ------ | -------------------------- | ---------------------- | ------ |
| 3   | POST   | `/user/api/updatePassword` | Update user's password | [done] |
| 4   | GET    | `/user/api/{userId}`       | Get user by ID         | [done] |
| 5   | GET    | `/user/api/all`            | Get all users          | [done] |
| 6   | DELETE | `/user/api/{userId}`       | Delete user by ID      | [done] |

---

## 3. Wallet Controller (`/wallets/api`)

| #   | Method | Endpoint                           | Description                    | Tested | Notes |
| --- | ------ | ---------------------------------- | ------------------------------ | ------ | ----- |
| 7   | POST   | `/wallets/api/create`              | Create a new wallet            | [done] |       |
| 8   | GET    | `/wallets/api/{walletId}`          | Get wallet by ID               | [done] |       |
| 9   | GET    | `/wallets/api/all`                 | Get all wallets                | [done] |       |
| 10  | PUT    | `/wallets/api/deactive/{walletId}` | deactive wallet by ID          | [done] |       |
| 11  | DELETE | `/wallets/api/delete/{walletId}`   | Delete wallet by ID            | [done] |       |
| 12  | PUT    | `/wallets/api/transfer`            | Transfer funds between wallets | [done] |       |

---

## 4. Transaction Controller (`/transactions/api`)

| #   | Method | Endpoint                                      | Description                | Tested | Notes |
| --- | ------ | --------------------------------------------- | -------------------------- | ------ | ----- |
| 13  | GET    | `/transactions/api/wallet/{walletId}`         | Get wallet transactions    | [done] |       |
| 14  | GET    | `/transactions/api/all`                       | Get all transactions       | [done] |       |
| 15  | GET    | `/transactions/api/status/{status}`           | Get transactions by status | [done] |       |
| 16  | GET    | `/transactions/api/report/{walletId}/{month}` | Generate monthly report    | [done] |       |

---

## 5. Loan Application Controller (`/loan-applications/api`)

| #   | Method | Endpoint                                          | Description                       | Tested | Notes |
| --- | ------ | ------------------------------------------------- | --------------------------------- | ------ | ----- |
| 17  | POST   | `/loan-applications/api`                          | Create loan application           | [done] |       |
| 18  | GET    | `/loan-applications/api/{id}`                     | Get loan application by ID        | [done] |       |
| 19  | GET    | `/loan-applications/api`                          | Get all loan applications         | [done] |       |
| 20  | GET    | `/loan-applications/api/user/{userId}`            | Get loan applications by user ID  | [done] |       |
| 21  | GET    | `/loan-applications/api/status/{status}`          | Get loan applications by status   | [done] |       |
| 22  | PATCH  | `/loan-applications/api/status`                   | Update loan application status    | [done] |       |
| 23  | DELETE | `/loan-applications/api/{id}`                     | Delete loan application           | [done] |       |
| 24  | GET    | `/loan-applications/api/exists/{userId}/{status}` | Check if loan application exists  | [done] |       |
| 25  | GET    | `/loan-applications/api/count/{status}`           | Count loan applications by status | [done] |       |

## note : for the deteLoan api make it return the loanResponeDto not void and delte the id field from response

## 6. Support Ticket Controller (`/support-tickets/api`)

| #   | Method | Endpoint                                   | Description             | Tested | Notes |
| --- | ------ | ------------------------------------------ | ----------------------- | ------ | ----- |
| 26  | POST   | `/support-tickets/api`                     | Create support ticket   | [done] |       |
| 27  | GET    | `/support-tickets/api/{ticketId}`          | Get ticket by ID        | [done] |       |
| 28  | GET    | `/support-tickets/api`                     | Get all tickets         | [done] |       |
| 29  | GET    | `/support-tickets/api/user/{userId}`       | Get tickets by user ID  | [done] |       |
| 30  | GET    | `/support-tickets/api/status/{status}`     | Get tickets by status   | [done] |       |
| 31  | GET    | `/support-tickets/api/category/{category}` | Get tickets by category | [done] |       |
| 33  | PATCH  | `/support-tickets/api/{ticketId}/status`   | Update ticket status    | [done] |       |
| 34  | DELETE | `/support-tickets/api/{ticketId}`          | Delete ticket           | [done] |       |

---

## 7. Support Ticket Response Controller (`/support-ticket-responses/api`)

| #   | Method | Endpoint                                          | Description                | Tested | Notes |
| --- | ------ | ------------------------------------------------- | -------------------------- | ------ | ----- |
| 35  | POST   | `/support-ticket-responses/api`                   | Create response            | [done] |       |
| 36  | GET    | `/support-ticket-responses/api/{responseId}`      | Get response by ID         | [done] |       |
| 37  | GET    | `/support-ticket-responses/api/ticket/{ticketId}` | Get responses by ticket ID | [done] |       |
| 38  | GET    | `/support-ticket-responses/api/sender/{senderId}` | Get responses by sender ID | [done] |       |
| 39  | DELETE | `/support-ticket-responses/api/{responseId}`      | Delete response            | [done] |       |

---

## Testing Summary

| Controller              | Total APIs | Tested | Passing | Issues |
| ----------------------- | ---------- | ------ | ------- | ------ | ------- |
| Authentication          | 2          | 2      | 2       | 0      | 100%    |
| User                    | 4          | 4      | 4       | 0      | 100%    |
| Wallet                  | 5          | 5      | 5       | 0      | 100%    |
| Transaction             | 4          | 4      | 4       | 0      | 100%    |
| Loan Application        | 9          | 9      | 9       | 0      | 100%    |
| Support Ticket          | 9          | 9      | 9       | 0      | 100%    |
| Support Ticket Response | 5          | 5      | 5       | 0      | 100%    |
| **TOTAL**               | **39**     | **39** | **39**  | **0**  | ✅ 100% |

---

## Testing Notes

### How to Test

1. Start the backend server
2. Open Swagger UI at: `http://localhost:8080/swagger-ui.html`
3. For authenticated endpoints, first call `/api/v1/auth/login` and use the JWT token
4. Mark each endpoint as tested by changing `[ ]` to `[x]` or `[!]` for issues

### Test Credentials

> ⚠️ **Note**: Create your own test user via the registration endpoint.
> Do not commit real credentials to version control.

### Issues Found

<!-- Document any issues found during testing here -->

1.

---

_Last Updated: February 2, 2026_

✅ **All 39 API endpoints tested successfully with no errors.**

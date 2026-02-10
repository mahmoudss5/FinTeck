import './App.css'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import Auth from "./Pages/Auth/Auth.jsx";
import RootLayout from "./Pages/Common/RootLayout.jsx";
import Register from "./Pages/Auth/Register.jsx";
import Dashboard from "./Pages/Common/Dashboard.jsx";
import Loan from "./Pages/Loans/Loan.jsx";
import Transaction from "./Pages/Common/Transaction.jsx";
import Wallets from "./Pages/Wallets/Wallets.jsx";
import WalletDetails from "./Components/Wallets/WalletDetails.jsx";
import CreateWallet from "./Pages/Wallets/CreateWallet.jsx";
import ProtectedRoute from "./Components/Common/ProtectedRoute.jsx";
import Login from "./Pages/Auth/Login.jsx";
import ComingSoon from "./Pages/Common/ComingSoon.jsx";
import AdminDashboard from "./Pages/Admin/AdminDashboard.jsx";
import LoadingSpinner from "./Components/Common/LoadingSpinner.jsx";
import CreateTransaction from "./Components/Transactions/CreateTransaction.jsx";
import { getWalletById } from "./services/WalletService.js";
import { getAllWalletsforCurrentUser } from "./services/WalletService.js";
import LoanDetailsForAdmin from "./Components/Admin/LoanDetailsForAdmin.jsx";
import { LoadLoanDetails } from "./services/LoanService.js";
import OAuth2Redirect from "./Pages/Auth/OAuth2Redirect.jsx";
import { getAllUserTransactions } from "./services/TransactionService.js";
import Support from "./Pages/Common/Support.jsx";
import { TicketProvider } from "./context/TicketContext.jsx";

function App() {
  const router = createBrowserRouter([
    {
      path: '/',
      element: <Auth />,
      children: [
        {
          index: true,
          element: <Login />
        },
        {
          path: 'login',
          element: <Login />
        },
        {
          path: 'register',
          element: <Register />
        }
      ]
    },
    {
      path: '/home',
      element: <TicketProvider><RootLayout /></TicketProvider>,
      children: [
        {
          index: true,
          element: <ProtectedRoute><Dashboard /></ProtectedRoute>
        },
        {
          path: 'loans',
          element: <ProtectedRoute><Loan /></ProtectedRoute>
        },
        {
          path: 'transactions',
          element: <ProtectedRoute><Transaction /></ProtectedRoute>,
          loader: getAllUserTransactions,
          children: [
            {
              path:'add',
              element: <ProtectedRoute>
                <CreateTransaction/>
                </ProtectedRoute>
            }
          ]
        },
        {
          path: 'wallets',
          element: <ProtectedRoute><Wallets /></ProtectedRoute>,
          loader: getAllWalletsforCurrentUser
        },
        {
          path: 'wallets/:id',
          element: <ProtectedRoute><WalletDetails /></ProtectedRoute>,
          loader:getWalletById
        },
        {
          path: 'wallets/create',
          element: <ProtectedRoute><CreateWallet /></ProtectedRoute>
        },
        {
          path: 'coming-soon',
          element: <ProtectedRoute><ComingSoon /></ProtectedRoute>
        },
        {
          path: 'admin-dashboard',
          element: <ProtectedRoute>
            <AdminDashboard />
            </ProtectedRoute>
        },
        {
          path: 'admin-dashboard/loan-details/:id',
          element: <ProtectedRoute>
            <LoanDetailsForAdmin />
            </ProtectedRoute>,
            loader: LoadLoanDetails
        },
        {
          path: 'support',
          element: <ProtectedRoute>
            <Support />
            </ProtectedRoute>
        }
      ]
    },
    {
      path: '/oauth2/redirect',
      element: <OAuth2Redirect />
    }
  ])

  return (
    <>
      <RouterProvider router={router} 
      fallbackElement={<LoadingSpinner />} />
    </>
  )
}

export default App

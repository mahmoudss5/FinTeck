import './App.css'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import Auth from "./Pages/Auth.jsx";
import RootLayout from "./Pages/RootLayout.jsx";
import Register from "./Pages/Register.jsx";
import Dashboard from "./Pages/Dashboard.jsx";
import Loan from "./Pages/Loan.jsx";
import Transaction from "./Pages/Transaction.jsx";
import Wallets from "./Pages/Wallets.jsx";
import WalletDetails from "./Components/WalletDetails.jsx";
import CreateWallet from "./Pages/CreateWallet.jsx";
import ProtectedRoute from "./Components/ProtectedRoute.jsx";
import Login from "./Pages/Login.jsx";
import ComingSoon from "./Pages/ComingSoon.jsx";
import AdminDashboard from "./Pages/AdminDashboard.jsx";
import LoadingSpinner from "./Components/LoadingSpinner.jsx";
import CreateTransaction from "./Components/CreateTransaction.jsx";
import { getWalletById } from "./services/WalletService.js";
import { getAllWalletsforCurrentUser } from "./services/WalletService.js";

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
      element: <RootLayout />,
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
        }
      ]
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

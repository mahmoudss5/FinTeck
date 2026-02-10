import { Link } from "react-router-dom";
import { useAuth } from "../../context/AuthProvider";
import { useState, useEffect } from "react";
import { getUserDetails } from "../../services/UserService";
import { getAllTransactions } from "../../services/TransactionService";
import { getAllWalletsforCurrentUser } from "../../services/WalletService";

export default function Dashboard() {
    const { user } = useAuth();
    const [userDetails, setUserDetails] = useState(null);
    const [transactions, setTransactions] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);
                const [userData, txnData] = await Promise.all([
                    getUserDetails(),
                    getAllWalletsforCurrentUser()
                ]);
                setUserDetails(userData);
                setTransactions(txnData.slice(0, 4)); 
            } catch (err) {
                setError(err.message);
                console.error("Error fetching dashboard data:", err);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    const walletCount = userDetails?.wallets?.length || 0;
    const totalBalance = userDetails?.wallets?.reduce((sum, wallet) => sum + parseFloat(wallet.balance || 0), 0) || 0;

    const income = transactions
        .filter(t => t.receiverUserName === user?.userName)
        .reduce((sum, t) => sum + parseFloat(t.amount || 0), 0);
    const expenses = transactions
        .filter(t => t.senderUserName === user?.userName)
        .reduce((sum, t) => sum + parseFloat(t.amount || 0), 0);

    const formatDate = (dateString) => {
        if (!dateString) return 'N/A';
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });
    };

    if (loading) {
        return (
            <div className="flex items-center justify-center min-h-[400px]">
                <div className="text-center">
                    <div className="w-12 h-12 border-4 border-amber-500 border-t-transparent rounded-full animate-spin mx-auto mb-4"></div>
                    <p className="text-gray-400">Loading dashboard...</p>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="bg-red-500/10 border border-red-500/20 rounded-xl p-6 text-center">
                <p className="text-red-400">Error loading dashboard: {error}</p>
            </div>
        );
    }

    return (
        <div className="space-y-6">
            {/* Welcome Section */}
            <div className="flex items-center justify-between">
                <div>
                    <h1 className="text-2xl font-bold text-white">Welcome back, {user?.userName || 'User'}!</h1>
                    <p className="text-gray-400">Here's what's happening with your finances today.</p>
                </div>
            </div>

            {/* Stats Cards */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                {/* Total Balance */}
                <div className="hover:scale-105 transition-all duration-200 bg-[#1a1a1a] border border-amber-900/20 rounded-xl p-6">
                    <div className="flex items-center justify-between mb-4">
                        <span className="text-gray-400 text-sm">Total Balance</span>
                        <span className="w-10 h-10 bg-amber-500/10 rounded-lg flex items-center justify-center">
                            <svg className="w-5 h-5 text-amber-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                            </svg>
                        </span>
                    </div>
                    <p className="text-2xl font-bold text-white">${totalBalance.toLocaleString('en-US', { minimumFractionDigits: 2 })}</p>
                    <p className="text-sm text-gray-500 mt-2">Across {walletCount} wallet{walletCount !== 1 ? 's' : ''}</p>
                </div>

                {/* Wallets */}
                <Link to="wallets" className="hover:scale-105 transition-all duration-200 bg-[#1a1a1a] border border-amber-900/20 rounded-xl p-6 cursor-pointer">
                    <div className="flex items-center justify-between mb-4">
                        <span className="text-gray-400 text-sm">My Wallets</span>
                        <span className="w-10 h-10 bg-blue-500/10 rounded-lg flex items-center justify-center">
                            <svg className="w-5 h-5 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z" />
                            </svg>
                        </span>
                    </div>
                    <p className="text-2xl font-bold text-white">{walletCount}</p>
                    <p className="text-sm text-blue-400 mt-2 flex items-center gap-1">
                        View all wallets →
                    </p>
                </Link>

                {/* Income */}
                <div className="hover:scale-105 transition-all duration-200 bg-[#1a1a1a] border border-amber-900/20 rounded-xl p-6">
                    <div className="flex items-center justify-between mb-4">
                        <span className="text-gray-400 text-sm">Income (Recent)</span>
                        <span className="w-10 h-10 bg-green-500/10 rounded-lg flex items-center justify-center">
                            <svg className="w-5 h-5 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 11l5-5m0 0l5 5m-5-5v12" />
                            </svg>
                        </span>
                    </div>
                    <p className="text-2xl font-bold text-green-500">+${income.toLocaleString('en-US', { minimumFractionDigits: 2 })}</p>
                    <p className="text-sm text-gray-500 mt-2">From recent transactions</p>
                </div>

                {/* Expenses */}
                <div className="hover:scale-105 transition-all duration-200 bg-[#1a1a1a] border border-amber-900/20 rounded-xl p-6">
                    <div className="flex items-center justify-between mb-4">
                        <span className="text-gray-400 text-sm">Expenses (Recent)</span>
                        <span className="w-10 h-10 bg-red-500/10 rounded-lg flex items-center justify-center">
                            <svg className="w-5 h-5 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 13l-5 5m0 0l-5-5m5 5V6" />
                            </svg>
                        </span>
                    </div>
                    <p className="text-2xl font-bold text-red-500">-${expenses.toLocaleString('en-US', { minimumFractionDigits: 2 })}</p>
                    <p className="text-sm text-gray-500 mt-2">From recent transactions</p>
                </div>
            </div>

            {/* Recent Transactions */}
            <div className="bg-[#1a1a1a] border border-amber-900/20 rounded-xl p-6">
                <div className="flex items-center justify-between mb-6">
                    <h2 className="text-lg font-semibold text-white">Recent Transactions</h2>
                    <Link to="transactions" className="text-amber-400 text-sm hover:text-amber-300 transition-colors">View All</Link>
                </div>
                <div className="space-y-4">
                    {transactions.length === 0 ? (
                        <div className="text-center py-8">
                            <svg className="w-12 h-12 mx-auto text-gray-600 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                            </svg>
                            <p className="text-gray-400">No recent transactions</p>
                        </div>
                    ) : (
                        transactions.map((txn, index) => {
                            const isReceived = txn.receiverUserName === user?.userName;
                            return (
                                <div key={index} className="flex items-center justify-between p-4 bg-[#242424] rounded-lg">
                                    <div className="flex items-center gap-4">
                                        <div className={`w-10 h-10 ${isReceived ? 'bg-green-500/10' : 'bg-red-500/10'} rounded-lg flex items-center justify-center`}>
                                            {isReceived ? (
                                                <svg className="w-5 h-5 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 11l5-5m0 0l5 5m-5-5v12" />
                                                </svg>
                                            ) : (
                                                <svg className="w-5 h-5 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 13l-5 5m0 0l-5-5m5 5V6" />
                                                </svg>
                                            )}
                                        </div>
                                        <div>
                                            <p className="text-white font-medium">
                                                {isReceived ? `From: ${txn.senderUserName}` : `To: ${txn.receiverUserName}`}
                                            </p>
                                            <p className="text-gray-500 text-sm">{formatDate(txn.createdAt)}</p>
                                        </div>
                                    </div>
                                    <span className={`font-semibold ${isReceived ? 'text-green-500' : 'text-red-500'}`}>
                                        {isReceived ? '+' : '-'}${parseFloat(txn.amount).toLocaleString('en-US', { minimumFractionDigits: 2 })}
                                    </span>
                                </div>
                            );
                        })
                    )}
                </div>
            </div>
        </div>
    );
}

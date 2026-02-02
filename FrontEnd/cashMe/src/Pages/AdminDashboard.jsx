import { useState, useEffect } from 'react';
import { getAllUsers, deleteUser, getAllWallets, getAllLoans } from '../services/AdminService';

export default function AdminDashboard() {
    const [activeTab, setActiveTab] = useState('overview');
    const [users, setUsers] = useState([]);
    const [wallets, setWallets] = useState([]);
    const [loans, setLoans] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [actionLoading, setActionLoading] = useState(null);

    useEffect(() => {
        fetchAllData();
    }, []);

    const fetchAllData = async () => {
        try {
            setLoading(true);
            setError(null);
            const [usersData, walletsData, loansData] = await Promise.all([
                getAllUsers(),
                getAllWallets(),
                getAllLoans().catch(() => []) 
            ]);
            setUsers(usersData);
            setWallets(walletsData);
            setLoans(loansData);
        } catch (err) {
            setError(err.message);
            console.error("Error fetching admin data:", err);
        } finally {
            setLoading(false);
        }
    };

    const handleDeleteUser = async (userId) => {
        if (!confirm('Are you sure you want to delete this user? This action cannot be undone.')) {
            return;
        }
        try {
            setActionLoading(userId);
            await deleteUser(userId);
            setUsers(users.filter(u => u.id !== userId));
        } catch (err) {
            alert('Failed to delete user: ' + err.message);
        } finally {
            setActionLoading(null);
        }
    };

    const formatDate = (dateString) => {
        if (!dateString) return 'N/A';
        return new Date(dateString).toLocaleDateString('en-US', {
            month: 'short', day: 'numeric', year: 'numeric'
        });
    };
    const handlePromoteUser = async (userId) => {
        if (!confirm('Are you sure you want to promote this user to admin? This action cannot be undone.')) {
            return;
        }
        try {
            setActionLoading(userId);
            await promoteUserToAdmin(userId);
            setUsers(users.map(u => u.id === userId ? { ...u, role: 'ADMIN' } : u));
        } catch (err) {
            alert('Failed to promote user: ' + err.message);
        } finally {
            setActionLoading(null);
        }
    };
     
    const handleDeactiveWallet = async (walletId) => {
        if (!confirm('Are you sure you want to deactive this wallet? This action cannot be undone.')) {
            return;
        }
        try {
            setActionLoading(walletId);
            await updateWalletStatus(walletId, 'INACTIVE');
            setWallets(wallets.map(w => w.id === walletId ? { ...w, status: 'INACTIVE' } : w));
        } catch (err) {
            alert('Failed to deactive wallet: ' + err.message);
        } finally {
            setActionLoading(null);
        }
    };


    // Stats calculations
    const totalBalance = wallets.reduce((sum, w) => sum + parseFloat(w.balance || 0), 0);
    const activeWallets = wallets.filter(w => w.active).length;
    const pendingLoans = loans.filter(l => l.status === 'PENDING').length;

    if (loading) {
        return (
            <div className="flex items-center justify-center min-h-[400px]">
                <div className="text-center">
                    <div className="w-12 h-12 border-4 border-red-500 border-t-transparent rounded-full animate-spin mx-auto mb-4"></div>
                    <p className="text-gray-400">Loading admin data...</p>
                </div>
            </div>
        );
    }

    return (
        <div className="space-y-6">
            {/* Header */}
            <div className="flex items-center justify-between">
                <div>
                    <h1 className="text-2xl font-bold text-white flex items-center gap-3">
                        <span className="w-10 h-10 bg-red-500/20 rounded-lg flex items-center justify-center">
                            <svg className="w-6 h-6 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                            </svg>
                        </span>
                        Admin Dashboard
                    </h1>
                    <p className="text-gray-400">Manage users, wallets, and system settings</p>
                </div>
                <button
                    onClick={fetchAllData}
                    className="px-4 py-2 bg-green-600 border border-green-500/20 text-white hover:bg-green-500/20 rounded-lg transition-colors"
                >
                    ↻ Refresh Data
                </button>
            </div>

            {/* Error Message */}
            {error && (
                <div className="bg-red-500/10 border border-red-500/20 rounded-xl p-4">
                    <p className="text-red-400">{error}</p>
                </div>
            )}

            {/* Stats Cards */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                <div className="bg-[#1a1a1a] border border-red-900/20 rounded-xl p-6">
                    <div className="flex items-center justify-between mb-4">
                        <span className="text-gray-400 text-sm">Total Users</span>
                        <span className="w-10 h-10 bg-blue-500/10 rounded-lg flex items-center justify-center">
                            <svg className="w-5 h-5 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
                            </svg>
                        </span>
                    </div>
                    <p className="text-3xl font-bold text-white">{users.length}</p>
                    <p className="text-sm text-gray-500 mt-2">Registered accounts</p>
                </div>

                <div className="bg-[#1a1a1a] border border-red-900/20 rounded-xl p-6">
                    <div className="flex items-center justify-between mb-4">
                        <span className="text-gray-400 text-sm">Total Wallets</span>
                        <span className="w-10 h-10 bg-amber-500/10 rounded-lg flex items-center justify-center">
                            <svg className="w-5 h-5 text-amber-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z" />
                            </svg>
                        </span>
                    </div>
                    <p className="text-3xl font-bold text-white">{wallets.length}</p>
                    <p className="text-sm text-green-400 mt-2">{activeWallets} active</p>
                </div>

                <div className="bg-[#1a1a1a] border border-red-900/20 rounded-xl p-6">
                    <div className="flex items-center justify-between mb-4">
                        <span className="text-gray-400 text-sm">System Balance</span>
                        <span className="w-10 h-10 bg-green-500/10 rounded-lg flex items-center justify-center">
                            <svg className="w-5 h-5 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                            </svg>
                        </span>
                    </div>
                    <p className="text-3xl font-bold text-green-500">${totalBalance.toLocaleString('en-US', { minimumFractionDigits: 2 })}</p>
                    <p className="text-sm text-gray-500 mt-2">Across all wallets</p>
                </div>

                <div className="bg-[#1a1a1a] border border-red-900/20 rounded-xl p-6">
                    <div className="flex items-center justify-between mb-4">
                        <span className="text-gray-400 text-sm">Loan Applications</span>
                        <span className="w-10 h-10 bg-purple-500/10 rounded-lg flex items-center justify-center">
                            <svg className="w-5 h-5 text-purple-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                            </svg>
                        </span>
                    </div>
                    <p className="text-3xl font-bold text-white">{loans.length}</p>
                    <p className="text-sm text-yellow-400 mt-2">{pendingLoans} pending</p>
                </div>
            </div>

            {/* Tab Navigation */}
            <div className="flex gap-2 bg-[#1a1a1a] p-1 rounded-lg w-fit border border-red-900/20">
                {['overview', 'users', 'wallets', 'loans'].map((tab) => (
                    <button
                        key={tab}
                        onClick={() => setActiveTab(tab)}
                        className={`px-4 py-2 rounded-lg font-medium capitalize transition-all duration-200 ${
                            activeTab === tab
                                ? 'bg-yellow-500 text-white'
                                : 'text-gray-400 hover:text-white'
                        }`}
                    >
                        {tab}
                    </button>
                ))}
            </div>

            {/* Users Table */}
            {activeTab === 'users' && (
                <div className="bg-[#1a1a1a] border border-red-900/20 rounded-xl overflow-hidden">
                    <div className="p-6 border-b border-red-900/20">
                        <h2 className="text-lg font-semibold text-white">All Users ({users.length})</h2>
                    </div>
                    <div className="overflow-x-auto">
                        <table className="w-full">
                            <thead className="bg-[#242424]">
                                <tr className="text-left text-gray-400 text-sm">
                                    <th className="px-6 py-4">ID</th>
                                    <th className="px-6 py-4">Username</th>
                                    <th className="px-6 py-4">Email</th>
                                    <th className="px-6 py-4">Wallets</th>
                                    <th className="px-6 py-4">Roles</th>
                                    <th className="px-6 py-4">Created</th>
                                    <th className="px-6 py-4">Actions</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-red-900/10">
                                {users.map((user) => (
                                    <tr key={user.id} className="hover:bg-[#242424] transition-colors">
                                        <td className="px-6 py-4 text-gray-400">#{user.id}</td>
                                        <td className="px-6 py-4 text-white font-medium">{user.userName}</td>
                                        <td className="px-6 py-4 text-gray-400">{user.email}</td>
                                        <td className="px-6 py-4 text-amber-400">{user.wallets?.length || 0}</td>
                                        <td className="px-6 py-4">
                                            {user.roles?.map((role, i) => (
                                                <span key={i} className={`px-2 py-1 rounded text-xs font-medium mr-1 ${
                                                    role.includes('ADMIN') 
                                                        ? 'bg-red-500/10 text-red-400 border border-red-500/20'
                                                        : 'bg-blue-500/10 text-blue-400 border border-blue-500/20'
                                                }`}>
                                                    {role.replace('ROLE_', '')}
                                                </span>
                                            ))}
                                        </td>
                                        <td className="px-6 py-4 text-gray-400">{formatDate(user.createdAt)}</td>
                                        <td className="px-6 py-4">
                                            <button
                                                onClick={() => handleDeleteUser(user.id)}
                                                disabled={actionLoading === user.id}
                                                className="px-3 py-1 bg-red-500/10 border border-red-500/20 text-red-400 hover:bg-red-500/20 rounded text-sm transition-colors disabled:opacity-50"
                                            >
                                                {actionLoading === user.id ? '...' : 'Delete'}
                                            </button>
                                            <button
                                                onClick={() => handlePromoteUserToAdmin(user.id)}
                                                disabled={actionLoading === user.id}
                                                className="ml-2 px-3 py-1 bg-yellow-500 border border-yellow-500/20 text-white hover:bg-yellow-500/20 rounded text-sm transition-colors disabled:opacity-50"
                                            >
                                                {actionLoading === user.id ? '...' : 'Promote to Admin'}
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            )}

            {/* Wallets Table */}
            {activeTab === 'wallets' && (
                <div className="bg-[#1a1a1a] border border-red-900/20 rounded-xl overflow-hidden">
                    <div className="p-6 border-b border-red-900/20">
                        <h2 className="text-lg font-semibold text-white">All Wallets ({wallets.length})</h2>
                    </div>
                    <div className="overflow-x-auto">
                        <table className="w-full">
                            <thead className="bg-[#242424]">
                                <tr className="text-left text-gray-400 text-sm">
                                    <th className="px-6 py-4">ID</th>
                                    <th className="px-6 py-4">Currency</th>
                                    <th className="px-6 py-4">Balance</th>
                                    <th className="px-6 py-4">Status</th>
                                    <th className="px-6 py-4">Created</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-red-900/10">
                                {wallets.map((wallet) => (
                                    <tr key={wallet.id} className="hover:bg-[#242424] transition-colors">
                                        <td className="px-6 py-4 text-gray-400">#{wallet.id}</td>
                                        <td className="px-6 py-4">
                                            <span className="px-2 py-1 bg-amber-500/10 text-amber-400 rounded font-medium">
                                                {wallet.currency}
                                            </span>
                                        </td>
                                        <td className="px-6 py-4 text-white font-semibold">
                                            ${parseFloat(wallet.balance).toLocaleString('en-US', { minimumFractionDigits: 2 })}
                                        </td>
                                        <td className="px-6 py-4">
                                            <span className={`px-2 py-1 rounded text-xs font-medium ${
                                                wallet.active 
                                                    ? 'bg-green-500/10 text-green-400 border border-green-500/20'
                                                    : 'bg-red-500/10 text-red-400 border border-red-500/20'
                                            }`}>
                                                <button className='cursor-pointer hover:text-white transition-colors'
                                                onClick={() => handleDeactiveWallet(wallet.id)}>
                                                    {wallet.active ? 'Active' : 'Inactive'}
                                                </button>
                                            </span>
                                        </td>
                                        <td className="px-6 py-4 text-gray-400">{formatDate(wallet.createdAt)}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            )}

            {/* Loans Table */}
            {activeTab === 'loans' && (
                <div className="bg-[#1a1a1a] border border-red-900/20 rounded-xl overflow-hidden">
                    <div className="p-6 border-b border-red-900/20">
                        <h2 className="text-lg font-semibold text-white">All Loan Applications ({loans.length})</h2>
                    </div>
                    {loans.length === 0 ? (
                        <div className="p-12 text-center">
                            <p className="text-gray-400">No loan applications found</p>
                        </div>
                    ) : (
                        <div className="overflow-x-auto">
                            <table className="w-full">
                                <thead className="bg-[#242424]">
                                    <tr className="text-left text-gray-400 text-sm">
                                        <th className="px-6 py-4">ID</th>
                                        <th className="px-6 py-4">Applicant</th>
                                        <th className="px-6 py-4">Amount</th>
                                        <th className="px-6 py-4">Purpose</th>
                                        <th className="px-6 py-4">Status</th>
                                        <th className="px-6 py-4">Applied</th>
                                    </tr>
                                </thead>
                                <tbody className="divide-y divide-red-900/10">
                                    {loans.map((loan) => (
                                        <tr key={loan.id} className="hover:bg-[#242424] transition-colors">
                                            <td className="px-6 py-4 text-gray-400">#{loan.id}</td>
                                            <td className="px-6 py-4 text-white">{loan.fullName}</td>
                                            <td className="px-6 py-4 text-amber-400 font-semibold">
                                                ${parseFloat(loan.requestedAmount).toLocaleString('en-US', { minimumFractionDigits: 2 })}
                                            </td>
                                            <td className="px-6 py-4 text-gray-400">{loan.loanPurpose}</td>
                                            <td className="px-6 py-4">
                                                <span className={`px-2 py-1 rounded text-xs font-medium ${
                                                    loan.status === 'APPROVED' ? 'bg-green-500/10 text-green-400 border border-green-500/20' :
                                                    loan.status === 'REJECTED' ? 'bg-red-500/10 text-red-400 border border-red-500/20' :
                                                    'bg-yellow-500/10 text-yellow-400 border border-yellow-500/20'
                                                }`}>
                                                    {loan.status}
                                                </span>
                                            </td>
                                            <td className="px-6 py-4 text-gray-400">{formatDate(loan.appliedAt)}</td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    )}
                </div>
            )}

            {/* Overview Tab */}
            {activeTab === 'overview' && (
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                    {/* Recent Users */}
                    <div className="bg-[#1a1a1a] border border-red-900/20 rounded-xl p-6">
                        <h3 className="text-lg font-semibold text-white mb-4">Recent Users</h3>
                        <div className="space-y-3">
                            {users.slice(0, 5).map((user) => (
                                <div key={user.id} className="flex items-center justify-between p-3 bg-[#242424] rounded-lg">
                                    <div className="flex items-center gap-3">
                                        <div className="w-10 h-10 bg-blue-500/20 rounded-full flex items-center justify-center text-blue-400 font-semibold">
                                            {user.userName?.charAt(0).toUpperCase()}
                                        </div>
                                        <div>
                                            <p className="text-white font-medium">{user.userName}</p>
                                            <p className="text-gray-500 text-sm">{user.email}</p>
                                        </div>
                                    </div>
                                    <span className="text-gray-400 text-sm">{formatDate(user.createdAt)}</span>
                                </div>
                            ))}
                        </div>
                    </div>

                    {/* Recent Wallets */}
                    <div className="bg-[#1a1a1a] border border-red-900/20 rounded-xl p-6">
                        <h3 className="text-lg font-semibold text-white mb-4">Recent Wallets</h3>
                        <div className="space-y-3">
                            {wallets.slice(0, 5).map((wallet) => (
                                <div key={wallet.id} className="flex items-center justify-between p-3 bg-[#242424] rounded-lg">
                                    <div className="flex items-center gap-3">
                                        <div className="w-10 h-10 bg-amber-500/20 rounded-lg flex items-center justify-center text-amber-400 font-semibold text-sm">
                                            {wallet.currency}
                                        </div>
                                        <div>
                                            <p className="text-white font-medium">Wallet #{wallet.id}</p>
                                            <p className="text-gray-500 text-sm">{wallet.active ? 'Active' : 'Inactive'}</p>
                                        </div>
                                    </div>
                                    <span className="text-green-400 font-semibold">
                                        ${parseFloat(wallet.balance).toLocaleString('en-US', { minimumFractionDigits: 2 })}
                                    </span>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

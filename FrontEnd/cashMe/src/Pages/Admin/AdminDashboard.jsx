import { useState, useEffect } from 'react';
import { getAllUsers, deleteUser, getAllWallets, getAllLoans, promoteUserToAdmin, demoteAdmin, updateWalletStatus } from '../../services/AdminService';
import { useAuth } from '../../context/AuthProvider';
import UsersTable from '../../Components/Admin/UsersTable';
import WalletsTable from '../../Components/Admin/WalletsTable';
import LoansTable from '../../Components/Admin/LoansTable';

export default function AdminDashboard() {
    const [activeTab, setActiveTab] = useState('overview');
    const [users, setUsers] = useState([]);
    const [wallets, setWallets] = useState([]);
    const [loans, setLoans] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [actionLoading, setActionLoading] = useState(null);
    const { user } = useAuth();
    const isOwner = user.roles.includes('Owner');

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
            setUsers(usersData.users || []); // Extract users array from response object
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

    const handlePromoteUserToAdmin = async (userId) => {
        if (!confirm('Are you sure you want to promote this user to admin?')) {
            return;
        }
        try {
            setActionLoading(userId);
            await promoteUserToAdmin(userId);
            await fetchAllData();
        } catch (err) {
            alert('Failed to promote user: ' + err.message);
        } finally {
            setActionLoading(null);
        }
    };

    const handleDemoteAdmin = async (userId) => {
        if (!confirm('Are you sure you want to demote this admin to regular user?')) {
            return;
        }
        try {
            setActionLoading(userId);
            await demoteAdmin(userId);
            await fetchAllData();
        } catch (err) {
            alert('Failed to demote admin: ' + err.message);
        } finally {
            setActionLoading(null);
        }
    };
     
    const handleDeactivateWallet = async (walletId) => {
        if (!confirm('Are you sure you want to deactivate this wallet? This action cannot be undone.')) {
            return;
        }
        try {
            setActionLoading(walletId);
            await updateWalletStatus(walletId);
            setWallets(wallets.map(w => w.id === walletId ? { ...w, active: false } : w));
        } catch (err) {
            alert('Failed to deactivate wallet: ' + err.message);
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
                <UsersTable
                    users={users}
                    isOwner={isOwner}
                    actionLoading={actionLoading}
                    onDelete={handleDeleteUser}
                    onPromote={handlePromoteUserToAdmin}
                    onDemote={handleDemoteAdmin}
                    formatDate={formatDate}
                />
            )}

            {/* Wallets Table */}
            {activeTab === 'wallets' && (
                <WalletsTable
                    wallets={wallets}
                    actionLoading={actionLoading}
                    onDeactivate={handleDeactivateWallet}  
                    formatDate={formatDate}
                />
            )}

            {/* Loans Table */}
            {activeTab === 'loans' && (
                <LoansTable
                    loans={loans}
                    formatDate={formatDate}
                />
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

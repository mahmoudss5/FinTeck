import { useLoaderData, useNavigate, useRevalidator } from "react-router-dom";
import { useState } from "react";
import { deactivateWallet } from "../../services/WalletService";

export default function WalletDetails() {
    const { wallet, transactions } = useLoaderData();
    const navigate = useNavigate();
    const revalidator = useRevalidator();
    const [deactivating, setDeactivating] = useState(false);
    const [error, setError] = useState(null);

    const handleBackToWallets = () => {
        navigate('../wallets');
    };

    const handleDeactivateWallet = async () => {
        
        if (!confirm('Are you sure you want to deactivate this wallet? This action cannot be undone.')) {
            return;
        }
        
        const id=Number(wallet.id)
        setDeactivating(true);
        setError(null);
        try {
            await deactivateWallet(id);
            // Revalidate to reload all loader data
            revalidator.revalidate();
            navigate('../wallets');
        } catch (err) {
            setError(err.message);
            setDeactivating(false);
        }
    };

    const formatDate = (dateString) => {
        if (!dateString) return 'N/A';
        return new Date(dateString).toLocaleDateString('en-US', {
            month: 'short',
            day: 'numeric',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    const getStatusBadge = (status) => {
        const styles = {
            COMPLETED: 'bg-green-500/10 text-green-500 border-green-500/20',
            PENDING: 'bg-yellow-500/10 text-yellow-500 border-yellow-500/20',
            FAILED: 'bg-red-500/10 text-red-500 border-red-500/20'
        };
        return styles[status] || styles.PENDING;
    };

    return (
        <div className="space-y-6">
            {/* Header */}
            <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
                <div>
                    <h1 className="text-2xl font-bold text-white">Wallet Details</h1>
                    <p className="text-gray-400">Manage your {wallet.currency} wallet</p>
                </div>
                <div className="flex gap-3">
                    <button
                        onClick={handleBackToWallets}
                        className="px-4 py-2 bg-yellow-700 border border-amber-900/20 text-gray-300 hover:text-white rounded-lg transition-colors duration-200"
                    >
                        ← Back to Wallets
                    </button>
                    {wallet.active && (
                        <button
                            onClick={handleDeactivateWallet}
                            disabled={deactivating}
                            className="px-4 py-2 bg-red-500/10 border border-red-500/20 text-red-400 hover:bg-red-500/20 rounded-lg transition-colors duration-200 disabled:opacity-50"
                        >
                            {deactivating ? 'Deactivating...' : 'Deactivate Wallet'}
                        </button>
                    )}
                </div>
            </div>

            {/* Error Message */}
            {error && (
                <div className="bg-red-500/10 border border-red-500/20 rounded-xl p-4">
                    <p className="text-red-400">{error}</p>
                </div>
            )}

            {/* Wallet Info Card */}
            <div className="bg-[#1a1a1a] border border-amber-900/20 rounded-xl p-6">
                <div className="flex items-center gap-4 mb-6">
                    <div className="w-16 h-16 bg-linear-to-br from-amber-500 to-yellow-600 rounded-xl flex items-center justify-center">
                        <span className="text-2xl font-bold text-black">{wallet.currency}</span>
                    </div>
                    <div>
                        <h2 className="text-2xl font-bold text-white">
                            ${parseFloat(wallet.balance).toLocaleString('en-US', { minimumFractionDigits: 2 })}
                        </h2>
                        <p className="text-gray-400">Current Balance</p>
                    </div>
                    <div className="ml-auto">
                        <span className={`px-3 py-1 rounded-full text-sm font-medium ${
                            wallet.active 
                                ? 'bg-green-500/10 text-green-500 border border-green-500/20' 
                                : 'bg-red-500/10 text-red-500 border border-red-500/20'
                        }`}>
                            {wallet.active ? 'Active' : 'Inactive'}
                        </span>
                    </div>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-3 gap-4 pt-6 border-t border-amber-900/20">
                    <div>
                        <p className="text-gray-400 text-sm">Wallet ID</p>
                        <p className="text-white font-medium">{wallet.id}</p>
                    </div>
                    <div>
                        <p className="text-gray-400 text-sm">Currency</p>
                        <p className="text-white font-medium">{wallet.currency}</p>
                    </div>
                    <div>
                        <p className="text-gray-400 text-sm">Created At</p>
                        <p className="text-white font-medium">{formatDate(wallet.createdAt)}</p>
                    </div>
                </div>
            </div>

            {/* Quick Stats */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div className="bg-[#1a1a1a] border border-amber-900/20 rounded-xl p-6">
                    <div className="flex items-center justify-between mb-2">
                        <span className="text-gray-400 text-sm">Total Transactions</span>
                        <svg className="w-5 h-5 text-amber-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                        </svg>
                    </div>
                    <p className="text-2xl font-bold text-white">{transactions?.length || 0}</p>
                </div>
                <div className="bg-[#1a1a1a] border border-amber-900/20 rounded-xl p-6">
                    <div className="flex items-center justify-between mb-2">
                        <span className="text-gray-400 text-sm">Received</span>
                        <svg className="w-5 h-5 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 11l5-5m0 0l5 5m-5-5v12" />
                        </svg>
                    </div>
                    <p className="text-2xl font-bold text-green-500">
                        {transactions?.filter(t => t.receiverWalletId === wallet.id).length || 0}
                    </p>
                </div>
                <div className="bg-[#1a1a1a] border border-amber-900/20 rounded-xl p-6">
                    <div className="flex items-center justify-between mb-2">
                        <span className="text-gray-400 text-sm">Sent</span>
                        <svg className="w-5 h-5 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 13l-5 5m0 0l-5-5m5 5V6" />
                        </svg>
                    </div>
                    <p className="text-2xl font-bold text-red-500">
                        {transactions?.filter(t => t.senderWalletId === wallet.id).length || 0}
                    </p>
                </div>
            </div>

            {/* Transaction History */}
            <div className="bg-[#1a1a1a] border border-amber-900/20 rounded-xl overflow-hidden">
                <div className="p-6 border-b border-amber-900/20">
                    <h2 className="text-lg font-semibold text-white">Transaction History</h2>
                    <p className="text-gray-400 text-sm">All transactions for this wallet</p>
                </div>

                {!transactions || transactions.length === 0 ? (
                    <div className="p-12 text-center">
                        <svg className="w-16 h-16 mx-auto text-gray-600 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                        </svg>
                        <p className="text-gray-400">No transactions yet</p>
                    </div>
                ) : (
                    <div className="divide-y divide-amber-900/10">
                        {transactions.map((txn, index) => {
                            const isReceived = txn.receiverWalletId === wallet.id || txn.senderWalletId !== wallet.id;
                            return (
                                <div key={index} className="p-4 hover:bg-[#242424] transition-colors">
                                    <div className="flex items-center justify-between">
                                        <div className="flex items-center gap-4">
                                            <div className={`w-10 h-10 rounded-lg flex items-center justify-center ${
                                                isReceived ? 'bg-green-500/10' : 'bg-red-500/10'
                                            }`}>
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
                                                    {isReceived 
                                                        ? `From: ${txn.senderUserName}` 
                                                        : `To: ${txn.receiverUserName}`
                                                    }
                                                </p>
                                                <p className="text-gray-500 text-sm">{formatDate(txn.createdAt)}</p>
                                            </div>
                                        </div>
                                        <div className="flex items-center gap-4">
                                            <span className={`px-2 py-1 rounded text-xs font-medium border ${getStatusBadge(txn.status)}`}>
                                                {txn.status}
                                            </span>
                                            <span className={`text-lg font-semibold ${
                                                isReceived ? 'text-green-500' : 'text-red-500'
                                            }`}>
                                                {isReceived ? '+' : '-'}${parseFloat(txn.amount).toLocaleString('en-US', { minimumFractionDigits: 2 })}
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            );
                        })}
                    </div>
                )}
            </div>
        </div>
    );
}
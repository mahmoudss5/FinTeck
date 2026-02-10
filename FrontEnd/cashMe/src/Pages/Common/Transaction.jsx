import { useState, useEffect, useMemo } from 'react';
import { Link, Outlet } from 'react-router-dom';
import { useAuth } from '../../context/AuthProvider';
import { getAllTransactions } from '../../services/TransactionService';

import { useLoaderData } from 'react-router-dom';
export default function Transaction() {
    const { user } = useAuth();
    const [filter, setFilter] = useState('all');
    const [searchTerm, setSearchTerm] = useState('');
    const [error, setError] = useState(null);
    const rawTransactions = useLoaderData();

    // Transform backend data to frontend format
    const transactions = useMemo(() => {
        if (!rawTransactions || !Array.isArray(rawTransactions)) return [];
        
        return rawTransactions.map((txn, index) => ({
            id: `TXN${String(index + 1).padStart(3, '0')}`,
            type: txn.receiverUserName === user?.userName ? 'received' : 'sent',
            amount: parseFloat(txn.amount),
            senderName: txn.senderUserName,
            receiverName: txn.receiverUserName,
            status: txn.status?.toLowerCase() || 'completed',
            date: txn.createdAt ? new Date(txn.createdAt).toLocaleDateString('en-US') : 'N/A',
            time: txn.createdAt ? new Date(txn.createdAt).toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit' }) : 'N/A',
            currency: txn.currency || 'USD'
        }));
    }, [rawTransactions, user?.userName]);

    // Filter transactions
    
    const filteredTransactions = transactions.filter(txn => {
        const matchesFilter = filter === 'all' || txn.type === filter;
        const matchesSearch = 
            txn.id.toLowerCase().includes(searchTerm.toLowerCase()) ||
            txn.senderName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
            txn.receiverName?.toLowerCase().includes(searchTerm.toLowerCase());
        return matchesFilter && matchesSearch;
    });

    // Calculate totals
    const totalReceived = transactions.filter(t => t.type === 'received').reduce((sum, t) => sum + t.amount, 0);
    const totalSent = transactions.filter(t => t.type === 'sent').reduce((sum, t) => sum + t.amount, 0);

    const getStatusBadge = (status) => {
        const styles = {
            completed: 'bg-green-500/10 text-green-500 border-green-500/20',
            pending: 'bg-yellow-500/10 text-yellow-500 border-yellow-500/20',
            failed: 'bg-red-500/10 text-red-500 border-red-500/20'
        };
        return styles[status] || styles.pending;
    };

    if (error) {
        return (
            <div className="bg-red-500/10 border border-red-500/20 rounded-xl p-6 text-center">
                <p className="text-red-400">Error loading transactions: {error}</p>
            </div>
        );
    }

    return (
        <div className="space-y-6">
            {/* Header Section */}
            <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
                <div>
                    <h1 className="text-2xl font-bold text-white">Transactions</h1>
                    <p className="text-gray-400">View and manage all your transactions</p>
                </div>
                <Link to="add">
                    <button className="cursor-pointer px-4 py-2 bg-linear-to-r from-amber-600 via-yellow-500 to-amber-600 text-black font-semibold rounded-lg hover:from-amber-500 hover:via-yellow-400 hover:to-amber-500 transition-all duration-200 shadow-lg shadow-amber-500/20 flex items-center gap-2 w-fit">
                        New Transfer +
                    </button>
                </Link>
            </div>

            {/* Summary Cards */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                {/* Total Transactions */}
                <div className="bg-[#1a1a1a] border border-amber-900/20 rounded-xl p-6">
                    <div className="flex items-center justify-between mb-4">
                        <span className="text-gray-400 text-sm">Total Transactions</span>
                        <span className="w-10 h-10 bg-amber-500/10 rounded-lg flex items-center justify-center">
                            <svg className="w-5 h-5 text-amber-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                            </svg>
                        </span>
                    </div>
                    <p className="text-2xl font-bold text-white">{transactions.length}</p>
                    <p className="text-sm text-gray-500 mt-2">All time</p>
                </div>

                {/* Money Received */}
                <div className="bg-[#1a1a1a] border border-amber-900/20 rounded-xl p-6">
                    <div className="flex items-center justify-between mb-4">
                        <span className="text-gray-400 text-sm">Money Received</span>
                        <span className="w-10 h-10 bg-green-500/10 rounded-lg flex items-center justify-center">
                            <svg className="w-5 h-5 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 11l5-5m0 0l5 5m-5-5v12" />
                            </svg>
                        </span>
                    </div>
                    <p className="text-2xl font-bold text-green-500">+${totalReceived.toLocaleString('en-US', { minimumFractionDigits: 2 })}</p>
                    <p className="text-sm text-gray-500 mt-2">{transactions.filter(t => t.type === 'received').length} transactions</p>
                </div>

                {/* Money Sent */}
                <div className="bg-[#1a1a1a] border border-amber-900/20 rounded-xl p-6">
                    <div className="flex items-center justify-between mb-4">
                        <span className="text-gray-400 text-sm">Money Sent</span>
                        <span className="w-10 h-10 bg-red-500/10 rounded-lg flex items-center justify-center">
                            <svg className="w-5 h-5 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 13l-5 5m0 0l-5-5m5 5V6" />
                            </svg>
                        </span>
                    </div>
                    <p className="text-2xl font-bold text-red-500">-${totalSent.toLocaleString('en-US', { minimumFractionDigits: 2 })}</p>
                    <p className="text-sm text-gray-500 mt-2">{transactions.filter(t => t.type === 'sent').length} transactions</p>
                </div>
            </div>

            {/* Filters and Search */}
            <div className="bg-[#1a1a1a] border border-amber-900/20 rounded-xl p-4">
                <div className="flex flex-col md:flex-row gap-4">
                    {/* Search */}
                    <div className="relative flex-1">
                        <svg className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                        </svg>
                        <input
                            type="text"
                            placeholder="Search transactions..."
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                            className="w-full bg-[#242424] border border-amber-900/20 rounded-lg pl-10 pr-4 py-2 text-white placeholder-gray-500 focus:outline-none focus:border-amber-500/50"
                        />
                    </div>

                    {/* Filter Buttons */}
                    <div className="flex gap-2">
                        <button
                            onClick={() => setFilter('all')}
                            className={`px-4 py-2 rounded-lg font-medium transition-all duration-200 ${
                                filter === 'all'
                                    ? 'bg-amber-500 text-black'
                                    : 'bg-[#242424] text-gray-400 hover:text-white border border-amber-900/20'
                            }`}
                        >
                            All
                        </button>
                        <button
                            onClick={() => setFilter('received')}
                            className={`px-4 py-2 rounded-lg font-medium transition-all duration-200 ${
                                filter === 'received'
                                    ? 'bg-green-500 text-black'
                                    : 'bg-[#242424] text-gray-400 hover:text-white border border-amber-900/20'
                            }`}
                        >
                            Received
                        </button>
                        <button
                            onClick={() => setFilter('sent')}
                            className={`px-4 py-2 rounded-lg font-medium transition-all duration-200 ${
                                filter === 'sent'
                                    ? 'bg-red-500 text-black'
                                    : 'bg-[#242424] text-gray-400 hover:text-white border border-amber-900/20'
                            }`}
                        >
                            Sent
                        </button>
                    </div>
                </div>
            </div>

            {/* Transactions List */}
            <div className="bg-[#1a1a1a] border border-amber-900/20 rounded-xl overflow-hidden">
                {/* Table Header */}
                <div className="hidden md:grid grid-cols-12 gap-4 p-4 bg-[#242424] border-b border-amber-900/20 text-gray-400 text-sm font-medium">
                    <div className="col-span-4">Transaction</div>
                    <div className="col-span-2">Date</div>
                    <div className="col-span-2">Currency</div>
                    <div className="col-span-2">Status</div>
                    <div className="col-span-2 text-right">Amount</div>
                </div>

                {/* Transaction Items */}
                <div className="divide-y divide-amber-900/10">
                    {filteredTransactions.length === 0 ? (
                        <div className="p-8 text-center">
                            <svg className="w-16 h-16 mx-auto text-gray-600 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                            </svg>
                            <p className="text-gray-400">No transactions found</p>
                        </div>
                    ) : (
                        filteredTransactions.map((txn) => (
                            <div
                                key={txn.id}
                                className="grid grid-cols-1 md:grid-cols-12 gap-4 p-4 hover:bg-[#242424] transition-colors cursor-pointer"
                            >
                                {/* Transaction Info */}
                                <div className="col-span-1 md:col-span-4 flex items-center gap-4">
                                    <div className={`w-10 h-10 rounded-lg flex items-center justify-center ${
                                        txn.type === 'received' ? 'bg-green-500/10 text-green-500' : 'bg-red-500/10 text-red-500'
                                    }`}>
                                        {txn.type === 'received' ? (
                                            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 11l5-5m0 0l5 5m-5-5v12" />
                                            </svg>
                                        ) : (
                                            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 13l-5 5m0 0l-5-5m5 5V6" />
                                            </svg>
                                        )}
                                    </div>
                                    <div className="flex-1 min-w-0">
                                        <p className="text-white font-medium truncate">
                                            {txn.type === 'received' ? `From: ${txn.senderName}` : `To: ${txn.receiverName}`}
                                        </p>
                                        <p className="text-gray-600 text-xs md:hidden mt-1">{txn.id}</p>
                                    </div>
                                </div>

                                {/* Date */}
                                <div className="col-span-1 md:col-span-2 flex items-center">
                                    <div className="md:block">
                                        <p className="text-white text-sm">{txn.date}</p>
                                        <p className="text-gray-500 text-xs">{txn.time}</p>
                                    </div>
                                </div>

                                {/* Currency */}
                                <div className="col-span-1 md:col-span-2 flex items-center">
                                    <span className="text-amber-400 text-sm">{txn.currency}</span>
                                </div>

                                {/* Status */}
                                <div className="col-span-1 md:col-span-2 flex items-center">
                                    <span className={`px-3 py-1 rounded-full text-xs font-medium border ${getStatusBadge(txn.status)}`}>
                                        {txn.status.charAt(0).toUpperCase() + txn.status.slice(1)}
                                    </span>
                                </div>

                                {/* Amount */}
                                <div className="col-span-1 md:col-span-2 flex items-center md:justify-end">
                                    <span className={`text-lg font-semibold ${
                                        txn.type === 'received' ? 'text-green-500' : 'text-red-500'
                                    }`}>
                                        {txn.type === 'received' ? '+' : '-'}${txn.amount.toLocaleString('en-US', { minimumFractionDigits: 2 })}
                                    </span>
                                </div>
                            </div>
                        ))
                    )}
                </div>
            </div>

            {/* Pagination */}
            <div className="flex items-center justify-between">
                <p className="text-gray-500 text-sm">
                    Showing {filteredTransactions.length} of {transactions.length} transactions
                </p>
            </div>

            {/* Outlet for nested routes like CreateTransaction modal */}
            <Outlet />
        </div>
    );
}
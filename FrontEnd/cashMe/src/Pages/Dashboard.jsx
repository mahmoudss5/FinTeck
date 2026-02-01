import { useAuth } from "../services/AuthProvider";

export default function Dashboard() {
    
    const {user} = useAuth();
    console.log(user);
    return (
        <div className="space-y-6">
            {/* Welcome Section */}
            <div className="flex items-center justify-between">
                <div>
                    <h1 className="text-2xl font-bold text-white">Welcome back, {user.userName}!</h1>
                    <p className="text-gray-400">Here's what's happening with your finances today.</p>
                </div>
                <button className="px-4 py-2 bg-gradient-to-r from-amber-600 via-yellow-500 to-amber-600 text-black font-semibold rounded-lg hover:from-amber-500 hover:via-yellow-400 hover:to-amber-500 transition-all duration-200 shadow-lg shadow-amber-500/20">
                    + Add Transaction
                </button>
            </div>

            {/* Stats Cards */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                {/* Total Balance */}
                <div className="bg-[#1a1a1a] border border-amber-900/20 rounded-xl p-6">
                    <div className="flex items-center justify-between mb-4">
                        <span className="text-gray-400 text-sm">Total Balance</span>
                        <span className="w-10 h-10 bg-amber-500/10 rounded-lg flex items-center justify-center">
                            <svg className="w-5 h-5 text-amber-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                            </svg>
                        </span>
                    </div>
                    <p className="text-2xl font-bold text-white">$24,563.00</p>
                    <p className="text-sm text-green-500 mt-2 flex items-center gap-1">
                        <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 10l7-7m0 0l7 7m-7-7v18" />
                        </svg>
                        +12.5% from last month
                    </p>
                </div>

                {/* Income */}
                <div className="bg-[#1a1a1a] border border-amber-900/20 rounded-xl p-6">
                    <div className="flex items-center justify-between mb-4">
                        <span className="text-gray-400 text-sm">Income</span>
                        <span className="w-10 h-10 bg-green-500/10 rounded-lg flex items-center justify-center">
                            <svg className="w-5 h-5 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 11l5-5m0 0l5 5m-5-5v12" />
                            </svg>
                        </span>
                    </div>
                    <p className="text-2xl font-bold text-white">$8,350.00</p>
                    <p className="text-sm text-green-500 mt-2 flex items-center gap-1">
                        <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 10l7-7m0 0l7 7m-7-7v18" />
                        </svg>
                        +8.2% from last month
                    </p>
                </div>

                {/* Expenses */}
                <div className="bg-[#1a1a1a] border border-amber-900/20 rounded-xl p-6">
                    <div className="flex items-center justify-between mb-4">
                        <span className="text-gray-400 text-sm">Expenses</span>
                        <span className="w-10 h-10 bg-red-500/10 rounded-lg flex items-center justify-center">
                            <svg className="w-5 h-5 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 13l-5 5m0 0l-5-5m5 5V6" />
                            </svg>
                        </span>
                    </div>
                    <p className="text-2xl font-bold text-white">$3,820.00</p>
                    <p className="text-sm text-red-500 mt-2 flex items-center gap-1">
                        <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 14l-7 7m0 0l-7-7m7 7V3" />
                        </svg>
                        +3.1% from last month
                    </p>
                </div>

                {/* Savings */}
                <div className="bg-[#1a1a1a] border border-amber-900/20 rounded-xl p-6">
                    <div className="flex items-center justify-between mb-4">
                        <span className="text-gray-400 text-sm">Savings</span>
                        <span className="w-10 h-10 bg-blue-500/10 rounded-lg flex items-center justify-center">
                            <svg className="w-5 h-5 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 9V7a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m2 4h10a2 2 0 002-2v-6a2 2 0 00-2-2H9a2 2 0 00-2 2v6a2 2 0 002 2zm7-5a2 2 0 11-4 0 2 2 0 014 0z" />
                            </svg>
                        </span>
                    </div>
                    <p className="text-2xl font-bold text-white">$4,530.00</p>
                    <p className="text-sm text-green-500 mt-2 flex items-center gap-1">
                        <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 10l7-7m0 0l7 7m-7-7v18" />
                        </svg>
                        +18.7% from last month
                    </p>
                </div>
            </div>

            {/* Recent Transactions */}
            <div className="bg-[#1a1a1a] border border-amber-900/20 rounded-xl p-6">
                <div className="flex items-center justify-between mb-6">
                    <h2 className="text-lg font-semibold text-white">Recent Transactions</h2>
                    <button className="text-amber-400 text-sm hover:text-amber-300 transition-colors">View All</button>
                </div>
                <div className="space-y-4">
                    {/* Transaction Item */}
                    <div className="flex items-center justify-between p-4 bg-[#242424] rounded-lg">
                        <div className="flex items-center gap-4">
                            <div className="w-10 h-10 bg-green-500/10 rounded-lg flex items-center justify-center">
                                <svg className="w-5 h-5 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 11l5-5m0 0l5 5m-5-5v12" />
                                </svg>
                            </div>
                            <div>
                                <p className="text-white font-medium">Salary Deposit</p>
                                <p className="text-gray-500 text-sm">Jan 27, 2026</p>
                            </div>
                        </div>
                        <span className="text-green-500 font-semibold">+$5,000.00</span>
                    </div>

                    {/* Transaction Item */}
                    <div className="flex items-center justify-between p-4 bg-[#242424] rounded-lg">
                        <div className="flex items-center gap-4">
                            <div className="w-10 h-10 bg-red-500/10 rounded-lg flex items-center justify-center">
                                <svg className="w-5 h-5 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 13l-5 5m0 0l-5-5m5 5V6" />
                                </svg>
                            </div>
                            <div>
                                <p className="text-white font-medium">Netflix Subscription</p>
                                <p className="text-gray-500 text-sm">Jan 25, 2026</p>
                            </div>
                        </div>
                        <span className="text-red-500 font-semibold">-$15.99</span>
                    </div>

                    {/* Transaction Item */}
                    <div className="flex items-center justify-between p-4 bg-[#242424] rounded-lg">
                        <div className="flex items-center gap-4">
                            <div className="w-10 h-10 bg-red-500/10 rounded-lg flex items-center justify-center">
                                <svg className="w-5 h-5 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 13l-5 5m0 0l-5-5m5 5V6" />
                                </svg>
                            </div>
                            <div>
                                <p className="text-white font-medium">Grocery Store</p>
                                <p className="text-gray-500 text-sm">Jan 24, 2026</p>
                            </div>
                        </div>
                        <span className="text-red-500 font-semibold">-$87.50</span>
                    </div>

                    {/* Transaction Item */}
                    <div className="flex items-center justify-between p-4 bg-[#242424] rounded-lg">
                        <div className="flex items-center gap-4">
                            <div className="w-10 h-10 bg-green-500/10 rounded-lg flex items-center justify-center">
                                <svg className="w-5 h-5 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 11l5-5m0 0l5 5m-5-5v12" />
                                </svg>
                            </div>
                            <div>
                                <p className="text-white font-medium">Freelance Payment</p>
                                <p className="text-gray-500 text-sm">Jan 22, 2026</p>
                            </div>
                        </div>
                        <span className="text-green-500 font-semibold">+$850.00</span>
                    </div>
                </div>
            </div>
        </div>
    )
}

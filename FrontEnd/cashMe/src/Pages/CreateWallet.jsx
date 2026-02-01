import { Link } from 'react-router-dom'

export default function CreateWallet() {
    return (
        <div className="max-w-2xl mx-auto space-y-6">
            {/* Header */}
            <div className="flex items-center gap-4">
                <Link to="/home/wallets" className="p-2 text-gray-400 hover:text-amber-400 hover:bg-amber-500/10 rounded-lg transition-all">
                    <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
                    </svg>
                </Link>
                <div>
                    <h1 className="text-2xl font-bold text-white">Create New Wallet</h1>
                    <p className="text-gray-400 text-sm">Set up a new wallet to manage your funds</p>
                </div>
            </div>

            {/* Form Card */}
            <div className="bg-[#1a1a1a] border border-amber-900/20 rounded-xl p-6">
                <form className="space-y-6">
                    {/* Wallet Name */}
                    <div>
                        <label className="block text-sm font-medium text-gray-300 mb-2">
                            Wallet Name
                        </label>
                        <input 
                            type="text" 
                            placeholder="e.g., My Savings"
                            className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all"
                        />
                    </div>

                    {/* Email */}
                    <div>
                        <label className="block text-sm font-medium text-gray-300 mb-2">
                            Email Address
                        </label>
                        <input 
                            type="email" 
                            placeholder="you@example.com"
                            className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all"
                        />
                    </div>

                    {/* Currency */}
                    <div>
                        <label className="block text-sm font-medium text-gray-300 mb-2">
                            Currency
                        </label>
                        <select className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all">
                            <option value="">Select currency</option>
                            <option value="USD">🇺🇸 USD - US Dollar</option>
                            <option value="EUR">🇪🇺 EUR - Euro</option>
                            <option value="GBP">🇬🇧 GBP - British Pound</option>
                            <option value="JPY">🇯🇵 JPY - Japanese Yen</option>
                            <option value="CAD">🇨🇦 CAD - Canadian Dollar</option>
                            <option value="AUD">🇦🇺 AUD - Australian Dollar</option>
                            <option value="CHF">🇨🇭 CHF - Swiss Franc</option>
                        </select>
                    </div>

                    {/* Initial Deposit */}
                    <div>
                        <label className="block text-sm font-medium text-gray-300 mb-2">
                            Initial Deposit <span className="text-gray-500">(Optional)</span>
                        </label>
                        <div className="relative">
                            <span className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-500">$</span>
                            <input 
                                type="number" 
                                placeholder="0.00"
                                min="0"
                                step="0.01"
                                className="w-full pl-8 pr-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all"
                            />
                        </div>
                        <p className="text-xs text-gray-500 mt-2">You can add funds later from your dashboard</p>
                    </div>

                    {/* Description */}
                    <div>
                        <label className="block text-sm font-medium text-gray-300 mb-2">
                            Description <span className="text-gray-500">(Optional)</span>
                        </label>
                        <textarea 
                            placeholder="Add a note about this wallet..."
                            rows={3}
                            className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all resize-none"
                        />
                    </div>

                    {/* Password */}
                    <div>
                        <label className="block text-sm font-medium text-gray-300 mb-2">
                            Wallet Password
                        </label>
                        <div className="relative">
                            <input 
                                type="password" 
                                placeholder="••••••••"
                                className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all"
                            />
                            <svg className="w-5 h-5 text-gray-500 absolute right-4 top-1/2 -translate-y-1/2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                            </svg>
                        </div>
                        <p className="text-xs text-gray-500 mt-2">Used to authorize transactions from this wallet</p>
                    </div>

                    {/* Confirm Password */}
                    <div>
                        <label className="block text-sm font-medium text-gray-300 mb-2">
                            Confirm Password
                        </label>
                        <div className="relative">
                            <input 
                                type="password" 
                                placeholder="••••••••"
                                className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all"
                            />
                            <svg className="w-5 h-5 text-gray-500 absolute right-4 top-1/2 -translate-y-1/2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                            </svg>
                        </div>
                    </div>

                    {/* Divider */}
                    <div className="border-t border-amber-900/20 pt-6">
                        {/* Terms */}
                        <div className="flex items-start gap-3 mb-6">
                            <input 
                                type="checkbox" 
                                id="terms"
                                className="w-5 h-5 mt-0.5 bg-[#242424] border-amber-900/30 rounded text-amber-500 focus:ring-amber-500 focus:ring-offset-[#1a1a1a]"
                            />
                            <label htmlFor="terms" className="text-sm text-gray-400">
                                I understand that this wallet is subject to CashMe's <a href="#" className="text-amber-400 hover:text-amber-300">Terms of Service</a> and <a href="#" className="text-amber-400 hover:text-amber-300">Privacy Policy</a>
                            </label>
                        </div>

                        {/* Buttons */}
                        <div className="flex gap-4">
                            <Link to="/home/wallets" className="flex-1">
                                <button 
                                    type="button"
                                    className="w-full py-3 px-4 border border-amber-900/30 text-gray-300 font-medium rounded-lg hover:bg-amber-500/10 hover:text-white transition-all duration-200"
                                >
                                    Cancel
                                </button>
                            </Link>
                            <button 
                                type="submit" 
                                className="flex-1 py-3 px-4 bg-gradient-to-r from-amber-600 via-yellow-500 to-amber-600 text-black font-semibold rounded-lg hover:from-amber-500 hover:via-yellow-400 hover:to-amber-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:ring-offset-2 focus:ring-offset-[#1a1a1a] transform hover:scale-[1.02] transition-all duration-200 shadow-lg shadow-amber-500/20"
                            >
                                Create Wallet
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    )
}
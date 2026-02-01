import { Link } from 'react-router-dom'

export default function AvailableWallets({ Wallets }) {
    return (
        <div className="space-y-6">
            {/* Header */}
            <div className="flex items-center justify-between">
                <div>
                    <h2 className="text-xl font-semibold text-white">Your Wallets</h2>
                    <p className="text-gray-400 text-sm">{Wallets.length} wallet{Wallets.length !== 1 ? 's' : ''} available</p>
                </div>
                <Link to="/home/wallets/create">
                    <button className="px-4 py-2 bg-gradient-to-r from-amber-600 via-yellow-500 to-amber-600 text-black font-semibold rounded-lg hover:from-amber-500 hover:via-yellow-400 hover:to-amber-500 transition-all duration-200 shadow-lg shadow-amber-500/20 flex items-center gap-2 text-sm">
                        <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
                        </svg>
                        Add Wallet
                    </button>
                </Link>
            </div>

            {/* Wallets Grid */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {Wallets.map((wallet, index) => (
                    <Link to={`/home/wallets/${wallet.id || index}`} key={index}>
                        <div className="bg-[#1a1a1a] border border-amber-900/20 rounded-xl p-5 hover:border-amber-500/50 hover:shadow-lg hover:shadow-amber-500/10 transition-all duration-300 cursor-pointer group">
                            {/* Wallet Header */}
                            <div className="flex items-center justify-between mb-4">
                                <div className="w-12 h-12 bg-gradient-to-br from-amber-500 to-yellow-600 rounded-xl flex items-center justify-center shadow-lg shadow-amber-500/20">
                                    <svg className="w-6 h-6 text-black" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z" />
                                    </svg>
                                </div>
                                <span className="text-xs text-gray-500 bg-[#242424] px-2 py-1 rounded-full">
                                    {wallet.currency}
                                </span>
                            </div>

                            {/* Balance */}
                            <div className="mb-4">
                                <p className="text-gray-400 text-sm mb-1">Balance</p>
                                <p className="text-2xl font-bold text-white group-hover:text-amber-400 transition-colors">
                                    {Number(wallet.walletBalance)} <span className="text-sm text-gray-500">{wallet.walletCurrency}</span>
                                </p>
                            </div>

                            {/* Wallet Info */}
                            <div className="pt-4 border-t border-amber-900/20 space-y-2">
                                <div className="flex items-center justify-between text-sm">
                                    <span className="text-gray-500">Created</span>
                                    <span className="text-gray-300">{wallet.walletCreatedAt}</span>
                                </div>
                                <div className="flex items-center justify-between text-sm">
                                    <span className="text-gray-500">Last Updated</span>
                                    <span className="text-gray-300">{wallet.walletUpdatedAt}</span>
                                </div>
                            </div>

                            {/* View Details Arrow */}
                            <div className="mt-4 flex items-center justify-end text-amber-500 opacity-0 group-hover:opacity-100 transition-opacity">
                                <span className="text-sm mr-1">View Details</span>
                                <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
                                </svg>
                            </div>
                        </div>
                    </Link>
                ))}
            </div>
        </div>
    )
}
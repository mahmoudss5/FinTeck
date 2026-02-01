import { Link } from "react-router-dom";

export default function NoWalletsFound() {
    return (
        <div className="flex flex-col justify-center items-center py-20">
            {/* Empty State Icon */}
            <div className="w-24 h-24 bg-amber-500/10 rounded-full flex items-center justify-center mb-6">
                <svg className="w-12 h-12 text-amber-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z" />
                </svg>
            </div>

            {/* Message */}
            <h2 className="text-xl font-semibold text-white mb-2">No Wallets Found</h2>
            <p className="text-gray-400 text-center mb-8 max-w-md">
                You haven't created any wallets yet. Create your first wallet to start managing your funds.
            </p>

            {/* Create Button */}
            <Link to="/home/wallets/create">
                <button className="px-6 py-3 bg-gradient-to-r from-amber-600 via-yellow-500 to-amber-600 text-black font-semibold rounded-lg hover:from-amber-500 hover:via-yellow-400 hover:to-amber-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:ring-offset-2 focus:ring-offset-[#242424] transform hover:scale-105 transition-all duration-200 shadow-lg shadow-amber-500/20 flex items-center gap-2">
                    <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
                    </svg>
                    Create Wallet
                </button>
            </Link>
        </div>
    )
}
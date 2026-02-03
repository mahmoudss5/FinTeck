export default function WalletsTable({ wallets, actionLoading, onDeactivate, formatDate }) {
    return (
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
                                        <button 
                                            className='cursor-pointer hover:text-white transition-colors'
                                            onClick={() => onDeactivate(wallet.id)}
                                            disabled={actionLoading === wallet.id}
                                        >
                                            {actionLoading === wallet.id ? '...' : (wallet.active ? 'Active' : 'Inactive')}
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
    );
}

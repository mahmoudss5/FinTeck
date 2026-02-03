export default function UsersTable({ 
    users, 
    isOwner, 
    actionLoading, 
    onDelete, 
    onPromote, 
    onDemote, 
    formatDate 
}) {
    return (
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
                        {users.map((user) => {
                            const isUserOwner = user.roles?.some(r => r.toLowerCase().includes('owner'));
                            const isUserAdmin = user.roles?.some(r => r.toLowerCase().includes('admin'));
                            return (
                                <tr key={user.id} className={`hover:bg-[#242424] transition-colors ${
                                    isUserOwner 
                                        ? 'bg-gradient-to-r from-amber-500/10 to-transparent border-l-4 border-l-amber-400' 
                                        : isUserAdmin 
                                            ? 'bg-gradient-to-r from-purple-500/10 to-transparent border-l-4 border-l-purple-400' 
                                            : ''
                                }`}>
                                    <td className="px-6 py-4 text-gray-400">#{user.id}</td>
                                    <td className="px-6 py-4 text-white font-medium">{user.userName}</td>
                                    <td className="px-6 py-4 text-gray-400">{user.email}</td>
                                    <td className="px-6 py-4 text-amber-400">{user.wallets?.length || 0}</td>
                                    <td className="px-6 py-4">
                                        {user.roles?.map((role, i) => {
                                            const roleLower = role.toLowerCase();
                                            let colorClass = 'bg-blue-500/10 text-blue-400 border border-blue-500/20';
                                            if (roleLower.includes('owner')) {
                                                colorClass = 'bg-amber-500/10 text-amber-400 border border-amber-500/20';
                                            } else if (roleLower.includes('admin')) {
                                                colorClass = 'bg-purple-500/10 text-purple-400 border border-purple-500/20';
                                            }
                                            return (
                                                <span key={i} className={`px-2 py-1 rounded text-xs font-medium mr-1 ${colorClass}`}>
                                                    {role.replace('ROLE_', '')}
                                                </span>
                                            );
                                        })}
                                    </td>
                                    <td className="px-6 py-4 text-gray-400">{formatDate(user.createdAt)}</td>
                                    <td className="px-6 py-4">
                                        <button
                                            onClick={() => onDelete(user.id)}
                                            disabled={actionLoading === user.id || isUserOwner || (!isOwner && isUserAdmin)}
                                            className={`px-3 py-1 rounded text-sm transition-colors ${
                                                isUserOwner || (!isOwner && isUserAdmin)
                                                    ? 'bg-gray-500/10 border border-gray-500/20 text-gray-500 cursor-not-allowed'
                                                    : 'bg-red-500/10 border border-red-500/20 text-red-400 hover:bg-red-500/20 disabled:opacity-50'
                                            }`}
                                        >
                                            {actionLoading === user.id ? '...' : 'Delete'}
                                        </button>

                                        {isUserAdmin ? (
                                            <button
                                                onClick={() => onDemote(user.id)}
                                                disabled={actionLoading === user.id || isUserOwner || !isOwner}
                                                className={`ml-2 px-3 py-1 rounded text-sm transition-colors ${
                                                    isUserOwner || !isOwner
                                                        ? 'bg-gray-500/10 border border-gray-500/20 text-gray-500 cursor-not-allowed'
                                                        : 'bg-orange-500/10 border border-orange-500/20 text-orange-400 hover:bg-orange-500/20 disabled:opacity-50'
                                                }`}
                                            >
                                                {actionLoading === user.id ? '...' : 'Depromote'}
                                            </button>
                                        ) : (
                                            <button
                                                onClick={() => onPromote(user.id)}
                                                disabled={actionLoading === user.id || isUserOwner}
                                                className={`ml-2 px-3 py-1 rounded text-sm transition-colors ${
                                                    isUserOwner
                                                        ? 'bg-gray-500/10 border border-gray-500/20 text-gray-500 cursor-not-allowed'
                                                        : 'bg-green-500/10 border border-green-500/20 text-green-400 hover:bg-green-500/20 disabled:opacity-50'
                                                }`}
                                            >
                                                {actionLoading === user.id ? '...' : 'Promote'}
                                            </button>
                                        )}
                                    </td>
                                </tr>
                            );
                        })}
                    </tbody>
                </table>
            </div>
        </div>
    );
}

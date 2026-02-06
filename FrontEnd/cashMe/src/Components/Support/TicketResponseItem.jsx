import { formatCategoryName, formatStatusName, getStatusColor } from '../../constants/ticketConstants';

export default function TicketResponseItem({ response, currentUserId, onDelete }) {
    const isOwnResponse = response.senderId === currentUserId;
    const isAdminResponse = response.senderUserName?.toLowerCase().includes('admin');

    const formatDate = (dateString) => {
        if (!dateString) return 'N/A';
        return new Date(dateString).toLocaleString('en-US', {
            month: 'short',
            day: 'numeric',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    return (
        <div className={`p-4 rounded-lg border ${isAdminResponse ? 'bg-amber-500/5 border-amber-500/20' : 'bg-[#242424] border-red-900/10'}`}>
            <div className="flex items-start justify-between mb-2">
                <div className="flex items-center gap-2">
                    <div className={`w-8 h-8 rounded-full flex items-center justify-center text-sm font-semibold ${
                        isAdminResponse ? 'bg-amber-500/20 text-amber-400' : 'bg-blue-500/20 text-blue-400'
                    }`}>
                        {response.senderUserName?.charAt(0).toUpperCase()}
                    </div>
                    <div>
                        <p className="text-white font-medium">{response.senderUserName}</p>
                        <p className="text-gray-500 text-xs">{formatDate(response.createdAt)}</p>
                    </div>
                    {isAdminResponse && (
                        <span className="px-2 py-0.5 bg-amber-500/20 text-amber-400 text-xs rounded">
                            Admin
                        </span>
                    )}
                </div>
                {isOwnResponse && onDelete && (
                    <button
                        onClick={() => onDelete(response.id)}
                        className="text-red-400 hover:text-red-300 text-sm"
                    >
                        Delete
                    </button>
                )}
            </div>
            <p className="text-gray-300 whitespace-pre-wrap">{response.responseMessage}</p>
        </div>
    );
}

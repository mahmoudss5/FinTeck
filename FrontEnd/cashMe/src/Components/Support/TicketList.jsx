import { formatCategoryName, formatStatusName, getStatusColor } from '../../constants/ticketConstants';

export default function TicketList({ tickets, onTicketSelect, onCreateTicket }) {
    const formatDate = (dateString) => {
        if (!dateString) return 'N/A';
        return new Date(dateString).toLocaleDateString('en-US', {
            month: 'short',
            day: 'numeric',
            year: 'numeric'
        });
    };

    const getStatusBadgeClasses = (status) => {
        const color = getStatusColor(status);
        const colorMap = {
            blue: 'bg-blue-500/20 text-blue-400 border-blue-500/30',
            yellow: 'bg-yellow-500/20 text-yellow-400 border-yellow-500/30',
            green: 'bg-green-500/20 text-green-400 border-green-500/30',
            gray: 'bg-gray-500/20 text-gray-400 border-gray-500/30'
        };
        return colorMap[color] || colorMap.gray;
    };

    if (tickets.length === 0) {
        return (
            <div className="bg-[#1a1a1a] border border-red-900/20 rounded-xl p-12 text-center">
                <div className="w-20 h-20 bg-red-500/10 rounded-full flex items-center justify-center mx-auto mb-4">
                    <svg className="w-10 h-10 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z" />
                    </svg>
                </div>
                <h3 className="text-xl font-semibold text-white mb-2">No Support Tickets</h3>
                <p className="text-gray-400 mb-6">You haven't created any support tickets yet.</p>
                <button
                    onClick={onCreateTicket}
                    className="px-6 py-3 bg-red-600 hover:bg-red-700 text-white rounded-lg font-medium transition-colors"
                >
                    Create Your First Ticket
                </button>
            </div>
        );
    }

    return (
        <div className="space-y-4">
            <div className="flex items-center justify-between">
                <h2 className="text-xl font-bold text-white">Your Support Tickets</h2>
                <button
                    onClick={onCreateTicket}
                    className="px-4 py-2 bg-red-600 hover:bg-red-700 text-white rounded-lg font-medium transition-colors flex items-center gap-2"
                >
                    <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
                    </svg>
                    New Ticket
                </button>
            </div>

            <div className="grid gap-4">
                {tickets.map((ticket) => (
                    <div
                        key={ticket.id}
                        onClick={() => onTicketSelect(ticket)}
                        className="bg-[#1a1a1a] border border-red-900/20 rounded-xl p-6 hover:border-red-500/30 transition-all cursor-pointer group"
                    >
                        <div className="flex items-start justify-between mb-3">
                            <div className="flex-1">
                                <h3 className="text-lg font-semibold text-white group-hover:text-red-400 transition-colors mb-1">
                                    {ticket.subject}
                                </h3>
                                <p className="text-gray-400 text-sm">
                                    Created {formatDate(ticket.createdAt)}
                                </p>
                            </div>
                            <span className={`px-3 py-1 rounded-full text-sm font-medium border ${getStatusBadgeClasses(ticket.ticketStatus)}`}>
                                {formatStatusName(ticket.ticketStatus)}
                            </span>
                        </div>

                        {ticket.description && (
                            <p className="text-gray-300 line-clamp-2 mb-3">
                                {ticket.description}
                            </p>
                        )}

                        <div className="flex items-center gap-3 text-sm">
                            <span className="px-2 py-1 bg-[#242424] text-gray-400 rounded">
                                {formatCategoryName(ticket.ticketCategory)}
                            </span>
                            <span className="text-gray-500">
                                Ticket #{ticket.id}
                            </span>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

import { useState } from 'react';
import { formatCategoryName, formatStatusName, getStatusColor, TICKET_STATUS } from '../../constants/ticketConstants';
import TicketResponseItem from './TicketResponseItem';
import { useAuth } from '../../services/AuthProvider';
import { deleteTicketResponse, updateTicketStatus } from '../../services/TicketService';

export default function TicketDetail({ ticket, responses, onBack, onResponseAdded, onStatusUpdated }) {
    const [responseText, setResponseText] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [error, setError] = useState(null);
    const { user } = useAuth();
    const isAdmin = user?.roles?.some(role => role.toLowerCase() === 'admin' || role.toLowerCase() === 'owner');

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

    const handleSubmitResponse = async (e) => {
        e.preventDefault();
        if (!responseText.trim()) return;

        try {
            setIsSubmitting(true);
            setError(null);
            await onResponseAdded(ticket.id, responseText);
            setResponseText('');
        } catch (err) {
            setError(err.message);
        } finally {
            setIsSubmitting(false);
        }
    };

    const handleDeleteResponse = async (responseId) => {
        if (!confirm('Are you sure you want to delete this response?')) return;

        try {
            await deleteTicketResponse(responseId);
            // Refresh responses by calling parent
            window.location.reload(); // Simple refresh for now
        } catch (err) {
            alert('Failed to delete response: ' + err.message);
        }
    };

    const handleStatusUpdate = async (newStatus) => {
        try {
            await updateTicketStatus(ticket.id, newStatus);
            onStatusUpdated && onStatusUpdated(ticket.id, newStatus);
        } catch (err) {
            alert('Failed to update status: ' + err.message);
        }
    };

    return (
        <div className="space-y-6">
            {/* Header */}
            <div className="flex items-start justify-between">
                <button
                    onClick={onBack}
                    className="flex items-center gap-2 text-gray-400 hover:text-white transition-colors"
                >
                    <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
                    </svg>
                    Back to Tickets
                </button>

                {isAdmin && (
                    <div className="flex gap-2">
                        {Object.values(TICKET_STATUS).map(status => (
                            <button
                                key={status}
                                onClick={() => handleStatusUpdate(status)}
                                disabled={ticket.ticketStatus === status}
                                className={`px-3 py-1 rounded text-sm font-medium transition-colors ${
                                    ticket.ticketStatus === status
                                        ? 'bg-red-600 text-white cursor-not-allowed'
                                        : 'bg-[#242424] text-gray-400 hover:bg-[#2a2a2a] hover:text-white'
                                }`}
                            >
                                {formatStatusName(status)}
                            </button>
                        ))}
                    </div>
                )}
            </div>

            {/* Ticket Details Card */}
            <div className="bg-[#1a1a1a] border border-red-900/20 rounded-xl p-6">
                <div className="flex items-start justify-between mb-4">
                    <div className="flex-1">
                        <h1 className="text-2xl font-bold text-white mb-2">{ticket.subject}</h1>
                        <div className="flex items-center gap-3 text-sm text-gray-400">
                            <span>Ticket #{ticket.id}</span>
                            <span>•</span>
                            <span>Created {formatDate(ticket.createdAt)}</span>
                            <span>•</span>
                            <span>{ticket.userName}</span>
                        </div>
                    </div>
                    <span className={`px-3 py-1 rounded-full text-sm font-medium border ${getStatusBadgeClasses(ticket.ticketStatus)}`}>
                        {formatStatusName(ticket.ticketStatus)}
                    </span>
                </div>

                <div className="mb-4">
                    <span className="px-3 py-1 bg-[#242424] text-gray-400 text-sm rounded">
                        {formatCategoryName(ticket.ticketCategory)}
                    </span>
                </div>

                {ticket.description && (
                    <div className="mt-4 p-4 bg-[#242424] rounded-lg">
                        <p className="text-gray-300 whitespace-pre-wrap">{ticket.description}</p>
                    </div>
                )}
            </div>

            {/* Responses Section */}
            <div className="bg-[#1a1a1a] border border-red-900/20 rounded-xl p-6">
                <h2 className="text-xl font-bold text-white mb-4">
                    Responses ({responses?.length || 0})
                </h2>

                <div className="space-y-4 mb-6">
                    {responses && responses.length > 0 ? (
                        responses.map(response => (
                            <TicketResponseItem
                                key={response.id}
                                response={response}
                                currentUserId={user?.id}
                                onDelete={handleDeleteResponse}
                            />
                        ))
                    ) : (
                        <p className="text-gray-400 text-center py-8">No responses yet. Be the first to respond!</p>
                    )}
                </div>

                {/* Add Response Form */}
                <form onSubmit={handleSubmitResponse} className="space-y-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-400 mb-2">
                            Add Response
                        </label>
                        <textarea
                            value={responseText}
                            onChange={(e) => setResponseText(e.target.value)}
                            rows={4}
                            className="w-full px-4 py-2 bg-[#242424] border border-red-900/20 text-white rounded-lg focus:outline-none focus:border-red-500 resize-none"
                            placeholder="Type your response here..."
                            disabled={isSubmitting}
                        />
                    </div>

                    {error && (
                        <p className="text-red-400 text-sm">{error}</p>
                    )}

                    <button
                        type="submit"
                        disabled={!responseText.trim() || isSubmitting}
                        className="px-6 py-2 bg-red-600 hover:bg-red-700 disabled:bg-red-600/50 disabled:cursor-not-allowed text-white rounded-lg font-medium transition-colors"
                    >
                        {isSubmitting ? 'Sending...' : 'Send Response'}
                    </button>
                </form>
            </div>
        </div>
    );
}

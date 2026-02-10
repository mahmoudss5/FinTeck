import { useState, useEffect } from 'react';
import { useTicket } from '../../context/TicketContext';
import TicketList from '../../Components/Support/TicketList';
import TicketDetail from '../../Components/Support/TicketDetail';
import TicketForm from '../../Components/Support/TicketForm';

export default function Support() {
    const [view, setView] = useState('list'); // 'list', 'detail', 'create'
    const [selectedTicketId, setSelectedTicketId] = useState(null);
    const {
        tickets,
        selectedTicket,
        ticketResponses,
        loading,
        error,
        fetchUserTickets,
        fetchTicketDetails,
        refreshTickets,
        createNewTicket,
        addResponse,
        clearSelectedTicket
    } = useTicket();

    // Fetch tickets on mount
    useEffect(() => {
        fetchUserTickets();
    }, [fetchUserTickets]);

    const handleTicketSelect = async (ticket) => {
        setSelectedTicketId(ticket.id);
        setView('detail');
        await fetchTicketDetails(ticket.id);
    };

    const handleBackToList = () => {
        setView('list');
        setSelectedTicketId(null);
        clearSelectedTicket();
    };

    const handleCreateTicket = () => {
        setView('create');
    };

    const handleCancelCreate = () => {
        setView('list');
    };

    const handleSubmitTicket = async (ticketData) => {
        try {
            await createNewTicket(ticketData);
            setView('list');
        } catch (err) {
            throw err; // Let form handle the error
        }
    };

    const handleResponseAdded = async (ticketId, responseMessage) => {
        await addResponse(ticketId, responseMessage);
    };

    const handleStatusUpdated = async () => {
        // Refresh the ticket details
        if (selectedTicketId) {
            await fetchTicketDetails(selectedTicketId, true);
        }
    };

    if (loading && tickets.length === 0) {
        return (
            <div className="flex items-center justify-center min-h-[400px]">
                <div className="text-center">
                    <div className="w-12 h-12 border-4 border-red-500 border-t-transparent rounded-full animate-spin mx-auto mb-4"></div>
                    <p className="text-gray-400">Loading support tickets...</p>
                </div>
            </div>
        );
    }

    return (
        <div className="space-y-6">
            {/* Header */}
            <div className="flex items-center justify-between">
                <div>
                    <h1 className="text-2xl font-bold text-white flex items-center gap-3">
                        <span className="w-10 h-10 bg-red-500/20 rounded-lg flex items-center justify-center">
                            <svg className="w-6 h-6 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z" />
                            </svg>
                        </span>
                        Support Center
                    </h1>
                    <p className="text-gray-400">Get help with your account and transactions</p>
                </div>
                {view === 'list' && (
                    <button
                        onClick={() => refreshTickets()}
                        className="px-4 py-2 bg-green-600 border border-green-500/20 text-white hover:bg-green-500/20 rounded-lg transition-colors"
                    >
                        ↻ Refresh
                    </button>
                )}
            </div>

            {/* Error Message */}
            {error && (
                <div className="bg-red-500/10 border border-red-500/20 rounded-xl p-4">
                    <p className="text-red-400">{error}</p>
                </div>
            )}

            {/* Content */}
            {view === 'list' && (
                <TicketList
                    tickets={tickets}
                    onTicketSelect={handleTicketSelect}
                    onCreateTicket={handleCreateTicket}
                />
            )}

            {view === 'detail' && selectedTicket && (
                <TicketDetail
                    ticket={selectedTicket}
                    responses={ticketResponses[selectedTicketId] || []}
                    onBack={handleBackToList}
                    onResponseAdded={handleResponseAdded}
                    onStatusUpdated={handleStatusUpdated}
                />
            )}

            {view === 'create' && (
                <TicketForm
                    onSubmit={handleSubmitTicket}
                    onCancel={handleCancelCreate}
                    loading={loading}
                />
            )}
        </div>
    );
}

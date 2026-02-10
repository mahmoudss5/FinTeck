import { createContext, useContext, useState, useCallback } from 'react';
import { getUserTickets, getTicketById, getTicketResponses, createTicket, createTicketResponse } from '../services/TicketService';
import { useAuth } from './AuthProvider';

const TicketContext = createContext();

export const TicketProvider = ({ children }) => {
    const [tickets, setTickets] = useState([]);
    const [selectedTicket, setSelectedTicket] = useState(null);
    const [ticketResponses, setTicketResponses] = useState({});
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const { user } = useAuth();

    // Fetch user's tickets
    const fetchUserTickets = useCallback(async (forceRefresh = false) => {
        if (!user?.id) return;
        
        // Return cached data if available and not forcing refresh
        if (tickets.length > 0 && !forceRefresh) {
            return tickets;
        }

        try {
            setLoading(true);
            setError(null);
            const data = await getUserTickets(user.id);
            setTickets(data);
            return data;
        } catch (err) {
            setError(err.message);
            console.error("Error fetching tickets:", err);
            return [];
        } finally {
            setLoading(false);
        }
    }, [user?.id, tickets.length]);

    // Fetch ticket details with responses
    const fetchTicketDetails = useCallback(async (ticketId, forceRefresh = false) => {
        // Return cached data if available and not forcing refresh
        if (selectedTicket?.id === ticketId && ticketResponses[ticketId] && !forceRefresh) {
            return { ticket: selectedTicket, responses: ticketResponses[ticketId] };
        }

        try {
            setLoading(true);
            setError(null);
            const [ticketData, responsesData] = await Promise.all([
                getTicketById(ticketId),
                getTicketResponses(ticketId)
            ]);
            setSelectedTicket(ticketData);
            setTicketResponses(prev => ({
                ...prev,
                [ticketId]: responsesData
            }));
            return { ticket: ticketData, responses: responsesData };
        } catch (err) {
            setError(err.message);
            console.error("Error fetching ticket details:", err);
            return null;
        } finally {
            setLoading(false);
        }
    }, [selectedTicket, ticketResponses]);

    // Refresh tickets (force fetch)
    const refreshTickets = useCallback(async () => {
        return await fetchUserTickets(true);
    }, [fetchUserTickets]);

    // Create new ticket
    const createNewTicket = useCallback(async (ticketData) => {
        try {
            setLoading(true);
            setError(null);
            const newTicket = await createTicket(ticketData);
            setTickets(prev => [newTicket, ...prev]);
            return newTicket;
        } catch (err) {
            setError(err.message);
            console.error("Error creating ticket:", err);
            throw err;
        } finally {
            setLoading(false);
        }
    }, []);

    // Add response to ticket
    const addResponse = useCallback(async (ticketId, responseMessage) => {
        try {
            setLoading(true);
            setError(null);
            const responseData = {
                ticketId,
                responseMessage
            };
            const newResponse = await createTicketResponse(responseData);
            setTicketResponses(prev => ({
                ...prev,
                [ticketId]: [...(prev[ticketId] || []), newResponse]
            }));
            return newResponse;
        } catch (err) {
            setError(err.message);
            console.error("Error adding response:", err);
            throw err;
        } finally {
            setLoading(false);
        }
    }, []);

    // Clear selected ticket
    const clearSelectedTicket = useCallback(() => {
        setSelectedTicket(null);
    }, []);

    const value = {
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
    };

    return (
        <TicketContext.Provider value={value}>
            {children}
        </TicketContext.Provider>
    );
};

export const useTicket = () => {
    const context = useContext(TicketContext);
    if (!context) {
        throw new Error('useTicket must be used within a TicketProvider');
    }
    return context;
};

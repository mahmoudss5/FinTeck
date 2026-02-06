import { API_BASE_URL, getHeaders } from "./config";

// Ticket APIs
export const getAllTickets = async () => {
    const response = await fetch(`${API_BASE_URL}/support-tickets/api`, {
        method: "GET",
        headers: getHeaders(),
    });
    if (!response.ok) {
        throw new Error("Failed to fetch tickets");
    }
    return await response.json();
};

export const getTicketById = async (ticketId) => {
    const response = await fetch(`${API_BASE_URL}/support-tickets/api/${ticketId}`, {
        method: "GET",
        headers: getHeaders(),
    });
    if (!response.ok) {
        throw new Error("Failed to fetch ticket details");
    }
    return await response.json();
};

export const getUserTickets = async (userId) => {
    const response = await fetch(`${API_BASE_URL}/support-tickets/api/user/${userId}`, {
        method: "GET",
        headers: getHeaders(),
    });
    if (!response.ok) {
        throw new Error("Failed to fetch user tickets");
    }
    return await response.json();
};

export const getTicketsByStatus = async (status) => {
    const response = await fetch(`${API_BASE_URL}/support-tickets/api/status/${status}`, {
        method: "GET",
        headers: getHeaders(),
    });
    if (!response.ok) {
        throw new Error("Failed to fetch tickets by status");
    }
    return await response.json();
};

export const getTicketsByCategory = async (category) => {
    const response = await fetch(`${API_BASE_URL}/support-tickets/api/category/${category}`, {
        method: "GET",
        headers: getHeaders(),
    });
    if (!response.ok) {
        throw new Error("Failed to fetch tickets by category");
    }
    return await response.json();
};

export const createTicket = async (ticketData) => {
    const response = await fetch(`${API_BASE_URL}/support-tickets/api`, {
        method: "POST",
        headers: getHeaders(),
        body: JSON.stringify(ticketData),
    });
    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.error || "Failed to create ticket");
    }
    return await response.json();
};

export const updateTicketStatus = async (ticketId, status) => {
    const response = await fetch(`${API_BASE_URL}/support-tickets/api/${ticketId}/status?status=${status}`, {
        method: "PATCH",
        headers: getHeaders(),
    });
    if (!response.ok) {
        throw new Error("Failed to update ticket status");
    }
    return await response.json();
};

export const deleteTicket = async (ticketId) => {
    const response = await fetch(`${API_BASE_URL}/support-tickets/api/${ticketId}`, {
        method: "DELETE",
        headers: getHeaders(),
    });
    if (!response.ok) {
        throw new Error("Failed to delete ticket");
    }
};

// Ticket Response APIs
export const getTicketResponses = async (ticketId) => {
    const response = await fetch(`${API_BASE_URL}/support-ticket-responses/api/ticket/${ticketId}`, {
        method: "GET",
        headers: getHeaders(),
    });
    if (!response.ok) {
        throw new Error("Failed to fetch ticket responses");
    }
    return await response.json();
};

export const createTicketResponse = async (responseData) => {
    const response = await fetch(`${API_BASE_URL}/support-ticket-responses/api`, {
        method: "POST",
        headers: getHeaders(),
        body: JSON.stringify(responseData),
    });
    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.error || "Failed to create response");
    }
    return await response.json();
};

export const deleteTicketResponse = async (responseId) => {
    const response = await fetch(`${API_BASE_URL}/support-ticket-responses/api/${responseId}`, {
        method: "DELETE",
        headers: getHeaders(),
    });
    if (!response.ok) {
        throw new Error("Failed to delete response");
    }
};

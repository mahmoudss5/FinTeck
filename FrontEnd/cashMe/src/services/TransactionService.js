import { API_BASE_URL, getHeaders } from "./config.js";

export const getAllTransactions = async () => {
    const response = await fetch(`${API_BASE_URL}/transactions/api/all`, {
        method: "GET",
        headers: getHeaders(),
    });

    if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || `HTTP error! status: ${response.status}`);
    }

    const result = await response.json();
    return result;
};

// need to edited the api link this is fake one until the backend is ready
export const getAllUserTransactions = async () => {
    const response = await fetch(`${API_BASE_URL}/transactions/api/user-All-transactions`, {
        method: "GET",
        headers: getHeaders(),
    });

    if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || `HTTP error! status: ${response.status}`);
    }

    const result = await response.json();
    return result;
};

export const getWalletTransactions = async (walletId) => {
    const response = await fetch(`${API_BASE_URL}/transactions/api/wallet/${walletId}`, {
        method: "GET",
        headers: getHeaders(),
    });

    if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || `HTTP error! status: ${response.status}`);
    }

    const result = await response.json();
    return result;
};

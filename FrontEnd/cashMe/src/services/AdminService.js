import { API_BASE_URL, getHeaders } from "./config";

export const getAllUsers = async () => {
    const response = await fetch(`${API_BASE_URL}/user/api/all`, {
        method: "GET",
        headers: getHeaders(),
    });
    if (!response.ok) {
        throw new Error("Failed to fetch users");
    }
    return await response.json();
};
export const getUserDetails = async (userId) => {
    const response = await fetch(`${API_BASE_URL}/user/api/${userId}`, {
        method: "GET",
        headers: getHeaders(),
    });
    if (!response.ok) {
        throw new Error("Failed to fetch user details");
    }
    return await response.json();
};

export const deleteUser = async (userId) => {
    const response = await fetch(`${API_BASE_URL}/user/api/${userId}`, {
        method: "DELETE",
        headers: getHeaders(),
    });
    if (!response.ok) {
        throw new Error("Failed to delete user");
    }
    return await response.json();
};

export const updateWalletStatus = async (walletId, status) => {
    const response = await fetch(`${API_BASE_URL}/wallets/api/${walletId}`, {
        method: "PUT",
        headers: getHeaders(),
        body: JSON.stringify({ status }),
    });
    if (!response.ok) {
        throw new Error("Failed to update wallet status");
    }
    return await response.json();
};

export const getAllWallets = async () => {
    const response = await fetch(`${API_BASE_URL}/wallets/api/all`, {
        method: "GET",
        headers: getHeaders(),
    });
    if (!response.ok) {
        throw new Error("Failed to fetch wallets");
    }
    return await response.json();
};
export const getWalletDetails = async (walletId) => {
    const response = await fetch(`${API_BASE_URL}/wallets/api/${walletId}`, {
        method: "GET",
        headers: getHeaders(),
    });
    if (!response.ok) {
        throw new Error("Failed to fetch wallet details");
    }
    return await response.json();
};


export const deleteWallet = async (walletId) => {
    const response = await fetch(`${API_BASE_URL}/wallets/api/${walletId}`, {
        method: "DELETE",
        headers: getHeaders(),
    });
    if (!response.ok) {
        throw new Error("Failed to delete wallet");
    }
    return await response.json();
};

export const updateLoanStatus = async (loanId, status) => {
    const response = await fetch(`${API_BASE_URL}/loans/api/${loanId}`, {
        method: "PUT",
        headers: getHeaders(),
        body: JSON.stringify({ status }),
    });
    if (!response.ok) {
        throw new Error("Failed to update loan status");
    }
    return await response.json();
};

export const getAllLoans = async () => {
    const response = await fetch(`${API_BASE_URL}/loan-applications/api/all`, {
        method: "GET",
        headers: getHeaders(),
    });
    if (!response.ok) {
        throw new Error("Failed to fetch loans");
    }
    return await response.json();
};
export const getLoanDetails = async (loanId) => {
    const response = await fetch(`${API_BASE_URL}/loans/api/${loanId}`, {
        method: "GET",
        headers: getHeaders(),
    });
    if (!response.ok) {
        throw new Error("Failed to fetch loan details");
    }
    return await response.json();
};

export const deleteLoan = async (loanId) => {
    const response = await fetch(`${API_BASE_URL}/loans/api/${loanId}`, {
        method: "DELETE",
        headers: getHeaders(),
    });
    if (!response.ok) {
        throw new Error("Failed to delete loan");
    }
    return await response.json();
};

export const promoteUserToAdmin = async (userId) => {
    const response = await fetch(`${API_BASE_URL}/user/api/promote/${userId}`, {
        method: "PUT",
        headers: getHeaders(),
    });
    if (!response.ok) {
        throw new Error("Failed to promote user to admin");
    }
    return await response.json();
};

export const demoteAdmin = async (userId) => {
    const response = await fetch(`${API_BASE_URL}/user/api/demote/${userId}`, {
        method: "PUT",
        headers: getHeaders(),
    });
    if (!response.ok) {
        throw new Error("Failed to demote admin");
    }
    return await response.json();
};

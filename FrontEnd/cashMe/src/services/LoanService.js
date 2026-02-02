import { API_BASE_URL, getHeaders } from "./config.jsx";

export const getUserLoans = async (userId) => {
    const response = await fetch(`${API_BASE_URL}/loan-applications/api/user/${userId}`, {
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

export const applyForLoan = async (data) => {
    const response = await fetch(`${API_BASE_URL}/loan-applications/api`, {
        method: "POST",
        headers: getHeaders(),
        body: JSON.stringify(data),
    });

    if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || `HTTP error! status: ${response.status}`);
    }

    const result = await response.json();
    return result;
};

export const getLoanById = async (id) => {
    const response = await fetch(`${API_BASE_URL}/loan-applications/api/${id}`, {
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

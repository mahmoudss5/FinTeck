import { API_BASE_URL, getHeaders } from "./config.jsx";

export const getUserDetails = async () => {
    const response = await fetch(`${API_BASE_URL}/user/api/me`, {
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

export const getUserById = async (userId) => {
    const response = await fetch(`${API_BASE_URL}/user/api/${userId}`, {
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

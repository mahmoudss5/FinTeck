import { getStoredToken as getToken } from "./authService";

export const API_BASE_URL = 'http://localhost:8080';

export const getHeaders = () => {

  const token = getToken();

  const headers = {
    'Content-Type': 'application/json',
  };

  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  return headers;
}


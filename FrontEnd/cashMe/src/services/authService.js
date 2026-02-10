import { API_BASE_URL, getHeaders } from "./config";
import { useAuth } from "../context/AuthProvider";

// ============================================
// AUTH TOKEN MANAGEMENT
// ============================================

const TOKEN_KEY = "authToken";

/**
 * Check if user has admin role
 * @param {Object} user - User object with roles property
 * @returns {boolean}
 */

export const isAdmin = (user) => {
  if (!user || !user.roles) return false;
  return user.roles.includes("Admin");
}

export const getStoredToken = () => localStorage.getItem(TOKEN_KEY);

export const setStoredToken = (token) => localStorage.setItem(TOKEN_KEY, token);

export const removeStoredToken = () => localStorage.removeItem(TOKEN_KEY);

export const isAuthenticated = () => !!getStoredToken();


// ============================================
// API CALLS
// ============================================

/**
 * Register a new user
 * @param {Object} registerData - { firstName, lastName, email, password }
 * @returns {Promise<Object>} - { token, user }
 */


export const register = async (registerData) => {
  const response = await fetch(`${API_BASE_URL}/api/v1/auth/register`, {
    method: "POST",
    headers: getHeaders(),
    body: JSON.stringify(registerData),
  });

  const result = await response.json();

  if (!response.ok) {
    throw new Error(result.message || "Registration failed");
  }

  if (!result.token) {
    throw new Error("No token received");
  }

  setStoredToken(result.token);
  return result;
};

/**
 * Login user with email and password
 * @param {string} email 
 * @param {string} password 
 * @returns {Promise<Object>} - { token, user }
 */

export const login = async (email, password) => {
  const response = await fetch(`${API_BASE_URL}/api/v1/auth/login`, {
    method: "POST",
    headers: getHeaders(),
    body: JSON.stringify({ email, password }),
  });

  const result = await response.json();

  if (!response.ok) {
    throw new Error(result.message || "Login failed");
  }

  if (!result.token) {
    throw new Error("No token received");
  }

  setStoredToken(result.token);
  return result;
};

/**
 * Fetch current user profile
 * @returns {Promise<Object>} - User data
 */
export const fetchProfile = async () => {
  const response = await fetch(`${API_BASE_URL}/user/api/me`, {
    method: "GET",
    headers: getHeaders(),
  });

  if (!response.ok) {
    throw new Error("Failed to fetch user info");
  }


  return await response.json();
};

/**
 * Logout - clear all auth data
 */
export const logout = () => {
  removeStoredToken();
};

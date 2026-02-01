import { createContext, useContext, useEffect, useState } from "react";
import {
    login as loginApi,
    logout as logoutApi,
    fetchProfile as fetchProfileApi
} from "./authService";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        checkUserLoggedIn();
    }, []);

    const checkUserLoggedIn = async () => {
        const token = localStorage.getItem("authToken");
        if (token) {
            try {
                const userData = await fetchProfileApi();
                setUser(userData);
            } catch (error) {
                console.error("Session expired", error);
                logoutApi();
                setUser(null);
            }
        }
        setLoading(false);
    };

    const login = async (email, password) => {
        const result = await loginApi(email, password);
        const userData = await fetchProfileApi();
        setUser(userData);
        return result;
    };

    const logout = () => {
        logoutApi();
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{ user, login, logout, loading }}>
            {!loading && children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    return useContext(AuthContext);
};

export default AuthProvider;
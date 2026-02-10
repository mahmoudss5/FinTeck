import { createContext, useContext, useEffect, useState } from "react";

import {
    login as loginApi,
    logout as logoutApi,
    fetchProfile as fetchProfileApi,
    isAdmin as isAdminApi,
    register as registerApi
} from "../services/authService";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [isAdmin, setIsAdmin] = useState(false);
    useEffect(() => {
        checkUserLoggedIn();
    }, []);

    const checkUserLoggedIn = async () => {
        const token = localStorage.getItem("authToken");
        if (token) {
            try {
                const userData = await fetchProfileApi();
                setUser(userData);
                setIsAdmin(isAdminApi(userData));
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
        setIsAdmin(isAdminApi(userData));
        return result;
    };

    const logout = () => {
        logoutApi();
        setUser(null);
    };

    const Register = async (registerData) => {
        const result = await registerApi(registerData);
        const userData = await fetchProfileApi();
        setUser(userData);
        setIsAdmin(isAdminApi(userData));
        return result;
    };

    function setUserData(userData) {
        setUser(userData);
        setIsAdmin(isAdminApi(userData));
    }

    async function loginWithToken(token) {
        localStorage.setItem("authToken", token);
        const userData = await fetchProfileApi();
        setUser(userData);
        setIsAdmin(isAdminApi(userData));
    }

    return (
        <AuthContext.Provider value={{loginWithToken,
         user,setUserData, 
         login, logout,
          loading, isAdmin,
          Register}}>

            {!loading && children}
        
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    return useContext(AuthContext);
};

export default AuthProvider;

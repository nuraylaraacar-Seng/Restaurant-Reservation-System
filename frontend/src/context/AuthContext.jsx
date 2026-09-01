import { createContext, useContext, useState, useEffect } from "react";

const AuthContext = createContext(null);

const STORAGE_KEY = "reservation_auth";

export function AuthProvider({ children }) {
    const [auth, setAuth] = useState(() => {
        const stored = localStorage.getItem(STORAGE_KEY);
        return stored ? JSON.parse(stored) : null;
    });

    useEffect(() => {
        if (auth) {
            localStorage.setItem(STORAGE_KEY, JSON.stringify(auth));
        } else {
            localStorage.removeItem(STORAGE_KEY);
        }
    }, [auth]);

    function login(authResponse) {
        // Backend AuthResponse: { accessToken, tokenType, userId, email, role }
        setAuth(authResponse);
    }

    function logout() {
        setAuth(null);
    }

    const value = {
        token: auth?.accessToken ?? null,
        email: auth?.email ?? null,
        role: auth?.role ?? null,
        isAuthenticated: !!auth,
        login,
        logout,
    };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useAuth, AuthProvider içinde kullanılmalı.");
    }
    return context;
}

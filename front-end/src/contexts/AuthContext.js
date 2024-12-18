import React, { createContext, useState, useEffect, useMemo } from "react";
import PropTypes from "prop-types";
import axios from "axios"

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem("token"));
  const [name, setName] = useState(null);
  const [email, setEmail] = useState(null);
  const [token, setToken] = useState(localStorage.getItem("token"));

  // Optional: load initial login state from local storage or a cookie
  useEffect(() => {
    if (token) {
      fetchUserDetails(token); // Load user details if already logged in
    }
  }, [token]);

  const fetchUserDetails = async (authToken) => {
    try {
      const headers = {
        Authorization: `Bearer ${authToken || token}`,  
      };

      const [nameResponse, emailResponse] = await Promise.all([
        axios.get("http://localhost:8080/api/user/name", { headers }),
        axios.get("http://localhost:8080/api/user/email", { headers }),
      ]);

      setName(nameResponse.data)
      setEmail(emailResponse.data)
      //setRooms(roomsResponse.data)
    } catch (error) {
      console.error("Failed to fetch user details:", error);
      logout();
    }
  };


  const login = (newToken) => {
    setToken(newToken);
    setIsLoggedIn(true);
    localStorage.setItem("token", newToken);
    fetchUserDetails(newToken);
  };

  const logout = () => {
    setIsLoggedIn(false);
    setName(null);
    setToken(null);
    localStorage.removeItem("token");
  };

  // Memoize the value to prevent unnecessary re-renders
  const value = useMemo(() => ({ isLoggedIn, name, email, token, login, logout }), [isLoggedIn, name, email, token]);

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

// Add PropTypes validation for children
AuthProvider.propTypes = {
    children: PropTypes.node.isRequired,
};
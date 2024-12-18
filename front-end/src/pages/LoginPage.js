import { React, useState, useContext } from "react";
import { Link, useNavigate } from "react-router-dom";
import "../assets/stylesheets/LoginPage.css"
import BannerMenu from "../components/BannerMenu";
import "../assets/stylesheets/GeneralLayout.css"
import axios from "axios";
import { AuthContext } from "../contexts/AuthContext";

export const loginUser = async (email, password, login, navigate, setErrorMessage) => {
    try {
        // Prepare user data to send to the backend
        const userData = {
            "fname": null,
            "lname": null,
            "email": email,
            "password": password,
            "rooms": [],
            "wishlist": [],
        };
        // Send POST request to backend
        const response = await axios.post('http://localhost:8080/api/login', userData);
        
        if (response.status === 200) {
            console.log('User logged in successfully:', response.data);

            // Extract and store the JWT token
            const token = response.data;

            login(token);

            navigate("/MyRooms"); // Redirect to rooms page
        }
    } catch (error) {
        if (error.response) {
            console.error("Error response:", error.response.data);
            const parsedMessage = error.response.data.split('"')[1] || error.response.data.split(" ").slice(-1)[0];
            setErrorMessage(parsedMessage);
        } else if (error.request) {
            // The request was made but no response was received
            console.error("Error request:", error.request);
            setErrorMessage("Login failed: No response from server.");
        } else {
            // Something happened in setting up the request
            console.error("Error message:", error.message);
            setErrorMessage(error.message);
        }
    }
};

function LoginPage() {
    
    const navigate = useNavigate();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState(''); 
    const { login } = useContext(AuthContext);

    const handleSubmit = (event) => {
        event.preventDefault();
        setErrorMessage(''); // Clear any previous error messages
        // Call backend API here
        loginUser(email, password, login, navigate, setErrorMessage);
    };
    
    return (
        <div className="container">
            <BannerMenu/>
            <div className="login-form">
                <div>
                    <form onSubmit={handleSubmit}>
                        <div>
                            <h1>Log In:</h1>
                            <label htmlFor="email">Email: </label>
                            <input
                                type="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                            />
                        </div>
                        <div>
                            <label htmlFor="password">Password: </label>
                            <input 
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </div>
                        <button className="main-login-button" type="submit">Log In</button>
                    
                        <h2>Don't have an account?</h2>
                        <Link to="/Signup">
                            <button className="signup-button">Sign Up!</button>
                        </Link>
                    </form>
                    {errorMessage && <p style={{ textAlign:"center", color: 'red' }}>{errorMessage}</p>}
                </div>
            </div>
        </div>
    )
}
export default LoginPage;
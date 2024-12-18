import { React, useState, useContext} from "react";
import { Link, useNavigate } from "react-router-dom";
import "../assets/stylesheets/LoginPage.css"
import BannerMenu from "../components/BannerMenu";
import axios from "axios";
import { AuthContext } from "../contexts/AuthContext";
import { loginUser } from "./LoginPage";

function SignupPage() {
    
    const navigate = useNavigate();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [fname, setFname] = useState("");
    const [lname, setLname] = useState("");
    const { login } = useContext(AuthContext);
    const [errorMessage, setErrorMessage] = useState('');

    const handleSubmit = (event) => {
        event.preventDefault();
        // Call backend API here
        registerUser();
    };
    
    const registerUser = async () => {
        try {
            // Prepare user data to send to the backend
            const userData = {
                "fname": fname,
                "lname": lname,
                "email": email,
                "password": password,
                "rooms": [],
                "wishlist": [],
            };
            console.log(userData)
            // Send POST request to backend
            const response = await axios.post('http://localhost:8080/api/register', userData);
            
            if (response.status === 200) {
                console.log('User registered successfully:', response.data);
                loginUser(email, password, login, navigate, setErrorMessage)
                navigate("/MyRooms"); // Redirect to rooms page
            }
        } catch (error) {
            if (error.response) {
                // The request was made and the server responded with a status code
                console.error("Error response:", error.response.data);
                setErrorMessage(`Registration failed: ${error.response.data.message || "Unknown error occurred."}`);
            } else if (error.request) {
                // The request was made but no response was received
                console.error("Error request:", error.request);
                setErrorMessage("Registration failed: No response from server.");
            } else {
                // Something happened in setting up the request
                console.error("Error message:", error.message);
                setErrorMessage(`Registration failed: ${error.message}`);
            }
        }
      };
    
    return (
        <div className="container">
            <BannerMenu/>
            <div className="login-form">
                <div>
                    <form onSubmit={handleSubmit}>
                        <div>
                            <h1>Create an Account:</h1>
                            <label htmlFor="fname">First Name: </label>
                            <input 
                                type="text"
                                value={fname}
                                onChange={(e) => setFname(e.target.value)}
                                required
                            />
                        </div>
                        <div>
                            <label htmlFor="lname">Last Name: </label>
                            <input 
                                type="text"
                                value={lname}
                                onChange={(e) => setLname(e.target.value)}
                                required
                            />
                        </div>
                        <div>
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
                        <button className="main-login-button" type="submit">Sign Up</button>
                        <h2>Already have an account?</h2>
                        <Link to="/Login">
                            <button className="signup-button">Log In</button>
                        </Link>
                    </form>
                    {errorMessage && <p style={{ textAlign:"center", color: 'red' }}>{errorMessage}</p>}
                </div>
            </div>
        </div>
    )
}
export default SignupPage;
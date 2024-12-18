import { React, useContext } from "react";
import { useNavigate } from "react-router-dom";
import BannerMenu from "../components/BannerMenu";
import "../assets/stylesheets/MyAccount.css"
import { AuthContext } from "../contexts/AuthContext";

function MyAccountPage() {
    
    const navigate = useNavigate();
    const { name, email, logout } = useContext(AuthContext);

    const handleClick = (event) => {
        event.preventDefault();
        // Call backend API here
        logout();
        console.log("The user has logged out")
        navigate("/Login"); // Redirect to login page
    };
    
    //We are gonna have blank login credentials until we set up the backend
    return (
        <div className="container">
            <BannerMenu/>
            <div className="my-account-page">
                <h1>My Account</h1>
                <div>
                    <h2>Pending Login Requests: 0</h2>
                </div>
                <div className="account-info">
                    <div className="account-details">
                        <h2>Account Details</h2>
                        <p><strong>Name:</strong> {name}</p>
                        <p><strong>Email:</strong> {email}</p>
                    </div>
                    <div className="account-settings">
                        <h2>Settings</h2>
                        <button className="settings-btn">Edit Profile</button>
                        <button className="settings-btn">Change Password</button>
                    </div>
                </div>
                <div className="logout-section">
                    <button className="logout-btn" onClick={handleClick}>Log Out</button>
                </div>
            </div>
        </div>
    )
}
export default MyAccountPage;
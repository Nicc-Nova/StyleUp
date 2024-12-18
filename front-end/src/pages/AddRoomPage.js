import React, { useContext, useEffect, useState } from 'react';
import Sidebar from '../components/Sidebar';
import BannerMenu from '../components/BannerMenu';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../contexts/AuthContext';
import "../assets/stylesheets/LoginPage.css"
import axios from 'axios';
import { createRoom } from "../utils/RoomUtils";

function AddRoomPage() {
  const navigate = useNavigate();
  const { isLoggedIn, token } = useContext(AuthContext);
  const [roomName, setRoomName] = useState("");
  const [errorMessage, setErrorMessage] = useState('');

  useEffect(() => {
    if (!isLoggedIn) {
      navigate("/Login"); // Redirect to Login if not logged in
    }
  }, [isLoggedIn, navigate]); // Depend on isLoggedIn to trigger re-navigation

  const handleSubmit = (event) => {
    event.preventDefault();
    createRoom(
      roomName,
      token,
      () => navigate("/MyRooms"),
      setErrorMessage
    );
  };

  return (
    <div>
      <div>
        <BannerMenu/>
        <Sidebar/>
        <h1 style={{ textAlign: 'center' }}>Add a Room</h1>
      </div>
      {isLoggedIn ? (
        <div className="login-form">
          <div>
              <form onSubmit={handleSubmit}>
                  <div>
                      <h1>Create a Room:</h1>
                      <label id="room-name" >
                         Room Name: 
                      <input 
                          type="text"
                          value={roomName}
                          onChange={(e) => setRoomName(e.target.value)}
                          required
                      />
                      </label>
                  </div>
                  <button className="main-login-button" type="submit">Create Room</button>
              </form>
              {errorMessage && <p style={{ textAlign:"center", color: 'red' }}>{errorMessage}</p>}
          </div>
      </div>
      ):(
        <p style={{ textAlign: 'center' }}>Redirecting to login...</p>
      )}
    </div>
  );
}

export default AddRoomPage;
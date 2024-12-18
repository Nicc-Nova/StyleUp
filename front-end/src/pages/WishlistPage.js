import React, { useContext, useEffect, useState } from 'react';
import Sidebar from '../components/Sidebar.js';
import BannerMenu from '../components/BannerMenu.js';
import { AuthContext } from '../contexts/AuthContext.js';
import { useNavigate } from 'react-router-dom';
import "../assets/stylesheets/RoomPage.css"
import axios from 'axios';
import { createRoom } from "../utils/RoomUtils";

function WishlistPage() {
  const navigate = useNavigate();
  const { isLoggedIn, token } = useContext(AuthContext);
  const [menuOpenStates, setMenuOpenStates] = useState({});
  const [wishlist, setWishlist] = useState([]);
  const [loading, setLoading] = useState(true)
  const [errorMessage, setErrorMessage] = useState('')
  const [rooms, setRooms] = useState([])
  const [collabs, setCollabs] = useState([])
  const [messages, setMessages] = useState({});

  useEffect(() => {
    if (!isLoggedIn) {
      navigate("/Login"); // Redirect to Login if not logged in
    } else {
      getWishlist();
      getRooms();
      getCollabs();
    }
  }, [isLoggedIn, navigate]);

  const getWishlist = async () => {
    try{
      setLoading(true)
      const headers = {
        Authorization: `Bearer ${token}`,
      };
      const response = await axios.get("http://localhost:8080/api/user/wishlist", {headers})
      console.log("Wishlist: ", response.data)
      setWishlist(response.data)
    }catch (error) {
      console.error("Failed to fetch wishlist:", error);
      setErrorMessage(error);
    } finally {
      setLoading(false)
    }
  }

  const getRooms = async () => {
    try {

      const headers = {
        Authorization: `Bearer ${token}`,
      };

      const roomsResponse = await axios.get("http://localhost:8080/api/user/rooms", {headers})

      console.log("Rooms data:", roomsResponse.data); // Check rooms 
      setRooms(roomsResponse.data)

    } catch (error) {
      console.error("Failed to fetch user details:", error);
    }     
  }

  const getCollabs = async () => {
    try {
      const headers = {
        Authorization: `Bearer ${token}`,
      };
      const collabsResponse = await axios.get("http://localhost:8080/api/user/collabRooms", {headers})
      console.log("Collabs data:", collabsResponse.data); // Check rooms 
      setCollabs(collabsResponse.data)
    } catch (error) {
      console.error("Failed to fetch collab details:", error);
    }
  }

  const toggleMenu = (decorationId) => {
    setMenuOpenStates((prevState) => ({
      ...prevState,
      [decorationId]: !prevState[decorationId],
    }));
  };

  const addDecoration = async (item, room_id, index, image_url) => {
    try {
        // Prepare user data to send to the backend
        const headers = {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
        };
        console.log(item.htmlSnippet);

        const decoration={
          "searchLink": item.searchLink,
          "fkr": room_id,
          "wishId": null,
          "description": new DOMParser().parseFromString(item.htmlSnippet, 'text/html').body.textContent.trim() || "No description available.",
          "title": item.title,
          "image": image_url,
        }

        console.log("Create Decoration:", decoration);
        // Send POST request to backend
        const response = await axios.post('http://localhost:8080/api/decoration/create', decoration , {headers});
        
        if (response.status === 200) {
            console.log('Room Created successfully:', response.data);
            setMessages((prevMessages) => ({
              ...prevMessages,
              [index]: "Added to Room!", // Set message for the specific index
            }));
        }
      } catch (error) {
        setMessages((prevMessages) => ({
          ...prevMessages,
          [index]: "Error adding to room", // Set an error message if needed
      }));
      console.error("Error adding to room: ", error)
    }
  };

  const deleteDecoration = async (decoration) => {
    try {
        // Prepare user data to send to the backend
        const headers = {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
        };
        console.log("Delete Decoration:", decoration);
        // Send POST request to backend
        const response = await axios.post('http://localhost:8080/api/decoration/delete', decoration , {headers});
        
        if (response.status === 200) {
            console.log('Room deleted successfully:', response.data);
            // Update the local state to remove the deleted decoration
            setWishlist((prevList) => ({
                ...prevList,
                wishlist: prevList.filter((dec) => dec.dec_id !== decoration.dec_id),
            }));
        }
    } catch (error) {
        if (error.response) {
            // The request was made and the server responded with a status code
            console.error("Error response:", error.response.data);
            setErrorMessage(`Room deletion failed: ${error.response.data.message || "Unknown error occurred."}`);
        } else if (error.request) {
            // The request was made but no response was received
            console.error("Error request:", error.request);
            setErrorMessage("Room deletion failed: No response from server.");
        } else {
            // Something happened in setting up the request
            console.error("Error message:", error.message);
            setErrorMessage(`Room deletion failed: ${error.message}`);
        }
    }
  };

  const handleCreateRoom = () => {
    const roomName = prompt("Enter room name:");
    if (roomName) {
      createRoom(
        roomName,
        token,
        () => getRooms(),
        (error) => {
          alert(error); // Show error message
        }
      );
    }
  };

  const toggleRoomMenu = (index) => {
    setMenuOpenStates((prevState) => {
      const newStates = Object.keys(prevState).reduce((acc, key) => {
        acc[key] = false; // Close all other menus
        return acc;
      }, {});
      return {
        ...newStates,
        [index]: !prevState[index], // Toggle the clicked menu
      };
    });
  };

  const renderContent = () => {
    if (loading) {
      return <p style={{ textAlign: 'center' }}>Loading wishlist...</p>;
    }
    if (Array.isArray(wishlist) && wishlist.length > 0) {
      return (
        <div className="results-grid">
          {wishlist.map((decoration, index) => (
            <div key={`decoration-${decoration.dec_id}`} className="result-card">
                <div className='room-header'>
                    <h2>{decoration.title}</h2>
                    <div className="dropdown">
                        <button
                            className="menu-button"
                            onClick={() => toggleMenu(decoration.dec_id)}
                        >
                            ⋮
                        </button>
                        {menuOpenStates[decoration.dec_id] && (
                            <div className="dropdown-menu">
                                <button
                                    onClick={() => deleteDecoration(decoration)}
                                    className="delete-button"
                                >
                                    Delete Decoration
                                </button>
                            </div>
                        )}
                    </div>
                </div>
                {decoration.image && <img src={decoration.image} alt={decoration.title} className="result-image" />}
                <div className='description'> 
                    <h2>Description:</h2>
                    <p>{decoration.description}</p>
                </div>
                <div className='description'>
                    <h2>Link:</h2>
                    <a
                        href={decoration.searchLink}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="result-card-link"
                    >
                        {decoration.searchLink}
                    </a>
                </div>
                {isLoggedIn ? (
                <div className="dropdown">
                  <button className="add-button" onClick={() => toggleRoomMenu(index)}>
                    +
                  </button>
                  {menuOpenStates[index] && (
                    <div className="dropdown-menu">
                      {rooms.length === 0 ? (
                        <div>
                          <button className="room-button" onClick={handleCreateRoom}>Create a Room</button>
                        </div>
                      ) : (
                      rooms.map((room) => (
                        <button key={room.room_id} className="room-button" 
                          onClick={() => addDecoration(decoration, room.room_id, index, decoration.image)}  
                        >
                          {room.roomName}
                        </button>
                      ))
                    )}
                    {!Array.isArray(collabs) || collabs.length === 0 ? (
                      <div className="shared-rooms">
                        <p style={{ textAlign: "center"}}>No shared rooms</p>
                      </div>
                    ) : (
                      collabs.map((collab) => (
                        <button key={collab.room_id} className="room-button" 
                          onClick={() => addDecoration(decoration, collab.room_id, index, decoration.image)}  
                        >
                          {collab.roomName}
                        </button>
                      ))
                    )}
                    </div>
                  )}
                  {messages[index] && (
                    <div className="message-container">
                      <p style={{ color: "green", marginTop: "10px", textAlign: "center" }}>
                        {messages[index]}
                      </p>
                    </div>
                  )}
                </div>
              ) : (
                <p className="login-prompt">Log in to add this to a room.</p>
              )}
            </div>
          ))}
          {errorMessage && <p style={{ textAlign: "center", color: 'red' }}>{errorMessage}</p>}
        </div>
      );
    } 
        
    return (
        <div className="no-decorations-message">
          <p>You don't have any decorations saved here yet...</p>
        </div>
    );
  };

  return (
    <div className='container'>
      <BannerMenu />
      <Sidebar />
      {isLoggedIn ? (
        <div className='main-content'>
          <h1>Wishlist</h1>
          {renderContent()}
        </div>
      ) : (
        <p style={{ textAlign: 'center' }}>Redirecting to login...</p>
      )}
    </div>
  );
}

export default WishlistPage;
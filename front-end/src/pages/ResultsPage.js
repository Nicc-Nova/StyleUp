import React, { useState, useEffect, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { useSurveyContext } from "../contexts/SurveyContext";
import axios from "axios";
import BannerMenu from "../components/BannerMenu"
import { AuthContext } from "../contexts/AuthContext";
import Sidebar from "../components/Sidebar";
import "../assets/stylesheets/ResultsPage.css"
import { createRoom } from "../utils/RoomUtils";

const ResultsPage = () => {
  const navigate = useNavigate();
  const { finalResponse } = useSurveyContext();
  const { token, isLoggedIn } = useContext(AuthContext);
  const [menuOpenStates, setMenuOpenStates] = useState({});
  const [rooms, setRooms] = useState([])
  const [collabs, setCollabs] = useState([])
  const [messages, setMessages] = useState({});

  // Initialize results as null to show loading state initially
  const [results, setResults] = useState(null);

  useEffect(() => {
    fetchResults();
    //If you click outside dropdown menu, close it
    const handleClickOutside = (event) => {
      if (!event.target.closest(".dropdown")) {
        setMenuOpenStates({}); // Close all menus
      }
    };
    document.addEventListener("click", handleClickOutside);
    return () => document.removeEventListener("click", handleClickOutside);

  }, []);

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

  const fetchResults = async () => {
    try {
      const searchString = finalResponse + "%20Decorations"
      console.log(searchString);
      const response = await axios.post(`http://localhost:8080/api/search`, searchString);
      setResults(response.data); // Save the raw JSON object
      await getRooms();
      await getCollabs();
    } catch (error) {
      console.error("Error fetching results: ", error);
    }
  };

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


  const addDecoration = async (item, room_id, index, image_url) => {
    try {
        // Prepare user data to send to the backend
        const headers = {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
        };
        console.log(item.htmlSnippet);

        const decoration={
          "searchLink": item.link,
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

  const wishDecoration = async (item, index, image_url) => {
    try {
        // Prepare user data to send to the backend
        const headers = {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
        };
        console.log(item.htmlSnippet);

        const decoration={
          "searchLink": item.link,
          "fkr": null,
          "wishId": null,
          "description": new DOMParser().parseFromString(item.htmlSnippet, 'text/html').body.textContent.trim() || "No description available.",
          "title": item.title,
          "image": image_url,
        }

        console.log("Create Decoration:", decoration);
        // Send POST request to backend
        const response = await axios.post('http://localhost:8080/api/decoration/wish', decoration , {headers});
        
        if (response.status === 200) {
            console.log('Room wishlisted successfully:', response.data);
            setMessages((prevMessages) => ({
              ...prevMessages,
              [index]: "Added to wishlist!", // Set message for the specific index
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

  // Show loading message while results are being fetched
  if (!results) {
    return <div>Loading...</div>;
  }

  // Extract items array from results
  const items = results.items || [];

  return (
    <div className="results-container">
    <BannerMenu/>
    {isLoggedIn ? <Sidebar/> : <h1>Log In to save results to a room</h1>}
      <div className="results-content">
        <h1>Search Results</h1>
        <div className="results-grid">
          {items.map((item, index) => {
            const imageUrl =
              item.pagemap?.cse_image?.[0]?.src || item.pagemap?.metatags?.[0]?.["og:image"];
            return (
            <div className="result-card" key={index}>
              <h2>{item.title}</h2>
              <a href={item.link} target="_blank" rel="noopener noreferrer">
                {item.link}
              </a> {/* Use item.link */}
              <p dangerouslySetInnerHTML={{ __html: item.htmlSnippet }}></p> {/* Use item.htmlSnippet */}
              {imageUrl && <img src={imageUrl} alt={item.title} className="result-image" />}
              {isLoggedIn ? (
                <div className="dropdown">
                  <button className="add-button" onClick={() => toggleRoomMenu(index)}>
                    +
                  </button>
                  {menuOpenStates[index] && (
                    <div className="dropdown-menu">
                      <button className="room-button" 
                          onClick={() => wishDecoration(item, index, imageUrl)}  
                        >
                          Wishlist
                        </button>
                      {rooms.length === 0 ? (
                        <div>
                          <button className="room-button" onClick={handleCreateRoom}>Create a Room</button>
                        </div>
                      ) : (
                      rooms.map((room) => (
                        <button key={room.room_id} className="room-button" 
                          onClick={() => addDecoration(item, room.room_id, index, imageUrl)}  
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
                          onClick={() => addDecoration(item, collab.room_id, index, imageUrl)}  
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
            );
          })}
        </div>

        {/* Navigation Buttons */}
        <div className="navigation-buttons">
          <button onClick={() => navigate("/Survey")}>Back to Survey</button>
          <button onClick={() => navigate("/MyRooms")}>Go to My Rooms</button>
        </div>
      </div>
    </div>
  );
};
export default ResultsPage;

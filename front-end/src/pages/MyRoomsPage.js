import React, {useState, useEffect, useContext} from 'react';
import BannerMenu from '../components/BannerMenu';
import Sidebar from '../components/Sidebar';
import "../assets/stylesheets/GeneralLayout.css"
import "../assets/stylesheets/MyRoomsPage.css"
import myRoomsImage from "../assets/images/My Rooms.png"
import arrowcurve from "../assets/images/ArrowCurve.png"
import { AuthContext } from '../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function MyRoomsPage() {
  const navigate = useNavigate();
  const [rooms, setRooms] = useState([])
  const [collabs, setCollabs] = useState([])
  const [loading, setLoading] = useState(true); // Loading state to manage async data fetching
  const [loading1, setLoading1] = useState(true);
  const { isLoggedIn, token } = useContext(AuthContext);
  const [errorMessage, setErrorMessage] = useState('');

  useEffect(() => {
    if (!isLoggedIn) {
      navigate("/Login"); // Redirect to Login if not logged in
    } else {
      getRooms();
      getCollabs();
    }
  }, [isLoggedIn, token, navigate]); // Depend on isLoggedIn to trigger re-navigation

  const getRooms = async () => {
    try {
      setLoading(true); // Start loading

      const headers = {
        Authorization: `Bearer ${token}`,
      };

      const roomsResponse = await axios.get("http://localhost:8080/api/user/rooms", {headers})

      console.log("Rooms data:", roomsResponse.data); // Check rooms 
      setRooms(roomsResponse.data)

    } catch (error) {
      console.error("Failed to fetch user details:", error);
      setErrorMessage(error);
    } finally {
      setLoading(false); // Stop loading after fetch is done
    }
  }

  const getCollabs = async () => {
    try {
      setLoading1(true); // Start loading
      const headers = {
        Authorization: `Bearer ${token}`,
      };
      const collabsResponse = await axios.get("http://localhost:8080/api/user/collabRooms", {headers})
      console.log("Collabs data:", collabsResponse.data); // Check rooms 
      setCollabs(collabsResponse.data)
    } catch (error) {
      console.error("Failed to fetch collab details:", error);
      setErrorMessage(error);
    } finally {
      setLoading1(false); // Stop loading after fetch is done
    }
  }

  //navigate to room page and send selected room id and name in params
  const handleRoomClick = (room) => {
    navigate(`/Room?id=${room.room_id}&name=${encodeURIComponent(room.roomName)}`);
  };
  const handleKeyDown = (room) => {
    navigate(`/Room?id=${room.room_id}&name=${encodeURIComponent(room.roomName)}`);
  };

  const handleCollabClick = (collab) => {
    navigate(`/CollabRoom?id=${collab.room_id}&name=${encodeURIComponent(collab.roomName)}`);
  };

  const renderContent = () => {
    if (loading) {
      return <p style={{ textAlign: 'center' }}>Loading rooms...</p>;
    }
    if (rooms.length > 0) {
      return (
        <div className="rooms-list">
          {rooms.map((room) => (
            <div role="button" key={room.room_id} className="room-item" 
              onClick={() => handleRoomClick(room) }
              onKeyDown={() => handleKeyDown(room)}  
              style={{ cursor: 'pointer' }}>
              <h1>{room.roomName}</h1>
            </div>
          ))}
          {errorMessage && <p style={{ textAlign: "center", color: 'red' }}>{errorMessage}</p>}
        </div>
      );
    } 
    // Memoize the value to prevent unnecessary re-renders
    return (
      <>
        <div className="arrow-container">
          <img src={arrowcurve} alt="arrow" height='500px' width='800px' />
        </div>
        <div className="no-room-message">
          <p>You don't have any rooms yet...</p>
        </div>
      </>
    );
  };

  const renderCollabs = () => {
    if (loading1) {
      return <p style={{ textAlign: 'center' }}>Loading rooms...</p>;
    }
    if (Array.isArray(collabs)&&collabs.length > 0) {
      return (
        <div className="rooms-list">
          {collabs.map((collab) => (
            <div key={collab.room_id} className="room-item" 
              onClick={() => handleCollabClick(collab)}
              style={{ cursor: 'pointer' }}>
              <h1>{collab.roomName}</h1>
            </div>
          ))}
          {errorMessage && <p style={{ textAlign: "center", color: 'red' }}>{errorMessage}</p>}
        </div>
      );
    } 
    // Memoize the value to prevent unnecessary re-renders
    return (
      <>
        <div className="no-room-message">
          <p>Nobody has shared a room with you yet...</p>
        </div>
      </>
    );
  };

  return (
    <div>
      <BannerMenu/>
      <Sidebar/>
      {isLoggedIn ? (
        <div className='main-content'>  {/**breaks background for some reason */}
          <div className='page-title'>
            <div className='image-style'>
              <img src={myRoomsImage} alt="My Rooms"/>
            </div>
            <h1>My Rooms</h1>
          </div>  
          {renderContent()}
          <div className='page-title'>
            <div className='image-style'>
              <img src={myRoomsImage} alt="My Rooms"/>
            </div>
            <h1>Shared With Me</h1>
          </div>
          {renderCollabs()}
        </div>
      ):(
        <p style={{ textAlign: 'center' }}>Redirecting to login...</p>
      )}
    </div>
  );
}

export default MyRoomsPage;
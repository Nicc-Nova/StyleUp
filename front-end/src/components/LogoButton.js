import React, { useContext } from 'react';
import logo from '../assets/images/LOGO.png'
import { Link } from "react-router-dom";
import { AuthContext } from '../contexts/AuthContext';

function LogoButton() {
  
  const { isLoggedIn } = useContext(AuthContext);

  return (
    
    <div>
      {isLoggedIn ? (
        // If logged in, navigate to the MyRooms page
        <Link to="/MyRooms"><img src={logo} alt={""} style={{position: 'fixed', top: 0, left: 130, height: '100px', cursor: 'pointer'}}/></Link>
      ) : (
        // If not logged in, navigate to the Start page
        <Link to="/"><img src={logo} alt={""} style={{position: 'fixed', top: 0, left: 130, height: '100px', cursor: 'pointer'}}/></Link>
      )}
    </div>
  )
}

export default LogoButton

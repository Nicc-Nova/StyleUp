import React, {useContext} from 'react';
import LogoButton from './LogoButton.js';
import '../assets/stylesheets/BannerMenu.css';
import { Link } from "react-router-dom";
import { AuthContext } from '../contexts/AuthContext.js';

const BannerMenu = () => {
  const { name, isLoggedIn } = useContext(AuthContext);

  return (
    <div className='banner-container'> 
        {/**Container for the Banner Menu.*/}
        <div>
          {/* Logo Button */}
            <LogoButton />  
        </div>

        <div className='button-container'>
          {/* When User is logged in */}
          {isLoggedIn ? (
            <>
              {/* PFP component PLACEHOLDER*/}
              <p>{name}</p>
              <Link to="/MyAccount">
                <button className='logout-button'>My Account</button>
              </Link> 
            </>
          ):(
            <>
              {/* When User is not Logged in */}
              <Link to="/Login">
                <button className='login-button'>Log In</button>
              </Link>
              <Link to="/Signup">
                <button className="signup-button">Sign Up!</button>
              </Link>
            </>
          )}
        </div>

    </div>
  )
}

export default BannerMenu

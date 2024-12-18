import React from 'react'
import addRoomImage from "../assets/images/Add Room.png"
import myRoomsImage from "../assets/images/My Rooms.png"
import surveyImage from "../assets/images/Survey.png"
import wishlistImage from "../assets/images/Wishlist.png"
import invitationsImage from "../assets/images/Invitations.png"
import "../assets/stylesheets/GeneralLayout.css"
import { Link } from 'react-router-dom';

function Sidebar() {
    return (
        <div className='sidebar'>
            {/*Add Room Button*/}
            <Link to="/addRoom">
                <button id='addRoom' className='sidebar-button'>
                    <img src={addRoomImage} alt="Add Room"/>
                </button>
            </Link>
            {/*My Rooms Button*/}
            <Link to="/MyRooms">
                <button id='myRooms' className='sidebar-button'>
                    <img src={myRoomsImage} alt="My Rooms"/>
                </button>
            </Link>
            {/*Survey Button*/}
            <Link to="/Survey">
                <button id='survey' className="sidebar-button">
                    <img src={surveyImage} alt="Survey"/>
                </button>
            </Link>
            {/*Wishlist Button*/}
            <Link to="/Wishlist">
                <button id='wishlist' className="sidebar-button">
                    <img src={wishlistImage} alt="Wishlist"/>
                </button>
            </Link>
            {/*Invitations Button*/}
            <Link to="/Invitations">
                <button id='invitations' className="sidebar-button">
                    <img src={invitationsImage} alt="Invitations"/>
                </button>
            </Link>
        </div>
    )
}

export default Sidebar;
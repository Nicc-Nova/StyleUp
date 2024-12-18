import React from 'react'
import '../assets/stylesheets/OpeningPage.css';
import BannerMenu from '../components/BannerMenu.js'
import GradientBackground from '../components/GradientBackground'
import { Link } from "react-router-dom";

function OpeningPage() {

  return (
    <div className="all">
        <BannerMenu />
        <div className="gradient">
        <GradientBackground />
        </div>
        <div className="GetStarted">
          Get Your Personalized Recomendations
        </div>
        <div className="GetStarted2">
          <Link to="/Survey">
            <button className="start-button">
              Start
            </button>
          </Link></div>
    </div>
  );
}

export default OpeningPage;

import React, { useState } from 'react';
import PropTypes from 'prop-types';
import '../assets/stylesheets/SurveyPage.css';
import minimalist from "../assets/images/aesthetics/minimalist.jpg"
import cottageCore from "../assets/images/aesthetics/cottageCore.png"
import bohemian from "../assets/images/aesthetics/bohemian.jpg"
import industrial from "../assets/images/aesthetics/industrial.png"
import midCenturyModern from "../assets/images/aesthetics/midCenturyModern.jpg"
import logCabin from "../assets/images/aesthetics/logCabin.jpg"
import beach from "../assets/images/aesthetics/beach.png"
import vintage from "../assets/images/aesthetics/vintage.jpg"
import rustic from "../assets/images/aesthetics/rustic.jpg"
import eclectic from "../assets/images/aesthetics/eclectic.png"
import traditional from "../assets/images/aesthetics/traditional.jpg"
import hollywoodGlam from "../assets/images/aesthetics/hollywoodGlam.jpeg"
import y2k from "../assets/images/aesthetics/y2k.png"
import castle from "../assets/images/aesthetics/castle.jpg"
import urbanModern from "../assets/images/aesthetics/urbanModern.jpeg"
import contemporary from "../assets/images/aesthetics/contemporary.png"
import frenchCountry from "../assets/images/aesthetics/frenchCountry.jpg"
import goth from "../assets/images/aesthetics/goth.png"
import lightAcademia from "../assets/images/aesthetics/lightAcademia.jpg"
import darkAcademia from "../assets/images/aesthetics/darkAcademia.jpeg"

function Question3({ onAnswer }) {
  // State to keep track of selected aesthetics
  const [selectedAesthetics, setSelectedAesthetics] = useState([]);

  // List of aesthetics for the user to choose from
  const aesthetics = [
    "Minimalist", "Cottage Core", "Bohemian", "Industrial",
    "Log Cabin", "Mid-Century Modern", "Beach", "Vintage",
    "Rustic", "Eclectic", "Traditional", "Hollywood Glam", "Y2K",
    "Castle", "Urban Modern", "Contemporary", "French Country", "Goth",
    "Light Academia", "Dark Academia"
  ];

  // Mapping aesthetics to their respective images
  const aestheticImages = {
    "Minimalist": minimalist,
    "Cottage Core": cottageCore,
    "Bohemian": bohemian,
    "Industrial": industrial,
    "Mid-Century Modern": midCenturyModern,
    "Log Cabin": logCabin,
    "Beach": beach,
    "Vintage": vintage,
    "Rustic": rustic,
    "Eclectic": eclectic,
    "Traditional": traditional,
    "Hollywood Glam": hollywoodGlam,
    "Y2K": y2k,
    "Castle": castle,
    "Urban Modern": urbanModern,
    "Contemporary": contemporary,
    "French Country": frenchCountry,
    "Goth": goth,
    "Light Academia": lightAcademia,
    "Dark Academia": darkAcademia
  };

  // Handle aesthetic selection
  const handleSelectAesthetic = (aesthetic) => {
    setSelectedAesthetics((prevSelected) => {
      const updatedSelection = prevSelected.includes(aesthetic)
        ? prevSelected.filter((item) => item !== aesthetic)
        : [...prevSelected, aesthetic];
        
      onAnswer(updatedSelection); // Pass the updated array to SurveyPage
      return updatedSelection;
    });
  };

  return (
    <div className="question-container">
      <h1>Question 3</h1>
      <h2>Choose Aesthetic(s)</h2>
      <div className="aesthetic-options">
        {aesthetics.map((aesthetic) => (
          <div key={aesthetic} className="aesthetic-option">
                <img
                    src={aestheticImages[aesthetic]}
                    alt={`${aesthetic} example`}
                    className="aesthetic-image"
                />
                <label>
                    <input
                    type="checkbox"
                    className='checkbox-large'
                    value={aesthetic}
                    checked={selectedAesthetics.includes(aesthetic)}
                    onChange={() => handleSelectAesthetic(aesthetic)}
                    />
                    {aesthetic}
                </label>
          </div>
        ))}
      </div>
    </div>
  );
}

// Add prop types validation
Question3.propTypes = {
  onAnswer: PropTypes.func.isRequired,
};

export default Question3;
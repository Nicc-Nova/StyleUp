import { React, useState } from 'react';
import '../assets/stylesheets/SurveyPage.css';
import PropTypes from 'prop-types';

function Question2({ onAnswer }) {

  const [selectedColors, setSelectedColors] = useState([]);

  const handleSelectColor = (colorName) => {
    setSelectedColors((prevSelected) => {
      const updatedSelection = prevSelected.includes(colorName)
        ? prevSelected.filter((item) => item !== colorName)
        : [...prevSelected, colorName];
        
      onAnswer(updatedSelection); // Pass the updated array to SurveyPage
      return updatedSelection;
    });
  };

  // List of aesthetics for the user to choose from
  const colorHexMap = {
    Red: '#8F1600',
    Orange: '#e27602',
    Yellow: '#FCCF55',
    Green: '#315e26',
    Blue: '#023E8A',
    Purple: '#6A0DAD',
    Black: '#000000',
    White: '#FFFFFF',
    Tan: '#D2B48C',
    Brown: '#8B4513',
    Gray: '#808080'
  };

  return (
    <div className="question-container">
      <h1>Question 2</h1>
      <h2>What is your color preference?</h2>
      <div className="color-options">
        {Object.keys(colorHexMap).map((colorName) => (
          <button 
          key={colorName} 
          className={`color-option ${selectedColors.includes(colorName) ? 'selected' : ''}`}
          style={{ backgroundColor: colorHexMap[colorName] }}
          onClick={() => handleSelectColor(colorName)}
          > 
          </button>
        ))}
      </div>
      
    </div>
  );
}

// Add prop types validation
Question2.propTypes = {
  onAnswer: PropTypes.func.isRequired,
};

export default Question2;
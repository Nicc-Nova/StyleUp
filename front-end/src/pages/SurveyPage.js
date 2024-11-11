import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Question1 from '../components/Question1';
import Question2 from '../components/Question2';
import LogoButton from '../components/LogoButton.js';
import '../assets/stylesheets/Question1.css';
import SurveySidebar from '../components/SurveySidebar.js';

function SurveyPage() {
  const [currentQuestion, setCurrentQuestion] = useState(1);
  const [responses, setResponses] = useState({});
  const navigate = useNavigate();

  const handleNext = () => {
    setCurrentQuestion((prev) => Math.min(prev + 1, 5));
  };

  const handlePrevious = () => {
    if (currentQuestion === 1) {
      navigate('/HomePage'); // Navigate back to the home page if on the first question
    } else {
      setCurrentQuestion((prev) => prev - 1); // Otherwise, go to the previous question
    }
  };

  const handleAnswer = (question, answer) => {
    setResponses((prevResponses) => ({
      ...prevResponses,
      [question]: answer,
    }));
  };

  return (
    <div className="survey-page">
      <LogoButton />
      <SurveySidebar currentQuestion={currentQuestion}/>
      <div className="main-content">
        {currentQuestion === 1 && <Question1 onAnswer={(answer) => handleAnswer('Question 1', answer)}/>}
        {currentQuestion === 2 && <Question2 onAnswer={(answer) => handleAnswer('Question 2', answer)}/>}
        {/* Add more questions similarly */}
        <div className="nav-buttons">
          <button className="back-button" onClick={handlePrevious}>←</button>
          <button className="next-button" onClick={handleNext} disabled={currentQuestion === 5}>Next</button>
        </div>
        <div className="responses-summary">
          <h3>Survey Summary</h3>
          <ul>
            {Object.entries(responses).map(([question, answer]) => (
              <li key={question}>
                <strong>{question}:</strong> {answer}
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
}

export default SurveyPage;
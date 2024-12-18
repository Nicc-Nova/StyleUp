import React, { useState } from 'react';
import { useSurveyContext } from '../contexts/SurveyContext.js';
import { useNavigate } from 'react-router-dom';
import Question1 from '../components/Question1';
import Question2 from '../components/Question2';
import Question3 from '../components/Question3.js';
import Question4 from '../components/Question4';
import LogoButton from '../components/LogoButton.js';
import SurveySidebar from '../components/SurveySidebar.js';

function SurveyPage() {
  const [currentQuestion, setCurrentQuestion] = useState(1);
  const [responses, setResponses] = useState({});
  const navigate = useNavigate();
  const { setFinalResponse } = useSurveyContext();

  const handleNext = () => {
    setCurrentQuestion((prev) => Math.min(prev + 1, 4));
  };
                 
  const handlePrevious = () => {
    if (currentQuestion === 1) {
      navigate('/MyRooms'); // Navigate back to the home page if on the first question
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

  const handleSubmit = async () => {
    // Combine responses into a single string
    const responseArray = Object.values(responses).map((answer) =>
      Array.isArray(answer) ? answer.join('%20') : answer
    );
    const finalResponse = responseArray.join('%20');

    // Save to context
    setFinalResponse(finalResponse);
    console.log('Submitting responses:', finalResponse); // Debugging
    navigate('/Results');
  };


  return (
    <div className="survey-page">
      {/* Logo Button */}
      <LogoButton />

      {/* Sidebar to track question progress */}
      <SurveySidebar currentQuestion={currentQuestion} />

      {/* Main Content */}
      <div className="survey-main-content">
        {/* Render Questions */}
        {currentQuestion === 1 && (
          <Question1 onAnswer={(answer) => handleAnswer('Question 1', answer)} />
        )}
        {currentQuestion === 2 && (
          <Question2 onAnswer={(answer) => handleAnswer('Question 2', answer)} />
        )}
        {currentQuestion === 3 && (
          <Question3 onAnswer={(answer) => handleAnswer('Question 3', answer)} />
        )}
        {currentQuestion === 4 && (
          <Question4 onAnswer={(answer) => handleAnswer('Question 4', answer)} />
        )}

        {/* Navigation Buttons */}
        <div className="nav-buttons">
          <button className="back-button" onClick={handlePrevious}>
            ← Back
          </button>
          {currentQuestion < 4 && (
            <button className="next-button" onClick={handleNext}>
              Next →
            </button>
          )}
          {currentQuestion === 4 && (
            <button className="submit-button" onClick={handleSubmit}>
              Submit Survey
            </button>
          )}
        </div>

        {/* Survey Summary */}
        <div className="responses-summary">
          <h3>Survey Summary</h3>
          <ul>
            {Object.entries(responses).map(([question, answer]) => (
              <li key={question}>
                <strong>{question}:</strong>{' '}
                {Array.isArray(answer) ? answer.join(', ') : answer}
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
}

export default SurveyPage;
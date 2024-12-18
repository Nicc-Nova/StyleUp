import { React, useMemo } from 'react';
import '../assets/stylesheets/SurveyPage.css';
import PropTypes from 'prop-types';

function SurveySidebar({ currentQuestion }) {
  const totalQuestions = 4;
  const darkColor = '#633B48'; // Dark brown color
  const lightColor = '#F1E8E8'; // Light brown color

  const sidebarItems = useMemo(() =>
    Array.from({ length: totalQuestions }, (_, index) => ({
      key: `question-${index}`,
      color: index < currentQuestion ? darkColor : lightColor,
    }))
  , [currentQuestion, totalQuestions]);

  return (
    <div className="survey-sidebar">
      {sidebarItems.map(({ key, color }) => (
        <div
          key={key}
          className="sidebar-item"
          style={{
            backgroundColor: color,
          }}
        ></div>
      ))}
    </div>
  );
}

// Add PropTypes validation
SurveySidebar.propTypes = {
  currentQuestion: PropTypes.number.isRequired,
};

export default SurveySidebar;
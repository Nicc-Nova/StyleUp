import React, { createContext, useState, useContext, useMemo } from 'react';
import PropTypes from 'prop-types';

const SurveyContext = createContext();

export const SurveyProvider = ({ children }) => {
  const [finalResponse, setFinalResponse] = useState("");

    // Memoize the value to prevent unnecessary re-renders
  const value = useMemo(() => ({ finalResponse, setFinalResponse }), [finalResponse, setFinalResponse]);

  return (
    <SurveyContext.Provider value={value}>
      {children}
    </SurveyContext.Provider>
  );
};

// Add PropTypes validation for children
SurveyProvider.propTypes = {
    children: PropTypes.node.isRequired,
};

export const useSurveyContext = () => useContext(SurveyContext);
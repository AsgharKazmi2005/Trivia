import React, { useState } from 'react';
import './App.css';
import Question from './assets/comp.jsx';

function App() {
  const [questionData, setQuestionData] = useState(null);
  const [isDisabled, toggleDisability] = useState(false);

  //Clear UI when user generates question
  function clearAnswers() {
    let incorrectList=document.getElementsByClassName('incorrect')
    let correctAnswer = document.querySelector('.correct')

    if (incorrectList.length) {
    Array.from(incorrectList).forEach((wrong) => {
      wrong.classList.remove('incorrect')
    })}
    if (correctAnswer) correctAnswer.classList.remove('correct')
  }
  //Disable button function for dealing with API Limits
  async function disableButton() {
    toggleDisability(true); // Disable the button
    // Wrap setTimeout in a Promise
    await new Promise(resolve => {
        setTimeout(() => {
            toggleDisability(false);
        }, 5000);
    });
  }
  //User generates question
  async function fetchQuestion() {
    clearAnswers()
    disableButton()
    try {
      const response = await fetch(`https://opentdb.com/api.php?amount=1&difficulty=easy&type=multiple`);
      if (!response.ok) {
        throw new Error('Error fetching data');
      }
      const data = await response.json();
      setQuestionData(data.results[0]);
      console.log(data.results[0])
    } catch (err) {
      console.error('Error fetching data:', err);
    }
  }
  return (
    <div className="container">
      <div className="title">Who wants to be a Millionaire!</div>
      {questionData && <Question questionData={questionData} />}
      <button disabled={isDisabled} className = "button" onClick={fetchQuestion}>Get Question</button>
    </div>
  );
}

export default App;
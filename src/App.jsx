// Import useState, useEffect, css stylesheet, and our components
import React, { useState, useEffect } from 'react';
import './App.css';
import { Question, Lifeline } from './assets/comp.jsx';

function App() {
  //State variables to track questionData state, the generate button state, the score state, the answer state, and the game state
  const [questionData, setQuestionData] = useState(null);
  const [isDisabled, toggleDisability] = useState(false);
  const [score, setScore] = useState(1);
  const [isCorrectAnswer, setIsCorrectAnswer] = useState(true);
  const [isGameEnd, setIsGameEnd] = useState(false);
  
  // Clear Answer styles after the user generates a new question
  function clearAnswers() {
    //Clear Styles and Enable Lifelines
    let incorrectList = document.getElementsByClassName('incorrect')
    let correctAnswer = document.querySelector('.correct')

    if (incorrectList.length) {
      Array.from(incorrectList).forEach((wrong) => {
        wrong.classList.remove('incorrect')
      })
    }
    if (correctAnswer) correctAnswer.classList.remove('correct')
  }

  //5-second toggle
  async function disableButton() {
    toggleDisability(true);
    await new Promise(resolve => {
      setTimeout(() => {
        toggleDisability(false);
      }, 5000);
    });
  }

  // Fetch data from the Java backend
  async function fetchQuestion() {
    clearAnswers();
    disableButton();
    try {
      const response = await fetch(`http://localhost:8080/api/question`);
      if (!response.ok) {
        throw new Error('Error fetching data');
      }
      const data = await response.json();
      setQuestionData(data.results[0]);
      console.log(data.results[0]);
      // Update the score based on the current question value, excluding the initial question
      if (questionData !== null) {
        setScore(prevScore => prevScore * 10);
      }
      // Reset the state variables for next question
      setIsCorrectAnswer(true);
    } catch (err) {
      console.error('Error fetching data:', err);
    }
  }

  // Deal with user input and process it to the backend
  function userAnswer(e, answer) {
    let el = e.target;
    if (answer === questionData.correct_answer) {
      el.classList.add('correct');
      setScore(prevScore => prevScore * 10);
    } else {
      el.classList.add('incorrect');
      let correct = document.querySelector(`.a${questionData.correct_answer.split(' ').join('')}`);
      correct.classList.add('correct');
      setIsCorrectAnswer(false);
    }
    toggleDisability(true);
  }

  // Push a question to the UI on document mount
  useEffect(() => {
    fetchQuestion();
  }, []);

  // Reset all state variables on restart
  function restartGame() {
    setIsGameEnd(false);
    setScore(1);
    fetchQuestion();
  }

  // Render modal on game end
  useEffect(() => {
    if (!isCorrectAnswer && !isGameEnd) {
      setIsGameEnd(true);
    }
  }, [isCorrectAnswer]);

  // Set up dynamic UI
  return (
    <div className="container">
      <div className="title">Who wants to be a Millionaire!</div>
      <div className='scorecont'>
      <div className="score">Score: ${score}</div>
      </div>
      {questionData && <Question questionData={questionData} userAnswer={userAnswer} />}
      <Lifeline questionData={questionData}></Lifeline>
      <button disabled={isDisabled} className="button" onClick={fetchQuestion}>Get Question</button>
      {isGameEnd && <EndGameModal score={score} onRestart={restartGame} />}
    </div>
  );
}

export default App;

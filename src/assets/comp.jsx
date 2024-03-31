//Import useState and useEffect
import React, { useState, useEffect } from 'react';
import {AudienceResponseModal, PhoneAFriendModal,EndGameModal} from './modal.jsx';

//Create Question components which will be our quiz. We pass our API data as a prop
export function Question({ questionData }) {
    //State variable that contains the answer choices
    const [answersArr, setAnswersArr] = useState([]);
    //State variable that lets us know if a question is answered or not so users can't click multiple options
    const [isAnswered, setAnswered] = useState(false);
    const [score, setScore] = useState(1); 
    const [showEndGameModal, setShowEndGameModal] = useState(false);
    
    //Function for decoding the annoying HTML from the API. Use the .textContent property on a dummy HTML element
    function decodeHTML(encodedString) {
        const tempElement = document.createElement('div');    
        tempElement.innerHTML = encodedString;    
        const decodedString = tempElement.textContent;    
        return decodedString;
    }
    //API is annoying with its HTML formatting so we have to decode everything, starting with the answer
    let correctAnswer = decodeHTML(questionData.correct_answer);
    console.log("Correct" + correctAnswer)

    //Code to be executed when a new question is created: generate random decoded multiple choice
    useEffect(() => {
        //Once the component loads, we reset setAnswered to false as there is no answer yet
        setAnswered(false);

        //Function to shuffle array randomly for multiple choice implementation
        function shuffleArray(array) {
            for (let i = array.length - 1; i > 0; i--) {
                const j = Math.floor(Math.random() * (i + 1));
                [array[i], array[j]] = [array[j], array[i]];
            }
            return array;
        }
        //Function to apply the shuffle the 3 incorrect answers with the 1 correct answer
        function randomizeQuestions(incorrectAnswers, answer) {
            return shuffleArray([...incorrectAnswers, answer]);
        }
        
        //Create an array that utilizes the two above functions to randomize answer choices. Clean the HTML from it too.
        const randomizedAnswers = randomizeQuestions(questionData.incorrect_answers, questionData.correct_answer);
        const decodedAnswers = randomizedAnswers.map(decodeHTML);
    //Save the resulting randomized, decoding answer choices to our answerArr to be used in the app
    setAnswersArr(decodedAnswers);
    }, [questionData]);

    //When user selects an answer: Apply styles to show that they are wrong/right and block any further answers
    function userAnswer(e, answer) {
        //Target the clicked element
        let el = e.target;
        //If it isn't answered (blocks from choosing multiple answers)
        if (!isAnswered) {
            if (answer === correctAnswer) {
                //If it's correct, apply a green correct style, change state of isAnswered to true to avoid multiple answers
                el.classList.add('correct');
                setAnswered(true);
                setScore(score * 10);
            } else {
                //If wrong, apply incorrect class, show correct answer, change state of isAnswered to true to avoid multiple answers
                el.classList.add('incorrect');
                setAnswered(true);
                let correct = document.querySelector(`.a${correctAnswer.split(' ').join('')}`);
                correct.classList.add('correct');
                setShowEndGameModal(true);
            }
        }
    }

    //HTML export from the component. Include decoded category, decoded question, four randomized decoded answers. 
    //Save answer id and answer name so we can refer to all four buttons and the correct answer without needing to loop
    return (
        <div className='apiContent'>
            <div className='info'>
                <div className='category'>Category: {decodeHTML(questionData.category)}</div>
                <div className='difficulty'>Difficulty: <span id={questionData.difficulty}>{questionData.difficulty}</span></div>
            </div>
            <div className="containerQA">
                <div className="question">{decodeHTML(questionData.question)}</div>
                <div className="answerContainer">
                    {answersArr.map((answer, index) => (
                        <div key={index} onClick={(e) => userAnswer(e, answer)} className={`answer a${answer.split(' ').join('')}`} id={`button${index + 1}`}>
                            {answer.replaceAll(/[^'a-zA-Z0-9']/g, '-').split('-').join(' ')}
                        </div>
                    ))}
                </div>
            </div>
            {showEndGameModal && <EndGameModal score={score} onRestart={() => setShowEndGameModal(false)} />}
        </div>
    );
}


export function Lifeline({ questionData }) {
    const [lifelines, setLifelines] = useState([
        { name: '50/50', active: true },
        { name: 'Phone a Friend', active: true },
        { name: 'Ask the Audience', active: true },
    ]);

    const [modalOpen, setModalOpen] = useState(false);
    const [answerChoices, setAnswerChoices] = useState([]);
    const [percentages, setPercentages] = useState([]);
    const [friendAnswer, setFriendAnswer] = useState('');

        // Function to generate random percentages that sum up to 100%
    const generateRandomPercentages = () => {
            const percentages = [];
            for (let i = 0; i < 4; i++) {
                const randomPercentage = Math.floor(Math.random() * 100);
                percentages.push(randomPercentage);
            }
            const total = percentages.reduce((acc, curr) => acc + curr, 0);
            return percentages.map(percent => Math.round((percent / total) * 100)); // Convert to percentages that sum up to 100%
    };
    
        // Function to display answer choices with corresponding percentages
    const displayAnswerPercentages = (incorrectAnswers, correctAnswer, percentages) => {
            const answerChoices = [...incorrectAnswers, correctAnswer];
            for (let i = 0; i < answerChoices.length; i++) {
                console.log(`${answerChoices[i]}: ${percentages[i]}%`);
            }
    };

    function decodeHTML(encodedString) {
        const tempElement = document.createElement('div');    
        tempElement.innerHTML = encodedString;    
        const decodedString = tempElement.textContent;    
        return decodedString;
    }

    function shuffleArray(array) {
        for (let i = array.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [array[i], array[j]] = [array[j], array[i]];
        }
        return array;
    }
    

    const handleLifelineClick = async (index) => {
        if (!lifelines[index].active) return;

        const updatedLifelines = [...lifelines];
        updatedLifelines[index].active = false;
        setLifelines(updatedLifelines);

        // Close the modal if the other lifeline is already open
        if (modalOpen) {
            setModalOpen(false);
        }


        // Logic for "Ask the Audience" lifeline
        if (index === 2) {
            // Simulate audience response by generating random percentages for each answer choice
            const newPercentages = generateRandomPercentages();
            console.log(newPercentages)
            setPercentages(newPercentages);

            // Display the answer choices with their corresponding percentages
            setAnswerChoices([...questionData.incorrect_answers, questionData.correct_answer]);

            // Open the modal to display the audience response
            setModalOpen(true);
        }

        if (index === 1) {
            // Simulate friend's response by selecting a random answer choice
            setFriendAnswer(questionData.correct_answer);
            // Open the Phone a Friend modal
            setModalOpen(true);
        }

        // Logic to remove two incorrect answers only if 50/50 lifeline is active
        if (index === 0) {
            const incorrectAnswerButtons = document.querySelectorAll('.answer');
            console.log(incorrectAnswerButtons)
            const correctAnswerText = decodeHTML(questionData.correct_answer);
            const buttonArray = Array.from(incorrectAnswerButtons);
            const filteredButtons = buttonArray.filter(button => button.textContent !== correctAnswerText);
            const shuffledValues = shuffleArray([...filteredButtons]);
            shuffledValues.forEach((button, i) => {
                    if (i < 2) {
                        button.classList.add('incorrect');
                        button.disabled = true; // Disable the removed buttons
                    }
                });
            
        }

        try {
            // Send lifeline update to the backend
            await fetch('http://localhost:8080/api/lifelines', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ index }),
            });
            console.log('Lifeline updated successfully.');
        } catch (error) {
            console.error('Error updating lifeline:', error);
        }
    };

    return (
        <div className='lifelineCont'>
            <div>
                {lifelines.map((lifeline, index) => (
                    <button className="lifelines" key={index} onClick={() => handleLifelineClick(index)} disabled={!lifeline.active}>
                        {lifeline.name}
                    </button>
                ))}
            </div>
            <PhoneAFriendModal isOpen={modalOpen && friendAnswer !== ''} onClose={() => setModalOpen(false)} friendAnswer={friendAnswer} />
            <AudienceResponseModal isOpen={modalOpen && friendAnswer === ''} onClose={() => setModalOpen(false)} answerChoices={answerChoices} percentages={percentages} />
        </div>
    );
}

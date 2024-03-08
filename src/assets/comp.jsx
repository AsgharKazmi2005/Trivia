import React, { useState, useEffect } from 'react';

function Question({ questionData }) {
    let correctAnswer = decodeHTML(questionData.correct_answer);
    const [answersArr, setAnswersArr] = useState([]);
    const [isAnswered, setAnswered] = useState(false);
    
    function decodeHTML(encodedString) {
        const tempElement = document.createElement('div');    
        tempElement.innerHTML = encodedString;    
        const decodedString = tempElement.textContent;    
        return decodedString;
    }

    //SHUFFLE ANSWERS
    useEffect(() => {
        setAnswered(false);
        function shuffleArray(array) {
            for (let i = array.length - 1; i > 0; i--) {
                const j = Math.floor(Math.random() * (i + 1));
                [array[i], array[j]] = [array[j], array[i]];
            }
            return array;
        }

        function randomizeQuestions(incorrectAnswers, answer) {
            return shuffleArray([...incorrectAnswers, answer]);
        }

        const randomizedAnswers = randomizeQuestions(questionData.incorrect_answers, questionData.correct_answer);
        const decodedAnswers = randomizedAnswers.map(decodeHTML); // Apply decodeHTML to each answer
    setAnswersArr(decodedAnswers);
    }, [questionData]);

    //HANDLE CLICKS
    function userAnswer(e, answer) {
        let el = e.target;
        if (!isAnswered) {
            if (answer === correctAnswer) {
                el.classList.add('correct');
                setAnswered(true);
            } else {
                el.classList.add('incorrect');
                setAnswered(true);
                console.log(correctAnswer);
                let correct = document.querySelector(`.a${correctAnswer.split(' ').join('')}`);
                console.log(correct);
                correct.classList.add('correct');
            }
        }
    }

    return (
        <div className='apiContent'>
            <div className='category'>Category: {decodeHTML(questionData.category)}</div>
            <div className="containerQA">
                <div className="question">{decodeHTML(questionData.question)}</div>
                <div className="answerContainer">
                    {answersArr.map((answer, index) => (
                        <div key={index} onClick={(e) => userAnswer(e, answer)} className={`answer a${answer.split(' ').join('')}`} id={`button${index + 1}`}>
                            {answer}
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}

export default Question;


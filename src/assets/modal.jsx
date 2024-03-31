import React from 'react';

// Render modal to store the 'Ask the Audience' data.
export function AudienceResponseModal({ isOpen, onClose, answerChoices, percentages }) {
    if (!isOpen) return null;

    return (
        <div className='modal-overlay'>
            <div className="modal">
                <div className="modal-content">
                    <span className="close" onClick={onClose}>&times;</span>
                    <h2>Audience Response</h2>
                    <div>
                        {answerChoices.map((choice, index) => (
                            <h5 key={index}>
                                <p>{choice}: {percentages[index]}%</p>
                            </h5>
                        ))}
                    </div>
                </div>
            </div>
        </div>
    );
}
// Render modal component to show the PhoneAFriend data
export const PhoneAFriendModal = ({ isOpen, onClose, friendAnswer }) => {
    return (
        <>
            {isOpen && (
                <div style={{ position: 'fixed', top: '0', left: '0', right: '0', bottom: '0', backgroundColor: 'rgba(0, 0, 0, 0.5)', zIndex: '1000' }} onClick={onClose}>
                    <div style={{ position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', backgroundColor: 'white', padding: '20px', borderRadius: '8px', zIndex: '1001' }} onClick={(e) => e.stopPropagation()}>
                        <h2>Phone a Friend</h2>
                        <p>Your friend suggests:</p>
                        <p>{friendAnswer}</p>
                        <button onClick={onClose}>Close</button>
                    </div>
                </div>
            )}
        </>
    );
};

// Render modal for the end game
export function EndGameModal({ score }) {
    if (score>=1000000) {
        score=1000000
    } else if (score >= 1000) {
        score = 1000
    } else {
        score=0
    }
  return (
    <div className="modal-overlay">
      <div className="modal">
        <div className="modal-content">
          <p>You Won ${score}! </p>
          <button onClick={() => window.location.reload()}>Restart Game</button>
        </div>
      </div>
    </div>
  );
}

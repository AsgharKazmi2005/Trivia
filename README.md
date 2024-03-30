Who want to be a Millionaire by Asghar Kazmi and Denis Musovski

The application is built using a Maven Java backend that also serves as an API for the frontend. Data is exchanged between the Java backend and React frontend through http.

Below are the features implemented using best Java and OOP principles

- [X] Feature 1: Interactive Question and Options Display

        Objective: Modify the question display to present the question and shuffled answer options to the user without showing the correct answer.
        Requirements: Update the TriviaQuestionParser class. Ensure options are displayed in a random order.
        Implementation: The TrviaQuestionParser correctly uses a shuffling algorithm to mix up the answer choices. The frontend then displays these choices to the user without anyway of the user finding the correct answer.
  

- [X] Feature 2: User Option Selection and Validation

        Objective: Implement a user input mechanism to allow answer selection and validate the response.
        Requirements: Use Scanner for input in the MillionaireCoderGame class. Add answer validation logic in the Question class.
        Implementation: Since we used a React frontend, text field inputs as react state variables were used instead of a Scanner object for input. Answer validation logic is present in the Question class and works properly. ??????


- [X] Feature 3: Progressive Difficulty

        Objective: Adjust the difficulty of questions as the game progresses.
        Requirements: Modify the TriviaQuestionFetcher to include a difficulty parameter. Difficulty should increase based on the player's score.
        Implementation: Using a switch case, the difficulty is increased every 5 fetches. The API string is then concatinated accordingly within TriviaQuestionFetcher. Easy -> Medium -> Hard. The current difficulty is also shown to the user on the UI.
        


- [X] Feature 4: Lifelines Implementation

      Objective: Create lifelines to assist players, including a simulated "Phone a Friend" feature.
      Requirements:Implement "50:50", "Ask the Audience", and "Phone a Friend" lifelines. "Phone a Friend" can be a simulated feature offering hints.
      Implementation: All three lifelines are correctly implemented in Java and use polymorphism to conduct different functions with the same function name.

- [X] Feature 5: Scoring System and Safe Havens

      Objective: Develop a scoring system that rewards correct answers and establishes milestones.
      Requirements: Expand the Player class to manage scores and safe havens. Create a list to track scores and determine safe haven thresholds.
      Implementation: Once a user gets a question wrong, their final score is dependent on set safehavens. This functionality is correctly coded in the Player class. It will either be $0, $1000, or $1000000. The score starts at $1 and increases 10x for every correct                           answer and is shown on the UI for the user to see at all times.


- [X] Feature 6: Simple Frontend

      Objective: Update the game to include a basic web interface for enhanced player interaction.
      Requirements: Modify the GameServer to serve an HTML page for the game. Use form submission for answer selection.
      Implementation: The GameServer functions as an API to send POST and GET requests between the Java backend and React frontend. Form submission is handled correctly. Everything is styled well. Do note that a CORS extension is used to bypass any CORS errors.
  
Technical Details:

- [X] Your code should compile and run without errors.
          Comment: The code may contain a few bugs as API limits, CORS, and requests may run into issues. The environment also needs to be set correctly for the code to run.
- [X] Follow Java coding standards and best practices.


Evaluation Criteria:

Correctness of feature implementation.
Code quality, including readability and use of conventions.
Creativity in feature design, especially for "Phone a Friend" and frontend (Please do not stress too much over frontend).

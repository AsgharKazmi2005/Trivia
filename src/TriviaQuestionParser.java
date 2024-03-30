import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.Collections;
import java.util.List;

public class TriviaQuestionParser {
    // Method to parse the trivia question data received from the API
    public static String parseTriviaQuestion(String rawData) {
        // Use Gson library to parse JSON data into Java objects
        Gson gson = new Gson();
        Question questionData = gson.fromJson(rawData, Question.class);

        // Extract relevant information from the parsed data and format it as needed
        String category = questionData.getCategory();
        String question = questionData.getQuestion();
        String correctAnswer = questionData.getCorrectAnswer();
        List<String> incorrectAnswers = questionData.getIncorrectAnswers();

        // Shuffle the list of incorrect answers
        Collections.shuffle(incorrectAnswers);

        // Format the parsed data as a JSON object to be sent to the frontend
        JsonObject parsedData = new JsonObject();
        parsedData.addProperty("category", category);
        parsedData.addProperty("question", question);
        parsedData.addProperty("correctAnswer", correctAnswer);
        parsedData.add("incorrectAnswers", gson.toJsonTree(incorrectAnswers));

        // return the data to be used in the main file
        return gson.toJson(parsedData);
    }
}
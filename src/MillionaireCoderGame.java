// Imports
import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class MillionaireCoderGame {
    //Display questions and also print them to the console
    public static void main(String[] args) {
        String jsonResponse = TriviaQuestionFetcher.fetchTriviaQuestions();
        System.out.println(jsonResponse);
        if (jsonResponse != null) {
            // Parse the JSON response to get a list of Question objects
            Gson gson = new Gson();
            Question[] questionsArray = gson.fromJson(jsonResponse, Question[].class);
            List<Question> questions = new ArrayList<>(List.of(questionsArray));

            // Display the questions to the user
            for (int i = 0; i < questions.size(); i++) {
                Question q = questions.get(i);
                System.out.println("Category: " + q.getCategory());
                System.out.println("Question: " + q.getQuestion());
                System.out.println("Options:");
                System.out.println("  1. " + q.getCorrectAnswer());
                for (int j = 0; j < q.getIncorrectAnswers().size(); j++) {
                    System.out.println("  " + (j + 2) + ". " + q.getIncorrectAnswers().get(j));
                }
                System.out.println();
            }

            // Here, you would add logic for the user to choose an answer and check if it's correct
            List<Boolean> results = new ArrayList<>();
            for (int i = 0; i < questions.size(); i++) {
                Question q = questions.get(i);
                // Check if user's answer matches the correct answer
                boolean isCorrect = checkAnswer(q.getCorrectAnswer(), "User's answer", q.getIncorrectAnswers());
                results.add(isCorrect);
                // If correct, update the score accordingly
                if (isCorrect) {
                    Player.updateScore();
                // Else calculate the final user score
                } else {
                    Player.calculateScore(Player.score);
                }
            }

            // Send the results to the frontend using a server
            sendResultsToServer(results);
        }
    }

    //Check the user answer
    private static boolean checkAnswer(String correctAnswer, String userAnswer, List<String> incorrectAnswers) {
        // Check if user's answer matches the correct answer or any of the incorrect answers
        return correctAnswer.equals(userAnswer) || incorrectAnswers.contains(userAnswer);
    }

    //Send response to the front end and backend
    private static void sendResultsToServer(List<Boolean> results) {
        // URL
        String url = "http://localhost:5173";

        // Convert results to JSON string
        Gson gson = new Gson();
        String jsonResults = gson.toJson(results);

        // Create an HttpClient instance
        HttpClient client = HttpClient.newHttpClient();

        // Build the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonResults))
                .build();

        // Send the HTTP request and handle the response
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .join();
    }
}
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

public class TriviaQuestionFetcher {
    // Track fetchcount for difficulty updating
    private static int fetchCount = 0;
    // Default to easy
    private static String difficulty = "easy";

    // API Call to return the data depending on difficulty
    public static String fetchTriviaQuestions() {
        // Create HttpGet request with the URL
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://opentdb.com/api.php?amount=1&difficulty=" + difficulty + "&type=multiple"))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // Update fetch count
            fetchCount++;
            // Check if 5 fetches are done, then update difficulty
            if (fetchCount == 5) {
                updateDifficulty();
                fetchCount = 0;
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Function to update the difficulty.
    private static void updateDifficulty() {
        // Update difficulty based on current difficulty
        switch (difficulty) {
            case "easy":
                difficulty = "medium";
                break;
            case "medium":
                difficulty = "hard";
                break;
            case "hard":
                break;
            default:
                difficulty = "easy"; // Default to easy
        }
    }
}
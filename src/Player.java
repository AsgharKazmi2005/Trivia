import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
public class Player {
    static int score=1;
    //Lifelines handled in Lifeline file with the frontend
    // Calculate FINAL score and send to frontend
    public static void calculateScore(int score) {
        int finalScore=score;
        if (score >= 1000000) {
            finalScore= 1000000;
        } else if (score >= 1000) {
            finalScore= 1000;
        } else {
            finalScore = 0;
        }
        sendScoreToFrontend(finalScore);
    }

    // Every correct question, the user gains 10x money
    public static void updateScore() {
        score*=10;
    }

    // Function to send the score to the frontend using HTTP
    private static void sendScoreToFrontend(int score) {
        try {
            // Define the URL of the frontend endpoint to receive the score
            URL url = new URL("http://localhost:8080/score-endpoint");

            // Open connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to POST
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Create JSON payload with the score
            String jsonPayload = "{\"score\": " + score + "}";

            // Write the JSON payload to the connection output stream
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(jsonPayload.getBytes());
            }

            // Get the response code
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Score sent successfully to frontend.");
            } else {
                System.err.println("Failed to send score to frontend. Response code: " + responseCode);
            }

            // Disconnect the connection
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error sending score to frontend: " + e.getMessage());
        }
    }
}
// Imports
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

public class Lifeline {
    // Array to track whether lifelines are already used. {50/50, phone a friend, ask the audience}
    private static boolean[] lifelines = {true, true, true};

    public static void main(String[] args) {
        //Send the lifeline data to /api/lifelines, so it can manipulate it and return any changes
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

            // Endpoint to handle POST requests for updating the lifelines
            server.createContext("/api/lifelines", (exchange -> {
                if ("POST".equals(exchange.getRequestMethod())) {
                    threadPoolExecutor.execute(new LifelineHandler(exchange));
                }
            }));

            // Start the server
            server.start();
            System.out.println("Server started at http://localhost:8080/");
        // Handle errors
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    // Handle changes in the lifeline usage from the front-end
    private static class LifelineHandler implements Runnable {
        private final HttpExchange exchange;

        public LifelineHandler(HttpExchange exchange) {
            this.exchange = exchange;
        }

        // Method that verifies that the front-end gets the payload
        @Override
        public void run() {
            try {
                // Read the index of the lifeline to be updated from the request body
                int index = Integer.parseInt(new String(exchange.getRequestBody().readAllBytes()).trim());
                updateLifeline(index);
                sendResponse(exchange, 200, "Lifeline updated");
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        // Manage the state variables of the lifelines
        private void updateLifeline(int index) {
            if (index >= 0 && index < lifelines.length) {
                lifelines[index] = false; // Update the lifeline at the specified index
            }
        }
        // Handle Responses
        private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
            exchange.sendResponseHeaders(statusCode, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    //Interface for polymorphism between the lifelines
    interface Lifelines {
        Object generateAnswers();
    }

    // Class FiftyFifty uses polymorphism to implement itself
    static class FiftyFifty implements Lifelines {
        // Define private variables that will be put in through the constructor
        private String incorrect1;
        private String incorrect2;
        private String incorrect3;

        public FiftyFifty(String incorrect1, String incorrect2, String incorrect3) {
            this.incorrect1 = incorrect1;
            this.incorrect2 = incorrect2;
            this.incorrect3 = incorrect3;
        }

        // Randomly generate 2 incorrect answers using this
        public ArrayList<String> generateAnswers() {
            ArrayList<String> incorrectAnswers = new ArrayList<>();
            incorrectAnswers.add(incorrect1);
            incorrectAnswers.add(incorrect2);
            incorrectAnswers.add(incorrect3);

            // Select two incorrect answers randomly
            Random random = new Random();
            int randomIndex1 = random.nextInt(3);
            String answer1 = incorrectAnswers.remove(randomIndex1);
            int randomIndex2 = random.nextInt(2);
            String answer2 = incorrectAnswers.get(randomIndex2);

            // Send the answers to the frontend server
            sendToServer(answer1, answer2);

            return incorrectAnswers; // Returning the remaining incorrect answer (not used here)
        }

        private void sendToServer(String answer1, String answer2) {
            // URL where you want to send the answers
            String url = "http://localhost:5173";

            // Prepare the JSON payload with the incorrect answers
            String jsonPayload = "{\"answer1\": \"" + answer1 + "\", \"answer2\": \"" + answer2 + "\"}";

            // Create an HttpClient instance
            HttpClient client = HttpClient.newHttpClient();

            // Build the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            // Send the HTTP request and handle the response
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(System.out::println)
                    .join();
        }
    }

    // Class AskTheAudience uses polymorphism to define itself
    static class AskTheAudience implements Lifelines {
        // Use Constructor to define variables
        private String answer1;
        private String answer2;
        private String answer3;
        private String answer4;

        public AskTheAudience(String answer1, String answer2, String answer3, String answer4) {
            this.answer1 = answer1;
            this.answer2 = answer2;
            this.answer3 = answer3;
            this.answer4 = answer4;
        }

        // Give each answer its own percentage and send this to the frontend. Use a Map for this.
        public Map<String, Integer> generateAnswers() {
            Map<String, Integer> answersWithPercentages = new HashMap<>();
            ArrayList<String> answers = new ArrayList<>();
            answers.add(answer1);
            answers.add(answer2);
            answers.add(answer3);
            answers.add(answer4);

            Random random = new Random();
            int remainingPercentage = 100;
            for (int i = 0; i < answers.size() - 1; i++) {
                int randomPercentage = random.nextInt(remainingPercentage);
                answersWithPercentages.put(answers.get(i), randomPercentage);
                remainingPercentage -= randomPercentage;
            }
            // Assign the remaining percentage to the last answer
            answersWithPercentages.put(answers.get(answers.size() - 1), remainingPercentage);

            // Send the answers to the frontend server
            sendToServer(answersWithPercentages);

            return answersWithPercentages;
        }

        // Send the payload
        private void sendToServer(Map<String, Integer> answersWithPercentages) {
            // Url to end request
            String url = "http://localhost:5173";

            // Prepare the JSON payload with the answers and percentages
            StringBuilder jsonPayloadBuilder = new StringBuilder("{");
            for (Map.Entry<String, Integer> entry : answersWithPercentages.entrySet()) {
                jsonPayloadBuilder.append("\"").append(entry.getKey()).append("\": ").append(entry.getValue()).append(", ");
            }
            jsonPayloadBuilder.delete(jsonPayloadBuilder.length() - 2, jsonPayloadBuilder.length()); // Remove the last comma and space
            jsonPayloadBuilder.append("}");

            String jsonPayload = jsonPayloadBuilder.toString();

            // Create an HttpClient instance
            HttpClient client = HttpClient.newHttpClient();

            // Build the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            // Send the HTTP request and handle the response
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(System.out::println)
                    .join();
        }
    }

    // Class PhoneAFriend uses polymorphism to define itself
    static class PhoneAFriend implements Lifelines {
        // Define using constructor
        private String answer1;
        private String answer2;
        private String answer3;
        private String correct;

        public PhoneAFriend(String answer1, String answer2, String answer3, String correct) {
            this.answer1 = answer1;
            this.answer2 = answer2;
            this.answer3 = answer3;
            this.correct = correct;
        }

        // Your friend has an 80% chance of telling you the right answer, 20% incorrect. This function implements that
        public String generateAnswers() {
            ArrayList<String> answers = new ArrayList<>();
            answers.add(answer1);
            answers.add(answer2);
            answers.add(answer3);
            answers.add(correct);

            // Randomly decide whether to select the correct answer or not. 80% chance
            Random random = new Random();
            boolean selectCorrectAnswer = random.nextDouble() < 0.8;

            // If true, then return correct, else pick a random answer from those that are left
            if (selectCorrectAnswer) {
                sendToServer(correct);
            } else {
                // Exclude the correct answer from the list
                answers.remove(correct);
                // Select one of the incorrect answers randomly
                int randomIndex = random.nextInt(3);
                sendToServer(answers.get(randomIndex));
            }
            return "answers generated";
        }

        private void sendToServer(String selectedAnswer) {
            // URL where you want to send the answer
            String url = "http://localhost:5173";

            // Prepare the JSON payload with the selected answer
            String jsonPayload = "{\"selectedAnswer\": \"" + selectedAnswer + "\"}";

            // Create an HttpClient instance
            HttpClient client = HttpClient.newHttpClient();

            // Build the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            // Send the HTTP request and handle the response
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(System.out::println)
                    .join();
        }
    }
}

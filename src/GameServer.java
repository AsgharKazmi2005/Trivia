//Imports
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//Game Server that acts as an API for the front-end
public class GameServer {
    public static void main(String[] args) {
        // Create Server at localhost port 8080 and store the API data at /api/question. The front-end will access this.
        try {
            //Create Server at port 8080 using HttpServer
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

            // Define API endpoints to send data
            server.createContext("/api/question", (exchange -> {
                // Implement logic to generate and return a quiz question
                String questionJson = TriviaQuestionFetcher.fetchTriviaQuestions();
                sendResponse(exchange, 200, questionJson);
            }));

            // Enable CORS so we can connect our localhost ports (or else it's annoying)
            server.createContext("/", (exchange -> {
                // Set CORS headers to allow requests from any origin
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
                exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
                exchange.sendResponseHeaders(204, -1);
            }));

            // Create a scheduled executor service to fetch a new question every 5 seconds since the API has a 5-second limit
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> {
                // Return quiz question
                String parsedJson = TriviaQuestionParser.parseTriviaQuestion(TriviaQuestionFetcher.fetchTriviaQuestions());
                // Sending it as a response to the /api/question endpoint which our frontend will consume
                server.createContext("/api/question", (exchange -> {
                    sendResponse(exchange, 200, parsedJson);
                }));
            }, 0, 5, TimeUnit.SECONDS);

            // Start the server and log it to the console
            server.start();
            System.out.println("Server started at http://localhost:8080/");
        // Catch any errors
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    // Helper method to send HTTP response with specified status code and body
    private static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
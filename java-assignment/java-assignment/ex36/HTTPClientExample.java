import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Fetches public user data from the GitHub API using Java 11+ HttpClient.
 * Compile: javac HTTPClientExample.java
 * Run    : java  HTTPClientExample
 *
 * Optional: pipe the body through `jq` for pretty-printed JSON.
 */
public class HTTPClientExample {

    public static void main(String[] args) throws IOException, InterruptedException {
        String username = "torvalds"; // change to any GitHub username
        String apiUrl   = "https://api.github.com/users/" + username;

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Accept", "application/vnd.github+json")
                .header("User-Agent", "Java-HttpClient/1.0")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status : " + response.statusCode());
        System.out.println("URL    : " + apiUrl);
        System.out.println();

        // Pretty-print key fields without an external library
        String body = response.body();
        printField(body, "login");
        printField(body, "name");
        printField(body, "public_repos");
        printField(body, "followers");
        printField(body, "location");

        System.out.println("\nFull response body (JSON):");
        System.out.println(body);
    }

    /** Naive field extractor for simple JSON string / number values. */
    static void printField(String json, String field) {
        String key = "\"" + field + "\":";
        int start = json.indexOf(key);
        if (start == -1) return;
        start += key.length();
        // skip leading whitespace
        while (start < json.length() && json.charAt(start) == ' ') start++;
        boolean isString = json.charAt(start) == '"';
        int end;
        if (isString) {
            start++;
            end = json.indexOf('"', start);
            System.out.printf("%-15s: %s%n", field, json.substring(start, end));
        } else {
            end = json.indexOf(',', start);
            if (end == -1) end = json.indexOf('}', start);
            System.out.printf("%-15s: %s%n", field, json.substring(start, end).trim());
        }
    }
}

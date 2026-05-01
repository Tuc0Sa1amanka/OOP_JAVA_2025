package connector;

import java.net.URI;
import java.time.Duration;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Connector {
    private final HttpClient client = HttpClient.newHttpClient();
    public String getBody(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .timeout(Duration.ofMinutes(1))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() > 299) {
            throw new IOException("Response failed with status code: " + response.statusCode() + "\nbody: " + response.body());
        }
        return response.body();
    }
}

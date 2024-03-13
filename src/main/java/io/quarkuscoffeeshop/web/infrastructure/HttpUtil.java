package io.quarkuscoffeeshop.web.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.annotations.RegisterForReflection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

// bag 回避
@RegisterForReflection
public class HttpUtil {

    static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    
    static final ObjectMapper objectMapper = new ObjectMapper();

    static void sendHttp() {
        
        try {
            getHttp();
        } catch (IOException | InterruptedException ex) {
            logger.error(ex.getMessage());
        }
    }

    public static void getHttp() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
        .newBuilder(URI.create("http://localhost:8080/dashboard/stream"))
        .header("User-Agent", "Java HttpClient")
        .build();

        CompletableFuture<HttpResponse<String>> future =
            client.sendAsync(request,
            HttpResponse.BodyHandlers.ofString());
    }

}

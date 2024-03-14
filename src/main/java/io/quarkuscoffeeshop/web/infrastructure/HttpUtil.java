package io.quarkuscoffeeshop.web.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.runtime.annotations.StaticInitSafe;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import java.util.Optional;

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

    // @ConfigProperty(name="streamUrl")
    // static String streamUrl;
    
    static final ObjectMapper objectMapper = new ObjectMapper();

    public static void sendHttp() {
        
        try {
            //System.out.println("AAAAAAAAAAAAA  "+streamUrl);
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

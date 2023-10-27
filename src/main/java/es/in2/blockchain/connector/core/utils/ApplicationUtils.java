package es.in2.blockchain.connector.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.blockchain.connector.core.exception.RequestErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

import static es.in2.blockchain.connector.core.utils.BlockchainConnectorUtils.ACCEPT_HEADER;
import static es.in2.blockchain.connector.core.utils.BlockchainConnectorUtils.APPLICATION_JSON_HEADER;
import static es.in2.blockchain.connector.core.utils.BlockchainConnectorUtils.CONTENT_HEADER;

@Slf4j
@Component
public class ApplicationUtils {

    public String getRequest(String url) {
        // Create request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers(ACCEPT_HEADER, APPLICATION_JSON_HEADER)
                .GET()
                .build();
        // Send request asynchronously
        CompletableFuture<HttpResponse<String>> response =
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        // Verify Response HttpStatus
        checkGetResponse(response);
        return response.thenApply(HttpResponse::body).join();
    }

    public void patchRequest(String url, String requestBody) {
        // Create request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(requestBody))  // Use PATCH method
                .build();
        // Send request asynchronously
        CompletableFuture<HttpResponse<String>> response =
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        // Verify Response HttpStatus
        checkResponse(response);
    }

    private void checkGetResponse(CompletableFuture<HttpResponse<String>> response) {
        String statusCode = response.thenApply(HttpResponse::statusCode).join().toString();
        String body = response.thenApply(HttpResponse::body).join();
        if (statusCode.equals("200")) {
            log.debug(" > Request successful");
        } else if (statusCode.equals("404")) {
            log.error(" > !! Not found");
            throw new NoSuchElementException("Not found: " + body);
        } else {
            log.error(" > !! Bad Request");
            throw new RequestErrorException("Bad Request: " + body);
        }
    }

    public void postRequest(String url, String requestBody) {
        // Create request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers(CONTENT_HEADER, APPLICATION_JSON_HEADER, ACCEPT_HEADER, APPLICATION_JSON_HEADER)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        // Send request asynchronously
        CompletableFuture<HttpResponse<String>> response =
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        // Verify Response HttpStatus
        checkResponse(response);
    }

    private void checkResponse(CompletableFuture<HttpResponse<String>> response) {
        String statusCode = response.thenApply(HttpResponse::statusCode).join().toString();
        String body = response.thenApply(HttpResponse::body).join();
        switch (statusCode) {
            case "201", "200" -> log.debug("Request successful");
            case "204" -> log.debug("Successful Patch");
            default -> {
                log.error("Bad Request");
                throw new RequestErrorException("Bad Request: " + body);
            }
        }
    }

    public String calculateSHA256Hash(String data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

}
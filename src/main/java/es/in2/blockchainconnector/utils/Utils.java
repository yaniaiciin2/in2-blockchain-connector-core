package es.in2.blockchainconnector.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.blockchainconnector.exception.RequestErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class Utils {

    public static final String SHA_256_ALGORITHM = "SHA-256";
    public static final String PACKAGE_TO_SCAN = "es.in2.blockchainconnector.domain";
    public static final String HASHLINK_PREFIX = "?hl=";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";
    public static final String ACCEPT_HEADER = "Accept";

    public static boolean isNullOrBlank(String string) {
        return string == null || string.isBlank();
    }

    public static String getRequest(String url) {
        // Create request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).headers(ACCEPT_HEADER, APPLICATION_JSON).GET().build();
        // Send request asynchronously
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        return response.thenApply(HttpResponse::body).join();
    }

    public static int getResponseCode(String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            return client.send(request, HttpResponse.BodyHandlers.discarding()).statusCode();
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RequestErrorException("Failed to get Response Code");
        }
    }

    public static String calculateSHA256Hash(String data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(SHA_256_ALGORITHM);
        byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    private static String bytesToHex(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
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

    public static String postRequest(String url, String requestBody) {
        // Create request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).headers(CONTENT_TYPE, APPLICATION_JSON, ACCEPT_HEADER, APPLICATION_JSON).POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
        // Send request asynchronously
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        return response.thenApply(HttpResponse::body).join();
    }

    public static String patchRequest(String url, String requestBody) {
        // Create request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers(CONTENT_TYPE, APPLICATION_JSON, ACCEPT_HEADER, APPLICATION_JSON)
                .method("PATCH", HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Send request asynchronously
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        // Wait for the response and return the body
        return response.thenApply(HttpResponse::body).join();
    }

    public static String extractIdFromJson(String jsonString) {
        try {
            // Crear un ObjectMapper de Jackson
            ObjectMapper objectMapper = new ObjectMapper();

            // Convertir la cadena JSON a un JsonNode
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            // Extraer el valor del campo "id" como una cadena
            return jsonNode.get("id").asText();
        } catch (Exception e) {
            e.printStackTrace(); // Manejar excepciones según tus necesidades
            return null;
        }
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

}
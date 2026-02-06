package com.dinopark.api;

import com.dinopark.core.Park;
import org.junit.jupiter.api.*;

import java.net.http.*;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DinoHttpServerIT {

    private DinoHttpServer server;
    private final HttpClient client = HttpClient.newHttpClient();

    @BeforeAll
    void startServer() throws Exception {
        Park.dinosaurs.clear();
        server = new DinoHttpServer();
        server.start();
        Thread.sleep(500);
    }

    @AfterAll
    void stopServer() {
    }

    @Test
    void testGetDinos() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/dinos"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Rex"));
    }

    @Test
    void testAddDino() throws Exception {
        String json = "{\"name\":\"Testosaur\",\"energy\":70,\"dangerLevel\":2}";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/dinos"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertTrue(response.body().contains("Testosaur"));
    }

    @Test
    void testFeedAll() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/feed"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("All dinos are fed!"));
    }
}

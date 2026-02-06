package com.dinopark.api;

import com.dinopark.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Map;

public class DinoHttpServer {

    private final DinoService service = new DinoService();

    public void start() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        ObjectMapper mapper = new ObjectMapper();

        server.createContext("/api/dinos", exchange -> {
            String response = mapper.writeValueAsString(Park.dinosaurs);
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        });

        server.createContext("/api/feed", exchange -> {
            service.feedAll();
            String response = "All dinos are fed!";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        });

        server.createContext("/api/dinos", exchange -> {
            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
                return;
            }

            InputStream is = exchange.getRequestBody();
            Map<String, Object> data = mapper.readValue(is, Map.class);

            String name = (String) data.get("name");
            int energy = (int) data.getOrDefault("energy", 100);
            int dangerLevel = (int) data.getOrDefault("dangerLevel", 5);

            service.addDinosaur(name, energy, dangerLevel);

            String response = "Dino " + name + " has been added!";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        });

        server.start();
        System.out.println("Server started on port 8080");
    }
}

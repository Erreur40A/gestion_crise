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
            // -------- Preflight (IMPORTANT) --------
            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                exchange.close();
                return;
            }
            String method = exchange.getRequestMethod();
            if (method.equalsIgnoreCase("GET")) {
                String response = mapper.writeValueAsString(Park.dinosaurs);
                exchange.sendResponseHeaders(200, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());

            } else if (method.equalsIgnoreCase("POST")) {
                InputStream is = exchange.getRequestBody();
                Map<String, Object> data = mapper.readValue(is, Map.class);

                String name = (String) data.get("name");
                int energy = (int) data.getOrDefault("energy", 100);
                int dangerLevel = (int) data.getOrDefault("dangerLevel", 5);
                String espece = (String) data.getOrDefault("espece", "unknown");

                service.addDinosaur(name, energy, dangerLevel, espece);

                String response = "Dino " + name + " has been added!";
                exchange.sendResponseHeaders(201, response.getBytes().length); // 201 Created
                exchange.getResponseBody().write(response.getBytes());

            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
            exchange.close();
        });

        server.createContext("/api/feed", exchange -> {
            service.feedAll();
            String response = "All dinos are fed!";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        });

        // GET - Liste des espèces disponibles
        server.createContext("/api/species", exchange -> {
            String response = mapper.writeValueAsString(DinoSpecies.SPECIES);

            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        });

        // POST - Retirer un dino
        server.createContext("/api/dinos/remove", exchange -> {
            // exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            // exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            // exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            // exchange.getResponseHeaders().add("Content-Type", "application/json");

            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                InputStream is = exchange.getRequestBody();

                Map<String, String> body = mapper.readValue(is, Map.class);
                String name = body.get("name");
                service.remove(name);

                System.out.println("Le dinosaure mort a été supprimé : " + name);
                String response = mapper.writeValueAsString(Park.dinosaurs);

                exchange.sendResponseHeaders(200, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
            }
        });

        // POST - Retirer tous les dinos
        server.createContext("/api/dinos/removeAll", exchange -> {

            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                service.removeAll();

                System.out.println("Tous les dinos morts ont été supprimés");
                String response = mapper.writeValueAsString(Park.dinosaurs);

                exchange.sendResponseHeaders(200, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
            }
        });

        server.start();
        System.out.println("Server started on port 8080");
    }
}


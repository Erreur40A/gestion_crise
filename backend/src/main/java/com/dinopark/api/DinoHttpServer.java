package com.dinopark.api;

import com.dinopark.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class DinoHttpServer {

    private DinoService ds = new DinoService();

    public void start() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // GET - Liste des dinos
        server.createContext("/api/dinos", exchange -> {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.getResponseHeaders().add("Content-Type", "application/json");

            if (exchange.getRequestMethod().equals("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            String response = mapper.writeValueAsString(Park.dinosaurs);

            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        });

        // GET - Liste des espèces disponibles
        server.createContext("/api/species", exchange -> {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Content-Type", "application/json");

            ObjectMapper mapper = new ObjectMapper();
            String response = mapper.writeValueAsString(DinoSpecies.SPECIES);

            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        });

        // POST - Ajouter un dino
        server.createContext("/api/dinos/add", exchange -> {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.getResponseHeaders().add("Content-Type", "application/json");

            if (exchange.getRequestMethod().equals("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if (exchange.getRequestMethod().equals("POST")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
                String body = reader.lines().collect(Collectors.joining());

                ObjectMapper mapper = new ObjectMapper();
                Dinosaur newDino = mapper.readValue(body, Dinosaur.class);

                // Récupérer le niveau de danger basé sur l'espèce
                newDino.dangerLevel = DinoSpecies.getDangerLevel(newDino.espece);

                Park.dinosaurs.add(newDino);

                System.out.println("✅ Nouveau dino ajouté : " + newDino.name + " (" + newDino.espece + ")");

                String response = mapper.writeValueAsString(newDino);
                exchange.sendResponseHeaders(201, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
            }
        });

        // POST - Nourrir tous les dinos
        server.createContext("/api/dinos/feed", exchange -> {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.getResponseHeaders().add("Content-Type", "application/json");

            if (exchange.getRequestMethod().equals("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if (exchange.getRequestMethod().equals("POST")) {
                ds.feedAll();
                System.out.println("🍖 Tous les dinos ont été nourris !");

                ObjectMapper mapper = new ObjectMapper();
                String response = mapper.writeValueAsString(Park.dinosaurs);
                exchange.sendResponseHeaders(200, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
            }
        });

        // POST - Retirer un dino
        server.createContext("/api/dinos/remove", exchange -> {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.getResponseHeaders().add("Content-Type", "application/json");

            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                InputStream is = exchange.getRequestBody();
                ObjectMapper mapper = new ObjectMapper();

                Map<String, String> body = mapper.readValue(is, Map.class);
                String name = body.get("name");
                ds.remove(name);

                System.out.println("Le dinosaure mort a été supprimé : " + name);
                String response = mapper.writeValueAsString(Park.dinosaurs);

                exchange.sendResponseHeaders(200, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
            }
        });

        // POST - Retirer tous les dinos
        server.createContext("/api/dinos/removeAll", exchange -> {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.getResponseHeaders().add("Content-Type", "application/json");

            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {

                Park.dinosaurs.removeIf(dino -> !dino.isAlive());

                System.out.println("Tous les dinos morts ont été supprimés");

                ObjectMapper mapper = new ObjectMapper();
                String response = mapper.writeValueAsString(Park.dinosaurs);

                exchange.sendResponseHeaders(200, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
            }
        });

        server.start();
        System.out.println("🦕 Serveur démarré sur http://localhost:8080");
    }
}
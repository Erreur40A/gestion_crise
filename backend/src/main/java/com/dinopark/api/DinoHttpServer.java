package com.dinopark.api;

import com.dinopark.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class DinoHttpServer {

    public void start() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/api/dinos", exchange -> {
                        // Headers CORS AVANT tout
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            
            ObjectMapper mapper = new ObjectMapper();
            String response = mapper.writeValueAsString(Park.dinosaurs);

            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        });

        server.start();
    }
}

package com.dinopark;

import com.dinopark.api.DinoHttpServer;
import com.dinopark.simulator.DinoSimulator;
import com.dinopark.core.DinoService;

public class Main {
    public static void main(String[] args) throws Exception {
        DinoService service = new DinoService();
        
        DinoHttpServer server = new DinoHttpServer();
        server.start();

        DinoSimulator simulator = new DinoSimulator();
        simulator.start();
    }
}

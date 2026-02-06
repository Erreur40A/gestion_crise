package com.dinopark.simulator;

import com.dinopark.core.*;

public class DinoSimulator extends Thread {

    public void run() {
        DinoService service = new DinoService();

        while (true) {
            for (Dinosaur d : Park.dinosaurs) {
                if(d.isAlive()){
                    d.hunger += 5;
                    d.energy -= 3;
                }
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

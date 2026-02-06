package com.dinopark.simulator;

import com.dinopark.core.*;

public class DinoSimulator extends Thread {

    public void run() {
        while (true) {
            for (Dinosaur d : Park.dinosaurs) {
                if (!d.isAlive()) {
                    continue;
                }

                d.adjustHunger(6);
                d.adjustEnergy(-3);
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

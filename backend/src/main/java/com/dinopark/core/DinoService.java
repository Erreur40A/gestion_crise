package com.dinopark.core;

public class DinoService {

    public DinoService() {
        if (Park.dinosaurs.isEmpty()) {
            Dinosaur d = new Dinosaur();
            d.name = "Rex";
            d.energy = 100;
            d.hunger = 0;
            d.dangerLevel = 5;
            Park.dinosaurs.add(d);
        }
    }

    public void feedAll() {
        for (Dinosaur d : Park.dinosaurs) {
            d.hunger -= 10;
            d.energy += 5;
        }
    }
}

package com.dinopark.core;

import java.util.Objects;

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
            if (d.isAlive() && d.energy < 100) {
                d.hunger -= 10;
                d.energy += 5;
                d.hunger = d.hunger < 0 ? 0 : d.hunger;
                d.energy = d.energy > 100 ? 100 : d.energy;
            }
        }
    }

    public void remove(String name) {
        Park.dinosaurs = Park.dinosaurs
                .stream()
                .filter(e -> !Objects.equals(e.name, name))
                .toList();
    }

}

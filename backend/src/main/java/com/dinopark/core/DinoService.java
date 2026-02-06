package com.dinopark.core;

public class DinoService {
    public static final int HungerAdjustment = -100;
    public static final int EnergyAdjustment = 50;
    public DinoService() {
        if (Park.dinosaurs.isEmpty()) {
            addDinosaur("Rex", 100, 5);
        }
    }

    public void feedAll() {
        for (Dinosaur d : Park.dinosaurs) {
            d.adjustHunger(HungerAdjustment);
            d.adjustEnergy(EnergyAdjustment);
        }
    }

    public void addDinosaur(String name, int energy, int dangerLevel) {
        Dinosaur d = new Dinosaur();
        d.name = name;
        d.energy = energy;
        d.hunger = 0;
        d.dangerLevel = dangerLevel;
        Park.dinosaurs.add(d);
    }
}

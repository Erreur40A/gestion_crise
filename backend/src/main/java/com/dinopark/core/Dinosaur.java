package com.dinopark.core;

public class Dinosaur {
    public String name;
    public int energy;
    public int hunger;
    public int dangerLevel;

    private static final int maxEnergy = 300;

    public boolean isAlive() {
        return energy > 0;
    }

    public void adjustEnergy(int delta) {
        energy += delta;
        if (energy < 0)  {
            energy = 0;
        }
        if (energy > maxEnergy)  {
            energy = maxEnergy;
        }
    }

    public void adjustHunger(int delta) {
        hunger += delta;
        if (hunger < 0)  {
            hunger = 0;
        }
    }
}

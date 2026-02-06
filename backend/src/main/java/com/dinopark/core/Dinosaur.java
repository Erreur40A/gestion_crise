package com.dinopark.core;

public class Dinosaur {
    public String espece; //ajout attribut espèce pour avoir plusieurs dinos de la même espèce
    public String name;
    public int energy;
    public int hunger;
    public int dangerLevel;

    public boolean isAlive() {
        return energy > 0;
    }
}

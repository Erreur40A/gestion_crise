package com.dinopark.core;

import java.util.Random;

public class DinoService {
    
    private Random random = new Random();
    private String[] noms = {
        "Rex", "Blue", "Delta", "Charlie", "Echo", "Rexy", "Buck", "Doe",
        "Junior", "Big Al", "Tiny", "Spike", "Chomper", "Littlefoot",
        "Cera", "Ducky", "Petrie", "Sarah", "Topsy", "Trixie",
        "Aladar", "Neera", "Kron", "Bruton", "Yar"
    };

    public DinoService() {
        if (Park.dinosaurs.isEmpty()) {
            initializePark();
        }
    }
    
    private void initializePark() {
        String[] species = DinoSpecies.SPECIES.keySet().toArray(new String[0]);
        
        // Créer 20 dinosaures avec des espèces variées
        for (int i = 0; i < 20; i++) {
            Dinosaur d = new Dinosaur();
            d.espece = species[random.nextInt(species.length)];
            d.name = noms[i % noms.length] + (i > noms.length ? " " + (i / noms.length) : "");
            d.energy = 80 + random.nextInt(40); // Entre 80 et 120
            d.hunger = random.nextInt(30); // Entre 0 et 30
            d.dangerLevel = DinoSpecies.getDangerLevel(d.espece);
            
            Park.dinosaurs.add(d);
            System.out.println("🦕 Créé : " + d.name + " (" + d.espece + ") - Danger: " + d.dangerLevel);
        }
    }

    public void feedAll() {
        for (Dinosaur d : Park.dinosaurs) {
            d.hunger = Math.max(0, d.hunger - 20); // La faim ne peut pas être négative
            d.energy = Math.min(100, d.energy + 10); // L'énergie max est 100
        }
    }
}

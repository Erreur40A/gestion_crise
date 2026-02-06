package com.dinopark.core;

import java.util.HashMap;
import java.util.Map;

public class DinoSpecies {
    
    public static final Map<String, Integer> SPECIES = new HashMap<>();
    
    static {
        // Carnivores dangereux
        SPECIES.put("Tyrannosaurus Rex", 10);
        SPECIES.put("Velociraptor", 9);
        SPECIES.put("Spinosaurus", 9);
        SPECIES.put("Allosaurus", 8);
        SPECIES.put("Carnotaurus", 8);
        
        // Carnivores moyens
        SPECIES.put("Dilophosaurus", 7);
        SPECIES.put("Compsognathus", 4);
        
        // Herbivores dangereux (à cause de leur taille)
        SPECIES.put("Triceratops", 6);
        SPECIES.put("Stegosaurus", 5);
        SPECIES.put("Ankylosaurus", 6);
        
        // Herbivores géants
        SPECIES.put("Brachiosaurus", 4);
        SPECIES.put("Diplodocus", 3);
        SPECIES.put("Apatosaurus", 3);
        
        // Herbivores pacifiques
        SPECIES.put("Parasaurolophus", 2);
        SPECIES.put("Gallimimus", 1);
    }
    
    public static int getDangerLevel(String espece) {
        return SPECIES.getOrDefault(espece, 5);
    }
}
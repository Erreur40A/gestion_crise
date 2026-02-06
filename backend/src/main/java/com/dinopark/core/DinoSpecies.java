package com.dinopark.core;

import java.util.HashMap;
import java.util.Map;

public class DinoSpecies {
    
    public static final Map<String, Integer> SPECIES = new HashMap<>();
    public static final Map<String, String> SPECIES_IMAGES = new HashMap<>();
    
    static {
        // Carnivores dangereux
        SPECIES.put("TyrannosaurusRex", 10);
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

    static {
        // Carnivores dangereux
        SPECIES_IMAGES.put("TyrannosaurusRex", "TyranosaurusRex.jpeg");
        SPECIES_IMAGES.put("Velociraptor", "Velociraptor.jpeg");
        SPECIES_IMAGES.put("Spinosaurus", "spinosaurus.jpeg");
        SPECIES_IMAGES.put("Allosaurus", "allosaurus.jpeg");
        SPECIES_IMAGES.put("Carnotaurus", "carnotaurus.jpeg");
        
        // Carnivores moyens
        SPECIES_IMAGES.put("Dilophosaurus", "dilophosaurus.jpeg");
        SPECIES_IMAGES.put("Compsognathus", "compsognathus.jpeg");
        
        // Herbivores dangereux (à cause de leur taille)
        SPECIES_IMAGES.put("Triceratops", "triceratops.jpeg");
        SPECIES_IMAGES.put("Stegosaurus", "stegosaurus.jpeg");
        SPECIES_IMAGES.put("Ankylosaurus", "ankylosaurus.jpeg");
        
        // Herbivores géants
        SPECIES_IMAGES.put("Brachiosaurus", "brachiosaurus.jpeg");
        SPECIES_IMAGES.put("Diplodocus", "diplodocus.jpeg");
        SPECIES_IMAGES.put("Apatosaurus", "apatosaurus.jpeg");
        
        // Herbivores pacifiques
        SPECIES_IMAGES.put("Parasaurolophus", "parasaurolophus.jpeg");
        SPECIES_IMAGES.put("Gallimimus", "gallimimus.jpeg");
    }
    
    public static int getDangerLevel(String espece) {
        return SPECIES.getOrDefault(espece, 5);
    }

    public static String getImage(String espece) {
        return SPECIES_IMAGES.getOrDefault(espece, "");
    }
}
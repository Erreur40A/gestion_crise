package com.dinopark.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DinoServiceTest {

    private DinoService service;

    @BeforeEach
    void setup() {
        Park.dinosaurs.clear();
        service = new DinoService();
    }

    @Test
    void testAddDinosaur() {
        int initialSize = Park.dinosaurs.size();
        service.addDinosaur("Triceratops", 80, 3, "Triceratops");

        assertEquals(initialSize + 1, Park.dinosaurs.size());

        Dinosaur d = Park.dinosaurs.get(Park.dinosaurs.size() - 1);
        assertEquals("Triceratops", d.name);
        assertEquals(80, d.energy);
        assertEquals(0, d.hunger);
        assertEquals(3, d.dangerLevel);
    }

    @Test
    void testFeedAll() {
        Dinosaur d = new Dinosaur();
        int initialEnergy = 200;
        int initialHunger = 200;
        d.name = "Testosaur";
        d.energy = initialEnergy;
        d.hunger = initialHunger;
        d.dangerLevel = 4;
        Park.dinosaurs.add(d);

        service.feedAll();

        assertEquals(initialEnergy + DinoService.EnergyAdjustment, d.energy);
        assertEquals(initialHunger + DinoService.HungerAdjustment, d.hunger);
    }

    @Test
    void testEnergyCannotGoBelowZero() {
        Dinosaur d = new Dinosaur();
        d.name = "Weakling";
        d.energy = 2;
        d.hunger = 0;
        d.dangerLevel = 1;
        Park.dinosaurs.add(d);

        d.adjustEnergy(-5);

        assertEquals(0, d.energy);
        assertFalse(d.isAlive());
    }

    @Test
    void testHungerCannotGoBelowZero() {
        Dinosaur d = new Dinosaur();
        d.name = "Fullosaur";
        d.energy = 10;
        d.hunger = 5;
        d.dangerLevel = 1;
        Park.dinosaurs.add(d);

        d.adjustHunger(-10);

        assertEquals(0, d.hunger);
    }
}

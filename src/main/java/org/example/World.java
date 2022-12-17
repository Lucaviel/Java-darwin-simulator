package org.example;

import java.util.Random;

public class World {
    public static void main(String[] args) {
        Animal pet1 = new Animal();
        Animal pet2 = new Animal();
        System.out.println(pet1.getGenotype());
        System.out.println(pet2.getGenotype());
        pet1.changeOrientation();
        Animal pet3 = new Animal(pet1, pet2);
        System.out.println(pet3.getGenotype());
    }
}
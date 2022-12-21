package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulator {
    protected static final int GENE_LENGTH = 8;
    protected int day = 0;

    protected List<Animal> animals = new ArrayList<Animal>();

    IWorldMap map;

    public void addRandomAnimals(IWorldMap map, int initAnimalsNumber) {
        int i = 0;
        Random rand = new Random();
        while (i < initAnimalsNumber) {
            int x = rand.nextInt(map.getWidth());
            int y = rand.nextInt(map.getHeight());
            if (!(map.objectAt(new Vector2d(x, y)) instanceof Animal)) {

                Animal sampleAnimal = new Animal(map, new Vector2d(x, y));
                if (map.place(sampleAnimal)) {
                    this.animals.add(sampleAnimal);
                    i++;
                }
            }
        }
    }
    public Simulator(){
        for(int i=0; i<10; i++){
            //delete dead animals
            //changeOrientation();
            //eating grass
            //reproduction
            //grass growing
            //animal decreasing energy
            this.day++;
        }
    }
}

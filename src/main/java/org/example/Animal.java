package org.example;

import java.util.ArrayList;

public class Animal extends Simulator{
    protected int energy;
    protected MapDirection orientation;
    protected int days;
    protected int children;
    protected Genotype gene;
    protected Vector2d position;
    protected int currentGene;


    public Animal(){
        this.orientation = MapDirection.NORTH;
        this.days = 0;
        this.children = 0;
        this.position = new Vector2d(2,2);
        this.energy = 20;
        this.gene = new Genotype(GENE_LENGTH);
        this.currentGene = 0;
    }

    public Animal(Vector2d initialPosition, Animal parent1, Animal parent2, IWorldMap map, int birthdate){
        this.orientation = MapDirection.NORTH;
        this.days = 0;
        this.children = 0;
        this.position = parent1.getPosition();
        this.energy = parent1.getEnergy() / 4 + parent2.getEnergy() / 4;
        this.gene = new Genotype(GENE_LENGTH, parent1, parent2);
        this.currentGene = 0;
    }

    public void changeOrientation(){
        int n = gene.getCurrentGenotype(this.currentGene);
        for(int i=0; i<n; i++)
            this.orientation = this.orientation.next();
        move();
    }

    public void move() {
        this.position = this.position.add(this.orientation.toUnitVector());
        if (this.currentGene == GENE_LENGTH-1)
            this.currentGene = 0;
        else this.currentGene++;
    }

    public ArrayList<Integer> getGenotype(){
        return gene.getGenotype();
    }

    public int getEnergy(){
        return this.energy;
    }

    public int howOld(){
        return this.days;
    }

    public int getChildren(){
        return this.children;
    }

    public Vector2d getPosition(){
        return this.position;
    }
}

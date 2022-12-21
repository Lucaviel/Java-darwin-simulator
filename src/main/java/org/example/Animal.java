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
    IWorldMap map;


    public Animal(IWorldMap map, Vector2d randomPosition){
        this.orientation = MapDirection.NORTH;
        this.days = 0;
        this.children = 0;
        this.position = randomPosition;
        this.energy = 20;
        this.gene = new Genotype(GENE_LENGTH);
        this.currentGene = 0;
        this.map= map;
    }

    public Animal(IWorldMap map, Animal parent1, Animal parent2){
        this.orientation = MapDirection.NORTH;
        this.days = 0;
        this.children = 0;
        this.position = parent1.getPosition();
        this.energy = parent1.getEnergy() / 4 + parent2.getEnergy() / 4;
        this.gene = new Genotype(parent1, parent2);
        this.currentGene = 0;
        this.map = map;
    }

    public void changeOrientation(int n){
//        int n = gene.getCurrentGenotype(this.currentGene);
        for(int i=0; i<n; i++)
            this.orientation = this.orientation.next();
        this.changeEnergy(-2);
    }

    public void move() {
        this.position = this.position.add(this.orientation.toUnitVector());
        map.moveTo(this);
        if (this.currentGene == GENE_LENGTH-1)
            this.currentGene = 0;
        else this.currentGene++;
    }

    public String toString() {
        return switch(this.orientation) {
            case NORTH -> "N";
            case NORTHEAST -> "NE";
            case EAST -> "E";
            case SOUTHEAST -> "SE";
            case SOUTH -> "S";
            case SOUTHWEST -> "SW";
            case WEST -> "W";
            case NORTHWEST -> "NW";
        };
    }

    public ArrayList<Integer> getGenotype(){
        return gene.getGenotype();
    }

    public Integer getGenotypeInt()
    {
        ArrayList<Integer> g = getGenotype();
        String gString = "";
        for(int i = 0; i < g.size(); i++){
            gString = gString + g.get(i).toString();
        }
        return Integer.valueOf(gString);
    }

    public int getEnergy(){
        return this.energy;
    }

    public boolean isEnergyMoreThan(int n)
    {
        return (this.getEnergy()>=n);
    }

    public boolean isEnergyLessThan(int n)
    {
        return (this.getEnergy()<=n);
    }

    public void changeEnergy(int energy){
        this.energy = this.energy + energy;
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

package org.example;

import java.util.ArrayList;
import java.util.List;

public class Animal{
    protected int energy;
    protected MapDirection orientation;
    protected int days;
    protected int children;
    protected Genotype gene;
    protected Vector2d position;
    protected int currentGene;
    protected static int GENE_LENGTH = 8;
    IWorldMap map;
    private List<IPositionObserver> observers = new ArrayList<>();


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
        for(int i=0; i<n; i++)
            this.orientation = this.orientation.next();
    }

    public void move() {
        this.position = this.position.add(this.orientation.toUnitVector());
        map.moveTo(this);
        if (this.currentGene == GENE_LENGTH-1)
            this.currentGene = 0;
        else this.currentGene++;
        this.changeEnergy(-2);
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

    public int getEnergy(){
        return this.energy;
    }

    public void changeDays(){
        this.days++;
    }
    public void changeChildren(){
        this.children++;
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

    public String Visualize() {
        return "src/main/resources/shrek.png";
    }

    public void addObserver(IPositionObserver observer){
        observers.add(observer);
    }

    public void removeObserver(IPositionObserver observer){
        this.observers.remove(observer);
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        for (IPositionObserver observer : observers) observer.positionChanged(this,oldPosition, newPosition);
    }
}

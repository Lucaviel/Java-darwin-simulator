package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

public class Animal{
    protected int energy;
    protected Direction orientation;
    protected int days;
    protected int children;
    protected Genotype gene;
    protected Vector2d position;
    protected int currentGene;
    protected int geneLength;
    IWorldMap map;
    private List<IPositionChangeObserver> observers = new ArrayList<>();


    public Animal(IWorldMap map, Vector2d randomPosition, int startEnergy, int geneLength){
        this.orientation = Direction.NORTH;
        this.days = 0;
        this.children = 0;
        this.position = randomPosition;
        this.energy = startEnergy;
        this.geneLength = geneLength;
        this.gene = new Genotype(geneLength);
        this.currentGene = 0;
        this.map= map;
    }

    public Animal(IWorldMap map, Animal parent1, Animal parent2, int minMutate, int maxMutate){
        this.orientation = Direction.NORTH;
        this.days = 0;
        this.children = 0;
        this.position = parent1.getPosition();
        this.energy = parent1.getEnergy() / 4 + parent2.getEnergy() / 4;
        this.gene = new Genotype(parent1, parent2, minMutate, maxMutate);
        this.currentGene = 0;
        this.map = map;
    }

    public void changeOrientation(int n){
        for(int i=0; i<n; i++)
            this.orientation = this.orientation.next();
    }

    public void move(int energyLoss) {
        changeOrientation(this.gene.getCurrentGenotype(this.currentGene));
        this.position = this.position.add(this.orientation.toUnitVector());
        map.moveTo(this);
        if (this.currentGene == geneLength-1)
            this.currentGene = 0;
        else this.currentGene++;
        this.changeEnergy(energyLoss);
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
        return "src/main/resources/animal.png";
    }

    public void addObserver(IPositionChangeObserver observer){
        observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer){
        this.observers.remove(observer);
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        for (IPositionChangeObserver observer : observers) observer.positionChanged(this,oldPosition, newPosition);
    }
}

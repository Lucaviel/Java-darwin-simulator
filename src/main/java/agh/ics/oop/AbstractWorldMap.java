package agh.ics.oop;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver{

    protected int width;
    protected int height;
    protected int teleportEnergy;
    protected Vector2d lowerLeft;
    protected Vector2d upperRight;
    protected Map<Vector2d, ArrayList<Animal>> animals = new LinkedHashMap<>();
    protected Map<Vector2d, Grass> grasses = new LinkedHashMap<>();


    public AbstractWorldMap(int width, int height, int teleportEnergy){
        this.width = width;
        this.height = height;
        this.teleportEnergy = teleportEnergy;
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width-1, height-1);
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
            {
                animals.put(new Vector2d(x, y), new ArrayList<>());
            }
    }

    public Object objectAt(Vector2d position) {
        if (!animals.get(position).isEmpty() && animals.get(position).size() > 0){
            ArrayList<Animal> animalsOnThisField = animals.get(position);
            return animalsOnThisField.get(0);}
        else if (grasses.get(position) != null) return grasses.get(position);
        else return null;
    }

    public boolean place(Animal animal) {
        Vector2d newAnimalPosition = animal.getPosition();
        if (newAnimalPosition.follows(lowerLeft) && newAnimalPosition.precedes(upperRight)){
            this.animals.get(newAnimalPosition).add(animal);
            animal.addObserver(this);
            return true;
        }
        return false;
    }

    public boolean delete(Animal animal){
        for (int i = 0; i < animals.get(animal.getPosition()).size(); i++) {
            if (animals.get(animal.getPosition()).get(i) == animal) {
                animals.get(animal.getPosition()).get(i).removeObserver(this);
                animals.get(animal.getPosition()).remove(i);
                return true;
            }
        }
        return false;
    }

    public Map<Vector2d, ArrayList<Animal>> getAnimals() {
        return animals;
    }
    public Map<Vector2d, Grass> getGrasses(){
        return grasses;
    }
    public void removeGrass(Vector2d position){
        grasses.remove(position);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public boolean isOccupied(Vector2d position) {
        return false;
    }

    public String toString(){
        MapVisualizer mapVisualisation = new MapVisualizer(this);
        return mapVisualisation.draw(lowerLeft, upperRight);
    }

    public void positionChanged(Animal animal,Vector2d oldPosition, Vector2d newPosition) {
        ArrayList<Animal> animalsOnCurrentPosition = animals.get(oldPosition);
        for (int i = 0; i< animalsOnCurrentPosition.size(); i++){
            if (animalsOnCurrentPosition.get(i) == animal){
                animals.get(oldPosition).remove(i); // i
                animals.get(newPosition).add(animal);
            }
        }
    }

    public boolean isOccupiedByGrass(Vector2d position) {
        return grasses.containsKey(position);
    }

    public void addGrass(Vector2d position){
        grasses.put(position,new Grass(position));
    }

    public int getNumOfGrasses(){
        return grasses.size();
    }

}
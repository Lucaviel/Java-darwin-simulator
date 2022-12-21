package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

abstract public class AbstractWorld implements IWorldMap{

    protected int width;
    protected int height;
    protected Vector2d lowerLeft;
    protected Vector2d upperRight;
    protected Map<Vector2d, ArrayList<Animal>> animals = new LinkedHashMap<>();
    protected Map<Vector2d, Grass> grasses = new LinkedHashMap<>();


    public AbstractWorld(int width, int height){
        this.width = width;
        this.height = height;
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width, height);
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
            {
                animals.put(new Vector2d(x, y), new ArrayList<>());
            }
    }

    Comparator<Animal> compareGenotype = new Comparator<Animal>() {

        @Override
        public int compare(Animal a1, Animal a2) {
            if (a1.getGenotypeInt() > a2.getGenotypeInt()) return 1;
            if (a2.getGenotypeInt() > a1.getGenotypeInt()) return -1;
            return 0;
        }
    };

    public Object objectAt(Vector2d position) {
        if (!animals.get(position).isEmpty() && animals.get(position).size() > 0){
            ArrayList<Animal> animalsOnThisField = animals.get(position);
            animalsOnThisField.sort(compareGenotype.reversed());
            return animalsOnThisField.get(0);}

        else if (grasses.get(position) != null) return grasses.get(position);
        else return null;
    }

    public boolean place(Animal animal) {
        Vector2d newAnimalPosition = animal.getPosition();
        if (newAnimalPosition.follows(lowerLeft) && newAnimalPosition.precedes(upperRight)){
            this.animals.get(newAnimalPosition).add(animal);
            this.animals.get(newAnimalPosition).sort(compareGenotype.reversed());
            return true;
        }
        return false;
    }


}

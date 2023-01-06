package agh.ics.oop;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Statistics {
    public Simulator darwin;
    public List<Animal> animals;
    public IWorldMap map;

    public Statistics(Simulator darwin){
        this.darwin = darwin;
        this.animals = darwin.getSymAnimals();
        this.map = darwin.map;
    }

    public int avgEnergy() {
        int sum = 0;
        for (Animal animal : animals) sum = sum + animal.getEnergy();
        if (darwin.numOfAnimals() == 0) return 0;
        return sum / darwin.numOfAnimals();
    }

    public int getAvgLifeSpan() {
        if (darwin.deadAnimalsNum == 0) return 0;
        return darwin.summedLifeSpan / darwin.deadAnimalsNum;
    }

    public int freeFieldsNum(){
        int freeFields = 0;
        for (int x = 0; x < map.getWidth(); x++)
            for (int y = 0; y < map.getHeight(); y++) {
                ArrayList<Animal> animalsAtVector = map.getAnimals().get(new Vector2d(x, y));
                if (animalsAtVector.isEmpty())
                    if (map.getGrasses().get(new Vector2d(x, y)) == null)
                        freeFields++;
            }
        return freeFields;
    }

    public String dominantGenome() {
        if (animalsWithDominantGenome() == null) return "";
        if (animalsWithDominantGenome().size() == 0) return "";
        String result = animalsWithDominantGenome().get(0).getGenotype().toString();
        return result.replaceAll(",", "").replaceAll(" ", "");
    }

    public ArrayList<Animal> animalsWithDominantGenome() {
        Map<ArrayList<Integer>, Integer> genotypes = new LinkedHashMap<>();
        for (Animal animal : animals) {
            if (genotypes.get(animal.getGenotype()) != null) {
                int i = genotypes.get(animal.getGenotype());
                genotypes.remove(animal.getGenotype());
                genotypes.put(animal.getGenotype(), i + 1);
            } else genotypes.put(animal.getGenotype(), 1);
        }
        int val = 0;
        ArrayList<Integer> genome = new ArrayList<>();
        ArrayList<Animal> result = new ArrayList<>();

        for (Map.Entry<ArrayList<Integer>, Integer> el : genotypes.entrySet()) {
            if (el.getValue() > val) {
                genome = el.getKey();
                val = el.getValue();
            }
        }

        for (Animal animal : animals) {
            if (animal.getGenotype().equals(genome)) result.add(animal);
        }

        return result;
    }
}

package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulator implements IEngine,Runnable{
    protected int day = 0;
    protected final int DAYS = 10;
    protected int startAnimals;
    protected int dailyLossEnergy;
    protected int geneLength;
    protected int numGrass;
    protected int startEnergy;
    protected int reproductionEnergy;
    protected int minMutate;
    protected int maxMutate;
    protected List<Animal> animals = new ArrayList<Animal>();
    private final Random generator = new Random();
    protected IWorldMap map;

    public Simulator(IWorldMap map, int startAnimals, int startEnergy, int numGrass, int geneLength,
                     int dailyLossEnergy, int reproductionEnergy, int minMutate, int maxMutate){
        this.startAnimals = startAnimals;
        this.startEnergy = startEnergy;
        this.numGrass = numGrass;
        this.geneLength = geneLength;
        this.dailyLossEnergy = dailyLossEnergy;
        this.reproductionEnergy = reproductionEnergy;
        this.minMutate = minMutate;
        this.maxMutate = maxMutate;
        this.map = map;
            //eating grass
            //reproduction
            //grass growing
        addRandomAnimals(map, startAnimals);
        addRandomGrass(numGrass);
//        run();
    }

    public void run(){
        for(int i=0; i<DAYS; i++) {
            deleteAnimals();
            for (int j = 0; j < animals.size(); j++) {
                Vector2d prev = animals.get(i).getPosition();
                animals.get(i).move(dailyLossEnergy);
                Vector2d now = animals.get(i).getPosition();
                animals.get(i).positionChanged(prev, now);
                animals.get(i).changeDays();
            }
            eatingAndReproduction();
            this.day++;
        }
    }

    public void addRandomAnimals(IWorldMap map, int initAnimalsNumber) {
        int i = 0;
        Random rand = new Random();
        while (i < initAnimalsNumber) {
            int x = rand.nextInt(map.getWidth());
            int y = rand.nextInt(map.getHeight());
            Animal sampleAnimal = new Animal(map, new Vector2d(x, y), startEnergy, geneLength);
            if (map.place(sampleAnimal)) {
                this.animals.add(sampleAnimal);
                i++;
            }
        }
    }
    private void newBornAnimal(Animal parent1, Animal parent2) {
        if ((parent1.isEnergyMoreThan(reproductionEnergy)) && (parent2.isEnergyMoreThan(reproductionEnergy))){
            parent1.changeChildren();
            parent2.changeChildren();
            Animal kid = new Animal(map,parent1,parent2,minMutate,maxMutate);
            map.place(kid);
            this.animals.add(kid);
        }
    }


    public void deleteAnimals(){
        for (int i = 0; i < animals.size(); i++) {
            if (animals.get(i).getEnergy() <= 0) {
                animals.remove(animals.get(i));
                map.delete(animals.get(i));
            }
        }
    }

    public void eatingAndReproduction(){
        Random rand = new Random();
        for (int x = 0; x < map.getWidth(); x++)
            for (int y = 0; y < map.getHeight(); y++) {
                if (!map.getAnimals().get(new Vector2d(x, y)).isEmpty()) {
                    int highestEnergy = map.getAnimals().get(new Vector2d(x, y)).get(0).getEnergy();
                    int howManyOfHiEn = 0;
                    for (Animal animal : map.getAnimals().get(new Vector2d(x, y))) {
                        if (animal.getEnergy() == highestEnergy) howManyOfHiEn++;
                    }
                    //eating
//                    if (map.getGrasses().get(new Vector2d(x, y)) != null) {
//                        if (howManyOfHiEn == 1) map.getAnimals().get(new Vector2d(x,y)).get(0).setEnergy(highestEnergy
//                                + plantEnergy);
//                        else {
//                            for (Animal animal : map.getAnimals().get(new Vector2d(x, y))){
//                                if (animal.getEnergy() == highestEnergy) animal.setEnergy(highestEnergy + plantEnergy);
//                            }
//                        }
//                        map.removeGrass(new Vector2d(x,y));
//                    }

                    //reproduction
                    if (map.getAnimals().get(new Vector2d(x,y)).size() > 1){
                        if ((howManyOfHiEn == 1) && (map.getAnimals().get(new Vector2d(x,y)).size() == 2)){
                            Animal parent1 = map.getAnimals().get(new Vector2d(x,y)).get(0);
                            Animal parent2 = map.getAnimals().get(new Vector2d(x,y)).get(1);
                            newBornAnimal(parent1, parent2);
                        }
                        else{
                            int p1 = 0;
                            int p2 = 0;
                            do {
                                p1 = rand.nextInt(howManyOfHiEn);
                                p2 = rand.nextInt(howManyOfHiEn);
                            } while (p1 != p2);

                            Animal parent1 = map.getAnimals().get(new Vector2d(x,y)).get(p1);
                            Animal parent2 = map.getAnimals().get(new Vector2d(x,y)).get(p2);
                            newBornAnimal(parent1, parent2);

                        }

                    }
                }
            }
    }

    public void addRandomGrass(int initGrassNumber) {
        int i = 0;
        Random rand = new Random();
        int x = 0;
        int y = 0;

        while (i < initGrassNumber) { //80% rownik
            int drawNumber = generator.nextInt(10)+1; //losowanie od 1 do 10
            if (drawNumber <= 8){
                x = rand.nextInt(map.getWidth()+1);
                y = (int) (rand.nextInt((int) (map.getHeight()*0.2))+(0.4*map.getHeight())+1);
            }
            else{ //20% w innym miejscu
                x = rand.nextInt(map.getWidth());
                int randd = (generator.nextInt(2)); //losowanie od 1 do 2
                if(randd==0){
                    y = rand.nextInt((int) (map.getHeight()*0.4)+1);
                }
                else{
                    y = (int) (rand.nextInt((int) (map.getHeight()*0.4))+(0.6*map.getHeight())+1);
                }
            }

            if (!(map.isOccupiedByGrass(new Vector2d(x, y)))) {
                map.addGrass(new Vector2d(x,y));
                i++;
            }
        }
    }
}

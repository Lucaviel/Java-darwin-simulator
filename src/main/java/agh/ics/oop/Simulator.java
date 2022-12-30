package agh.ics.oop;

import java.util.*;

public class Simulator implements IEngine,Runnable{
    protected int day = 0;
    protected final int DAYS = 6;
    protected int startAnimals;
    protected int dailyLossEnergy;
    protected int geneLength;
    protected int numGrass;
    protected int startEnergy;
    protected int reproductionEnergy;
    protected int minMutate;
    protected int maxMutate;
    protected List<Animal> animals = new ArrayList<Animal>();
    List<Vector2d> equatorGrass = new ArrayList<Vector2d>();
    List<Vector2d> underEquatorGrass = new ArrayList<Vector2d>();
    List<Vector2d> upEquatorGrass = new ArrayList<Vector2d>();
    private final Random generator = new Random();
    protected IWorldMap map;
    protected int startGrass;
    protected int plantEnergy = 5;
    protected Statistics statistics;

    public Simulator(IWorldMap map, int startAnimals, int startEnergy, int numGrass, int geneLength,
                     int dailyLossEnergy, int reproductionEnergy, int maxMutate, int startGrass){
        this.startAnimals = startAnimals;
        this.startEnergy = startEnergy;
        this.numGrass = numGrass;
        this.geneLength = geneLength;
        this.dailyLossEnergy = dailyLossEnergy;
        this.reproductionEnergy = reproductionEnergy;
        this.maxMutate = maxMutate;
        this.startGrass = startGrass;
        this.map = map;
        this.equatorGrass = grassGenerator((int) (0.4*map.getHeight()), (int) (0.6*map.getHeight()));
        this.underEquatorGrass = grassGenerator(0, (int) (0.4*map.getHeight()));
        this.upEquatorGrass = grassGenerator((int) (0.6*map.getHeight()), map.getHeight());
        addRandomAnimals(map, startAnimals);
        addRandomGrass(startGrass);
        this.statistics = new Statistics(this.map);
    }

    public void run(){
        for(int i=0; i<DAYS; i++) {

            deleteAnimals();
            System.out.println("przed jedzeniu");
            System.out.println(map.getAnimals());
            System.out.println(map.getGrasses());
            eatingAndReproduction();
            System.out.println("po jedzeniu");
            System.out.println(map.getAnimals());
            System.out.println(map.getGrasses());
            for (int j = 0; j < animals.size(); j++) {
                Vector2d prev = animals.get(j).getPosition();
                animals.get(j).move(dailyLossEnergy);
                Vector2d now = animals.get(j).getPosition();
                animals.get(j).positionChanged(prev, now);
                animals.get(j).changeDays();
            }
            addRandomGrass( 3);
            this.day++;
        }
    }

    public int countAnimals(){
        return animals.size();
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

    public void addSpecificAnimal(Animal animal) {
        this.animals.add(animal);
        this.map.place(animal);
    }

    private void newBornAnimal(Animal parent1, Animal parent2) {
        if ((parent1.isEnergyMoreThan(reproductionEnergy)) && (parent2.isEnergyMoreThan(reproductionEnergy))){
            parent1.changeChildren();
            parent2.changeChildren();
            Animal kid = new Animal(map,parent1,parent2,maxMutate);
            map.place(kid);
            animals.add(kid);
        }
    }


    public void deleteAnimals(){
        for (int i = 0; i < animals.size(); i++) {
            if (animals.get(i).getEnergy() <= 0) {
                map.delete(animals.get(i));
            }
        }

        for (Animal animal : animals){
            if (animal.getEnergy() <= 0){ map.delete(animal);}
        }

        animals.removeIf(animal -> animal.getEnergy() <= 0);

    }

    public void sortAnimals(ArrayList<Animal> animalsUnsorted){

        Comparator<Animal> compareByMany = Comparator.comparing(Animal::getEnergy)
                .thenComparing(Animal::howOld).thenComparing(Animal::getChildren).reversed();

        animalsUnsorted.sort(compareByMany);

    }

    public void eatingAndReproduction(){
        for (int x = 0; x < map.getWidth(); x++)
            for (int y = 0; y < map.getHeight(); y++) {
                ArrayList<Animal> animalsAtVector = map.getAnimals().get(new Vector2d(x, y));
                if (!animalsAtVector.isEmpty()) {
                    sortAnimals(animalsAtVector);
                    Animal bestAnimal = animalsAtVector.get(0);
                    //eating
                    if (map.getGrasses().get(new Vector2d(x, y)) != null) {
                        bestAnimal.changeEnergy(plantEnergy);
                        map.removeGrass(new Vector2d(x,y));
                        updateGrass(x,y); /**/
                    }

                    //reproduction
                    if (animalsAtVector.size() > 1){
                            Animal parent1 = bestAnimal;
                            Animal parent2 = animalsAtVector.get(1);
                            newBornAnimal(parent1, parent2);
                        }
                    }
                }
            }

    public void addRandomGrass(int initGrassNumber) {
        int i = 0;
        int drawNumber = generator.nextInt(10)+1; //losowanie od 1 do 10

        while (i < initGrassNumber) {
            if (equatorGrass.size() == 0 && underEquatorGrass.size() == 0 && upEquatorGrass.size() == 0)
                break;
            if (drawNumber <= 8){ //80% prawdopodobieństwa na równik /**/
                if(equatorGrass.size() != 0) {
                    if(equatorGrass.size() == 1)
                        drawNumber = 0;
                    else drawNumber = generator.nextInt(equatorGrass.size() - 1);
                    map.addGrass(equatorGrass.get(drawNumber));
                    equatorGrass.remove(drawNumber);
                    i++;
                }
                else{
                    drawNumber = generator.nextInt(9,11);
                    continue;
                }
            }
            else{ //20% w innym miejscu
                if(drawNumber==9){
                    if(underEquatorGrass.size() != 0) {
                        if(underEquatorGrass.size() == 1)
                            drawNumber = 0;
                        else drawNumber = generator.nextInt(underEquatorGrass.size() - 1);
                        map.addGrass(underEquatorGrass.get(drawNumber));
                        underEquatorGrass.remove(drawNumber);
                        i++;
                    }
                    else{
                        drawNumber = 10;
                        continue;
                    }
                }
                else{
                    if(upEquatorGrass.size() != 0) {
                        if(upEquatorGrass.size() == 1)
                            drawNumber = 0;
                        else drawNumber = generator.nextInt(upEquatorGrass.size() - 1);
                        map.addGrass(upEquatorGrass.get(drawNumber));
                        upEquatorGrass.remove(drawNumber);
                        i++;
                    }
                    else{
                        if(underEquatorGrass.size() != 0)
                            drawNumber = 9;
                        else drawNumber = 3;
                        continue;
                    }
                }
            }
            drawNumber = generator.nextInt(10)+1;
        }
    }

    public List<Vector2d> grassGenerator(int a, int b){
        List<agh.ics.oop.Vector2d> setOfGrass = new ArrayList<Vector2d>();
        for (int x = 0; x < map.getWidth(); x++) {
            if ((b == 0) || (a==b))
                break;
            for (int y = a; y < b; y++)
                setOfGrass.add(new agh.ics.oop.Vector2d(x, y));
        }
        return setOfGrass;
    }

    public void updateGrass(int x, int y){
        if(y<(int)(0.4*map.getHeight()))
            underEquatorGrass.add(new Vector2d(x,y));
        else if (y<(int)(0.6*map.getHeight()))
            equatorGrass.add(new Vector2d(x, y));
        else upEquatorGrass.add(new Vector2d(x, y));
    }
}

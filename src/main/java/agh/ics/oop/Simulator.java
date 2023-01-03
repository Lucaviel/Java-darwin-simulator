package agh.ics.oop;

import agh.ics.oop.gui.App;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.*;

public class Simulator implements IEngine,Runnable {
    public int[] outputSum;
    public int days = 0;
    public boolean pausing = false;
    protected int startAnimals;
    protected int dailyLossEnergy;
    protected int geneLength;
    protected int numGrass;
    protected int startEnergy;
    protected int reproductionEnergy;
    protected int maxMutate;
    protected List<Animal> animals = new ArrayList<>();
    List<Vector2d> equatorGrass;
    List<Vector2d> underEquatorGrass;
    List<Vector2d> upEquatorGrass;
    private final Random generator = new Random();
    protected IWorldMap map;
    protected int startGrass;
    protected int plantEnergy;
    private int deadAnimalsNum = 0;
    private int summedLifeSpan = 0;
    private Animal trackedAnimal;
    public boolean tracked = false;
    protected App simulationObserver;
    protected GridPane pane;
    protected int moveEnergy;
    public Label daysCount;
    public Label genome;
    public Label grassCount;
    public Label avgEnergyCount;
    public Label avgLifeTime;
    public Label freeFieldsCount;
    Label trackedGenome;
    Label trackedCurrentGen;
    Label trackedOffspring;
    Label trackedDescendants;
    Label trackedEnergy;
    Label trackedGrass;
    Label deathDay;
    ArrayList<String[]> output;

    public Simulator(IWorldMap map, int startAnimals, int startEnergy, int numGrass, int geneLength,
                     int dailyLossEnergy, int reproductionEnergy, int maxMutate, int startGrass,
                     App simulationObserver, GridPane pane, int moveEnergy, int plantEnergy,
                     Label daysCount, Label genome, Label freeFieldsCount, Label grassCount, Label avgEnergyCount, Label avgLifeTime,
                     Label trackedGenome, Label trackedCurrentGen, Label trackedOffspring, Label trackedDescendants,
                     Label trackedEnergy,  Label trackedGrass,Label deathDay, ArrayList<String[]> output, int[] outputSum) {
        this.startAnimals = startAnimals;
        this.startEnergy = startEnergy;
        this.simulationObserver = simulationObserver;
        this.pane = pane;
        this.numGrass = numGrass;
        this.geneLength = geneLength;
        this.dailyLossEnergy = dailyLossEnergy;
        this.reproductionEnergy = reproductionEnergy;
        this.maxMutate = maxMutate;
        this.startGrass = startGrass;
        this.plantEnergy = plantEnergy;
        this.moveEnergy = moveEnergy;
        this.map = map;
        this.equatorGrass = grassGenerator((int) (0.4 * map.getHeight()), (int) (0.6 * map.getHeight()));
        this.underEquatorGrass = grassGenerator(0, (int) (0.4 * map.getHeight()));
        this.upEquatorGrass = grassGenerator((int) (0.6 * map.getHeight()), map.getHeight());
        this.daysCount = daysCount;
        this.genome = genome;
        this.freeFieldsCount = freeFieldsCount;
        this.grassCount = grassCount;
        this.avgLifeTime = avgLifeTime;
        this.avgEnergyCount = avgEnergyCount;
        this.trackedGenome = trackedGenome;
        this.trackedCurrentGen = trackedCurrentGen;
        this.trackedOffspring = trackedOffspring;
        this.trackedDescendants = trackedDescendants;
        this.trackedEnergy = trackedEnergy;
        this.trackedGrass = trackedGrass;
        this.deathDay = deathDay;
        this.output = output;
        this.outputSum = outputSum;
        addRandomAnimals(map, startAnimals);
        addRandomGrass(startGrass);
    }

    @Override
    public synchronized void run() {

        while(this.numOfAnimals() > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();}

            if (!pausing) {
                singleRun();
                this.days++;
            }
            if (tracked){
                Platform.runLater(() -> {
                    simulationObserver
                            .trackedAnimalVisual(trackedAnimal,this,trackedGenome,trackedCurrentGen,
                                    trackedOffspring,trackedDescendants, trackedEnergy, trackedGrass, deathDay);
                });
            }
        }
    }


    public synchronized void singleRun() {

        Platform.runLater(() -> {
            simulationObserver.mapVisual(map,pane);
            simulationObserver.statisticsVisual(this,this.map,this.daysCount,this.genome, this.grassCount,
                    this.avgEnergyCount, this.avgLifeTime, this.freeFieldsCount);
            simulationObserver.outputUpdate(map,this, output, outputSum);
        });

        deleteAnimals();

        for (Animal animal : animals) {
            Vector2d prev = animal.getPosition();
            animal.move(dailyLossEnergy);
            Vector2d now = animal.getPosition();
            animal.positionChanged(prev, now);
            animal.changeDays();
        }
        eatingAndReproduction();
        addRandomGrass(startGrass);

    }
    @Override
    public void switchPausing(){
        pausing = !pausing;
    }

    public int numOfAnimals() {
        return animals.size();
    }
    public Animal getTrackedAnimal() {
        return trackedAnimal;
    }

    public void setTrackedAnimal(Animal trackedAnimal) {
        this.trackedAnimal = trackedAnimal;
    }

    public void setTrackingVal(boolean a){
        tracked = a;
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
        if ((parent1.isEnergyMoreThan(reproductionEnergy)) && (parent2.isEnergyMoreThan(reproductionEnergy))) {
            parent1.changeChildren();
            parent2.changeChildren();
            Animal kid = new Animal(map, parent1, parent2, maxMutate);
            map.place(kid);
            animals.add(kid);
        }
    }

    public void deleteAnimals() {
        for (Animal value : animals) {
            if (value.getEnergy() <= 0) {
                deadAnimalsNum++;
                summedLifeSpan = summedLifeSpan + value.howOld();
                map.delete(value);
            }
        }

        animals.removeIf(animal -> animal.getEnergy() <= 0);

    }

    public void sortAnimals(ArrayList<Animal> animalsUnsorted) {

        Comparator<Animal> compareByMany = Comparator.comparing(Animal::getEnergy)
                .thenComparing(Animal::howOld).thenComparing(Animal::getChildren).reversed();

        animalsUnsorted.sort(compareByMany);

    }

    public void eatingAndReproduction() {
        for (int x = 0; x < map.getWidth(); x++)
            for (int y = 0; y < map.getHeight(); y++) {
                ArrayList<Animal> animalsAtVector = map.getAnimals().get(new Vector2d(x, y));
                if (!animalsAtVector.isEmpty()) {
                    sortAnimals(animalsAtVector);
                    Animal bestAnimal = animalsAtVector.get(0);
                    if (map.getGrasses().get(new Vector2d(x, y)) != null) { //jedzenie
                        bestAnimal.changeEnergy(plantEnergy);
                        bestAnimal.changeGrass();
                        map.removeGrass(new Vector2d(x, y));
                        updateGrass(x, y); /**/
                    }
                    if (animalsAtVector.size() > 1) {   //rozmnażanie
                        Animal parent2 = animalsAtVector.get(1);
                        newBornAnimal(bestAnimal, parent2);
                    }
                }
            }
    }

    public void addRandomGrass(int initGrassNumber) {
        int i = 0;
        int drawNumber = generator.nextInt(10) + 1; //losowanie od 1 do 10

        while (i < initGrassNumber) {
            if (equatorGrass.size() == 0 && underEquatorGrass.size() == 0 && upEquatorGrass.size() == 0)
                break;
            if (drawNumber <= 8) { //80% prawdopodobieństwa na równik
                if (equatorGrass.size() != 0) {
                    if (equatorGrass.size() == 1)
                        drawNumber = 0;
                    else drawNumber = generator.nextInt(equatorGrass.size() - 1);
                    map.addGrass(equatorGrass.get(drawNumber));
                    equatorGrass.remove(drawNumber);
                    i++;
                } else {
                    drawNumber = generator.nextInt(9, 11);
                    continue;
                }
            } else { //20% w innym miejscu
                if (drawNumber == 9) {
                    if (underEquatorGrass.size() != 0) {
                        if (underEquatorGrass.size() == 1)
                            drawNumber = 0;
                        else drawNumber = generator.nextInt(underEquatorGrass.size() - 1);
                        map.addGrass(underEquatorGrass.get(drawNumber));
                        underEquatorGrass.remove(drawNumber);
                        i++;
                    } else {
                        drawNumber = 10;
                        continue;
                    }
                } else {
                    if (upEquatorGrass.size() != 0) {
                        if (upEquatorGrass.size() == 1)
                            drawNumber = 0;
                        else drawNumber = generator.nextInt(upEquatorGrass.size() - 1);
                        map.addGrass(upEquatorGrass.get(drawNumber));
                        upEquatorGrass.remove(drawNumber);
                        i++;
                    } else {
                        if (underEquatorGrass.size() != 0)
                            drawNumber = 9;
                        else drawNumber = 3;
                        continue;
                    }
                }
            }
            drawNumber = generator.nextInt(10) + 1;
        }
    }

    public List<Vector2d> grassGenerator(int a, int b) {
        List<agh.ics.oop.Vector2d> setOfGrass = new ArrayList<>();
        for (int x = 0; x < map.getWidth(); x++) {
            if ((b == 0) || (a == b))
                break;
            for (int y = a; y < b; y++)
                setOfGrass.add(new agh.ics.oop.Vector2d(x, y));
        }
        return setOfGrass;
    }

    public void updateGrass(int x, int y) {
        if (y < (int) (0.4 * map.getHeight()))
            underEquatorGrass.add(new Vector2d(x, y));
        else if (y < (int) (0.6 * map.getHeight()))
            equatorGrass.add(new Vector2d(x, y));
        else upEquatorGrass.add(new Vector2d(x, y));
    }

    public int avgEnergy() {
        int sum = 0;
        for (Animal animal : animals) sum = sum + animal.getEnergy();
        if (numOfAnimals() == 0) return 0;
        return sum / numOfAnimals();
    }

    public int getAvgLifeSpan() {
        if (deadAnimalsNum == 0) return 0;
        return summedLifeSpan / deadAnimalsNum;
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

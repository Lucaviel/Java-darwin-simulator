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
    private final Random generator = new Random();
    protected IWorldMap map;
    protected int startGrass;

    protected int plantEnergy = 5;

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
            //eating grass
            //reproduction
            //grass growing
        addRandomAnimals(map, startAnimals);
        addRandomGrass(startGrass);
//        run();
    }

    public void run(){

//        ArrayList<Animal> aaa = getAnimalDetails();
//        for(Animal animal: aaa){
//            System.out.println(animal.getPosition());
//            System.out.println(animal.getEnergy());
//            System.out.println(animal.howOld());
//            System.out.println(animal.getChildren());
//            System.out.println("-------------");
//        }
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
    //to sb dodalam, żeby konkretnego zwierza dodać, bo mi inaczej nie wchodzil idk why
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
//        for (int i = 0; i < animals.size(); i++) {
//            if (animals.get(i).getEnergy() <= 0) {
//                //System.out.println("Energia to: " + animals.get(i).getEnergy());
//                map.delete(animals.get(i));
//                animals.remove(animals.get(i)); //bylo i ale bylo tez sciernisko a tu moze san franciso kto wie
//
//            }
//        }

        //removing if dead
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





    // to działa, ale jest taką funkcja z gatunku uposeldzenie umysłowe, po prostu chciałam, zeby tylko mi dawało animalse,
    // a nie ten linked mape xd, ale nigdzie tego nie używam finalnie just saying
//    public ArrayList<Animal> getAnimalDetails() {
//
//        ArrayList<Animal> animalArray = new ArrayList<>();
//
//        for (int x = 0; x < map.getWidth(); x++) {
//            for (int y = 0; y < map.getHeight(); y++) {
//                ArrayList<Animal> animalsAtVector = map.getAnimals().get(new Vector2d(x, y));
//                int animalsAtVectorSize = animalsAtVector.size();
//                if (animalsAtVectorSize > 0) {
//                    int i;
//                    for (i = 0; i < animalsAtVectorSize; i++) {
//                        animalArray.add(animalsAtVector.get(i));
//                    }
//                }
//            }
//        }
//        return animalArray;
//    }

    // to jest to sortowanie, z tym, ze tak było jeszcze na koncu w wymagnaiach projektu,
    // że jak się nie da już bardziej porównac to dac ranodomowo któregokolwiek
    // tot ego nie uwzgledniłam w tym tbh, no to kyrie elejson, co za różnic, i guess
    // pierwszy lepszy to jest losowy w pewnym sensie xd ew. mg nad tym posiedziec jeszcze
    //
    public void sortAnimals(ArrayList<Animal> animalsUnsorted){

        Comparator<Animal> compareByMany = Comparator.comparing(Animal::getEnergy)
                .thenComparing(Animal::howOld).thenComparing(Animal::getChildren).reversed();

        animalsUnsorted.sort(compareByMany);

    }

    public void eatingAndReproduction(){
        Random rand = new Random();
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

        Set<Vector2d> tempGrassCounterSet = new HashSet<>();

        int i = 0;
        Random rand = new Random();
        int x = 0;
        int y = 0;

        while (i < initGrassNumber) { //80% rownik
            int drawNumber = generator.nextInt(10)+1; //losowanie od 1 do 10
            if (drawNumber <= 8){
                x = rand.nextInt(1, map.getWidth()+1);
                int a = (int) (map.getHeight()*0.2+0.4*map.getHeight());
                y = rand.nextInt(a);
            }
            else{ //20% w innym miejscu
                x = rand.nextInt(1, map.getWidth()+1);
                int randd = (generator.nextInt(2)); //losowanie od 1 do 2
                if(randd==0){
                    y = rand.nextInt((int) (map.getHeight()*0.4));
                }
                else{
                    int a = (int) (map.getHeight()*0.4+0.6*map.getHeight());
                    y = rand.nextInt(a);
                }
            }
            if (!(map.isOccupiedByGrass(new Vector2d(x-1, y)))) {
                tempGrassCounterSet.add(new Vector2d(x-1,y));
                map.addGrass(new Vector2d(x-1,y));
                i = tempGrassCounterSet.size();
            }
        }
    }
}

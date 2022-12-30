package agh.ics.oop;

public class Statistics {
    protected int numDeadAnimals;
    protected int numAliveAnimals;
    protected int numGrass;
    protected int freeFields;
    protected int summedLifeSpan;
    IWorldMap map;

    public Statistics(IWorldMap map){
        this.map = map;
        this.numAliveAnimals = map.getAnimals().size();
        this.numGrass = map.getGrasses().size();
        this.numDeadAnimals = 0;
        this.summedLifeSpan = 0;

    }

    public int getAvgLifeSpan(){
        if (numDeadAnimals == 0) return 0;
        return summedLifeSpan / numDeadAnimals;
    }

    public int avgEnergy(){
        int sum = 0;
        for (Animal animal : map.getAnimals()) sum = sum + animal.getEnergy();
        if (numAliveAnimals == 0) return 0;
        return sum/numAliveAnimals;
    }
}

package org.example;

import java.util.ArrayList;
import java.util.Random;

public class Genotype extends Simulator{
    private ArrayList<Integer> GeneArr;
    private final Random generator = new Random();


    public Genotype(int nGenes){
        for(int i = 0; i < nGenes; i++)
            this.GeneArr.add(generator.nextInt(GENE_LENGTH));
    }

    public Genotype(int nGenes, Animal parent1, Animal parent2){
        this.GeneArr = afterParentsGenotype(parent1, parent2);
    }

    public int getCurrentGenotype(int i){      //nieco szaleństwa
        int drawNumber = generator.nextInt(1, 11);
        if (drawNumber <= 8){
            return this.GeneArr.get(i);
        }
        return this.GeneArr.get(generator.nextInt(0, GENE_LENGTH ));
    }

    public ArrayList<Integer> getGenotype(){
        return this.GeneArr;
    }

    public ArrayList<Integer> calculateGenotype(Animal parent1, Animal parent2, double sumEnergy){
        ArrayList<Integer> newGenArr = new ArrayList<>();
        int p = (int) ((double) parent1.getEnergy() / sumEnergy * GENE_LENGTH );
        for (int i = 0; i < GENE_LENGTH ; i++) {
            if (i < p) {
                newGenArr.add(parent1.getGenotype().get(i));
            } else {
                newGenArr.add(parent2.getGenotype().get(i));
            }
        }
        return newGenArr;
    }

    public ArrayList<Integer> afterParentsGenotype(Animal parent1, Animal parent2){   //dziedziczenie genotypu po rodzicach
        int sumOfParentsEnergy = parent1.getEnergy() + parent2.getEnergy();
        int sumOfParentsDays = parent1.howOld() + parent2.howOld();
        int sumOfParentsChildren = parent1.getChildren() + parent2.getChildren();
        if (parent1.getEnergy() < parent2.getEnergy()) {
            return calculateGenotype(parent1, parent2, sumOfParentsEnergy);
        }
        else if (parent1.getEnergy() > parent2.getEnergy()) {
            return calculateGenotype(parent2, parent1, sumOfParentsEnergy);
        }
        if (parent1.howOld() < parent2.howOld()) {
            return calculateGenotype(parent1, parent2, sumOfParentsDays);
        }
        else if (parent1.howOld() > parent2.howOld()){
            return calculateGenotype(parent2, parent1, sumOfParentsDays);
        }
        if (parent1.getChildren() < parent2.getChildren()){
            return calculateGenotype(parent1, parent2, sumOfParentsChildren);
        }
        else if (parent1.getChildren() > parent2.getChildren()){
            calculateGenotype(parent2, parent1, sumOfParentsChildren);
        }
        ArrayList<Integer> newGenArr = new ArrayList<>();       //losowo, jak wszystko jest równe
        int l = generator.nextInt(0, GENE_LENGTH );
        for (int i = 0; i < GENE_LENGTH; i++) {
            if (i < l) {
                newGenArr.add(parent1.getGenotype().get(i));
            } else {
                newGenArr.add(parent2.getGenotype().get(i));
            }
        }
        return newGenArr;
    }
}
package org.example;

import java.util.*;
import java.util.stream.IntStream;

public class Genotype{
    private ArrayList<Integer> GeneArr = new ArrayList<>();
    protected static int GENE_LENGTH = 8;
    private final Random generator = new Random();

    public Genotype(int nGenes){
        for(int i = 0; i < nGenes; i++)
            this.GeneArr.add(generator.nextInt(GENE_LENGTH));
    }

    public Genotype(Animal parent1, Animal parent2){
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
        int p = (int) ((double) parent1.getEnergy() / sumEnergy * GENE_LENGTH);
        for (int i = 0; i < GENE_LENGTH ; i++) {
            if (i < p) {
                newGenArr.add(parent1.getGenotype().get(i));
            } else {
                newGenArr.add(parent2.getGenotype().get(i));
            }
        }
        return newGenArr;
    }

    public ArrayList<Integer> mutateGenotype(ArrayList<Integer> genotypeArray){

        Random r1 = new Random();
        List<Integer> rangeList = IntStream.rangeClosed(0, GENE_LENGTH-1)
                .boxed().toList(); // lista indexów długości genomu
        int numToMutate = r1.nextInt(0, GENE_LENGTH + 1); // ranodmowa liczba genów do mutacji

        // get random subset of size numToMutate
        List<Integer> rangeLinkedList = new LinkedList<Integer>(rangeList);
        Collections.shuffle(rangeLinkedList);
        Set<Integer> indexesToMutate = new HashSet<Integer>(rangeLinkedList.subList(0, numToMutate));

        Iterator<Integer> indexesIterator = indexesToMutate.iterator();

        while(indexesIterator.hasNext()) {
            Integer indexToMutate = indexesIterator.next();
            Random r2 = new Random();
            int newGene = r2.nextInt(0, GENE_LENGTH);
            genotypeArray.set(indexToMutate, newGene);
        }

        return genotypeArray;
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

        return mutateGenotype(newGenArr); //pelna losowosc
    }
}
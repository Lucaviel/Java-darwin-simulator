package org.example;

import java.util.Random;

public class Genotype extends Simulator{
    private int[] GeneArr = {0,0,0,0,0,0,0,0};
    private final Random generator = new Random();
    private final int genesNum;


    public Genotype(int nGenes){
        genesNum = nGenes;
        for(int i=0; i < genesNum; i++)
            GeneArr[i] = generator.nextInt(8);
    }

    public int getGenotype(int i){      //nieco szaleństwa
        int drawNumber = generator.nextInt(1, 11);
        if (drawNumber <= 8){
            return GeneArr[i];
        }
        return GeneArr[generator.nextInt(i+1, 9)];
    }
}
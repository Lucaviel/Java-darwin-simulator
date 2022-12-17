package org.example;

public class Simulator {
    protected static final int GENE_LENGTH = 8;
    protected int day = 0;

    public Simulator(){
        for(int i=0; i<10; i++){
            //delete dead animals
            //changeOrientation();
            //eating grass
            //reproduction
            //grass growing
            //animal decreasing energy
            this.day++;
        }
    }
}

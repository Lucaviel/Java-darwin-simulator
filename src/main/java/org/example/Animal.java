package org.example;

public class Animal extends Simulator{
    private static final int GENE_LENGTH = 8;
    protected int energy;
    protected MapDirection orientation;
    protected int days;
    protected int children;
    protected Genotype gene;
    protected Vector2d position;
    protected int currentGene;


    public Animal(){
        this.orientation = MapDirection.NORTH;
        this.days = 0;
        this.children = 0;
        this.position = new Vector2d(2,2);
        this.energy = 20;
        this.gene = new Genotype(GENE_LENGTH);
        this.currentGene = 0;
    }

    public void changeOrientation(){
        int n = gene.getGenotype(this.currentGene);
        for(int i=0; i<n; i++)
            this.orientation = this.orientation.next();
        move();
    }

    public void move() {
        this.position = this.position.add(this.orientation.toUnitVector());
    }
}

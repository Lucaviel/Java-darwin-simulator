package org.example;

public class Grass {
    protected Vector2d position;

    public Grass(Vector2d position){
        this.position = position;
    }
    @Override
    public String toString(){
        return "*";
    }
}

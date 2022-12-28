package agh.ics.oop;

public class Grass {
    protected Vector2d position;

    public Grass(Vector2d position){
        this.position = position;
    }

    @Override
    public String toString(){
        return "*";
    }
    public Vector2d getPosition(){
        return this.position;
    }

    public String Visualize() {
        return "src/main/resources/dirt.png";
    }
}
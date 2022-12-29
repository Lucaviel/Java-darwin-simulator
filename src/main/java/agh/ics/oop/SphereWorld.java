package agh.ics.oop;

public class SphereWorld extends AbstractWorldMap {

    public SphereWorld(int width, int height) {
        super(width, height);
    }
    public void moveTo(Animal pet) {
        if (pet.position.y < 0 || pet.position.y >= height) {
            pet.position = pet.position.subtract(pet.orientation.toUnitVector());
            pet.changeOrientation(4);}
        else if (pet.position.x >= width)
            pet.position = new Vector2d(0, pet.position.y);
        else if (pet.position.x < 0)
            pet.position = new Vector2d(width-1, pet.position.y);

        }
    }


package agh.ics.oop;

public class SphereWorld extends AbstractWorldMap {

    public SphereWorld(int width, int height) {
        super(width, height);
    }
    public void moveTo(Animal pet) {
        if (pet.position.x > width)
            pet.position = new Vector2d(0, pet.position.y);
        if (pet.position.x < 0)
            pet.position = new Vector2d(width, pet.position.y);
        if (pet.position.y < 0 || pet.position.y > height) {
            pet.position = pet.position.subtract(pet.orientation.toUnitVector());
            pet.changeOrientation(4);
        }
    }
}

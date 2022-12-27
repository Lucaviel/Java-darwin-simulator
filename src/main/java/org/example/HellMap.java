package org.example;

import java.util.Random;

public class HellMap extends AbstractWorld{

    public HellMap(int width, int height) {
        super(width, height);
    }

    public Vector2d generateRandomPosition(){

        Random r1 = new Random();
        Random r2 = new Random();

        int x_r = r1.nextInt(0, width);
        int y_r = r2.nextInt(0, height);

        return new Vector2d(x_r, y_r);
    }

    public void moveTo(Animal pet) {
        if (pet.position.x > width || pet.position.x < 0 || pet.position.y < 0 || pet.position.y > height)
        {
            pet.position = generateRandomPosition();
            pet.changeEnergy(-2);
        }
    }
}

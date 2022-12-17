package org.example;

public class SphereMap {
    protected static final int HEIGHT = 10;
    protected static final int WEIGHT = 10;

    public Vector2d moveTo(Vector2d position) {
        if (position.x >= WEIGHT) return new Vector2d(0, position.y);
        if (position.y >= HEIGHT) return new Vector2d(position.x, HEIGHT);
        if (position.x < 0) return new Vector2d(WEIGHT, position.y);
        if (position.y < 0) return new Vector2d(position.x, 0);
        return new Vector2d(position.x, position.y);
    }
}

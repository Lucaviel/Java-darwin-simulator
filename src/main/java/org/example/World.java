package org.example;

import java.sql.SQLOutput;

public class World {

    public static void main(String[] args) {
        Animal gen = new Animal();
        gen.changeOrientation();
        System.out.println(gen.orientation);
    }
}
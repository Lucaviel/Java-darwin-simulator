package org.example;

public class World {

    public static void main(String[] args) {

//        SphereMap map = new SphereMap(10, 10);
//        Animal pet1 = new Animal(map, new Vector2d(2, 10));
//        Animal pet2 = new Animal(map, new Vector2d(0, 5));
//        System.out.println(pet2.position);
//        System.out.println(pet2.orientation);
//        System.out.println(pet1.getGenotype());
//        System.out.println(pet2.getGenotype());
//        pet2.changeOrientation(pet1.gene.getCurrentGenotype(pet1.currentGene));
//        pet2.move();
//        System.out.println(pet2.position);
//        System.out.println(pet2.orientation);
//        Animal pet3 = new Animal(map, pet1, pet2);
//        System.out.println(pet3.getGenotype());
//        System.out.println(pet3.getGenotypeInt());
        IWorldMap map = new SphereMap(10, 10);
        Simulator bla = new Simulator(map);

    }
}
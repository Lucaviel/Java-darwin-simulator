package agh.ics.oop;

public class World {

    public static void main(String[] args) {

//        SphereWorld map = new SphereWorld(10, 10);
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

        IWorldMap map = new SphereWorld(6, 6);
        IEngine bla = new Simulator(map, 2, 20, 10, 8, 2,
                5, 0, 2);
        System.out.println(bla);

    }
}
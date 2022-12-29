package agh.ics.oop;

public class World {

    public static void main(String[] args) {

       // IWorldMap map = new SphereWorld(5, 5);
        IWorldMap map = new SphereWorld(10, 10);
        Animal pet1 = new Animal(map, new Vector2d(0, 0), 10, 8);
        Animal pet2 = new Animal(map, new Vector2d(1, 0), 20, 8);
        IEngine bla = new Simulator(map, 0, 6, 10, 8, -2,
                5, 2, 6);

        System.out.println(map.getAnimals());

//        System.out.println(pet2.position);
//        System.out.println(pet2.orientation);
//        System.out.println(pet1.getGenotype());
//        System.out.println(pet2.getGenotype());
//        pet2.changeOrientation(pet1.gene.getCurrentGenotype(pet1.currentGene));
//        pet2.move();
//        System.out.println(pet2.position);
//        System.out.println(pet2.orientation);
//        System.out.println(pet3.getGenotype());

        bla.addSpecificAnimal(pet1);
        bla.addSpecificAnimal(pet2);
        System.out.println(map.getAnimals());

//        pet1.changeEnergy(-100);
//        pet2.changeEnergy(-200);

        bla.run();
        System.out.println(map.getAnimals());
       // Animal child = new Animal(map, pet1, pet2, 8);




    }
}
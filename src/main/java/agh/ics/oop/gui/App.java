package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.testng.Assert.assertTrue;

public class App extends Application{
    Stage window;
    Scene inputs, animation;
    Image imgDeadAnimal; // tego chyba nigdy nie będzie widać bo usuniemy z mapy na początku dnia go?

    {
        try {
            imgDeadAnimal = new Image(new FileInputStream("src/main/resources/dead.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    Image imgAnimal;

    {
        try {
            imgAnimal = new Image(new FileInputStream("src/main/resources/animal.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    Image imgDominantAnimal;

    {
        try {
            imgDominantAnimal = new Image(new FileInputStream("src/main/resources/dominantanimal.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    Image imgGrass;

    {
        try {
            imgGrass = new Image(new FileInputStream("src/main/resources/dirt.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    GridPane sphereWorld = new GridPane();
    GridPane hellMap = new GridPane();

    Label daysCount = new Label();
    Label genotype = new Label();

    Label daysCount2 = new Label();
    Label genotype2 = new Label();


    Button dominantGenome = new Button("Show animals with dominant genome (map on the left)");
    Button toFile = new Button("Generate CSV file");
    ArrayList<String[]> output = new ArrayList<>();
    int[] outputSum = {0,0,0,0,0};

    Button dominantGenome2 = new Button("Show animals with dominant genome (map on the right)");
    Button toFile2 = new Button("Generate CSV file");
    ArrayList<String[]> output2 = new ArrayList<>();
    int[] outputSum2 = {0,0,0,0,0};

    public void outputUpdate(IWorldMap map, Simulator engine,ArrayList<String[]> output,int[] outputSum){
        String[] dailyOutput = {String.valueOf(engine.numOfAnimals()),String.valueOf(map.getNumOfGrasses()),String.valueOf(engine.avgEnergy()),String.valueOf(engine.getAvgLifeSpan()),String.valueOf(engine.avgOffspringNum())};
        outputSum[0]=outputSum[0]+engine.numOfAnimals();
        outputSum[1]=outputSum[1]+map.getNumOfGrasses();
        outputSum[2]=outputSum[2]+engine.avgEnergy();
        outputSum[3]=outputSum[3]+engine.getAvgLifeSpan();
        outputSum[4]=outputSum[4]+engine.avgOffspringNum();
        output.add(dailyOutput);
    }

    public void statisticsVisual(Simulator engine,Label daysCount,Label genotype){
        daysCount.setText("Day: " + engine.days);
        genotype.setText("Genom: " + engine.dominantGenome());
    }

    public void trackedAnimalVisual(Animal animal,Simulator engine,IWorldMap map,Label trackedGenome,Label trackedOffspring, Label trackedDescendants, Label deathDay){
        if (animal == null){
            deathDay.setText("Dead on: " + engine.days + " day");
            engine.setTrackingVal(false);
            engine.setTrackedAnimal(null);
        }
        else if (animal.getEnergy() <= 0){
            deathDay.setText("Dead on: " + engine.days + " day");
            engine.setTrackingVal(false);
            engine.setTrackedAnimal(null);
        }
        else{
            trackedGenome.setText("Genome:" + (animal.getGenotype().toString().replace(",", "").replace(" ","")));
            trackedOffspring.setText("Kids: " + animal.getChildren());
            trackedDescendants.setText("Energy: " + animal.getEnergy());
            trackedDescendants.setText("Days: " + animal.howOld());
        }

    }

    public void mapVisual(IWorldMap map, GridPane pane, Simulator engine) {
        pane.setGridLinesVisible(false);
        pane.getColumnConstraints().clear();
        pane.getRowConstraints().clear();
        pane.getChildren().clear();
        pane.setGridLinesVisible(true);

        pane.setPadding(new Insets(10, 10, 10, 10));

        for (int i = 0; i < map.getWidth(); i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints(25); // width in pixels
            pane.getColumnConstraints().add(columnConstraints);
        }

        for (int i = 0; i < map.getHeight(); i++) {
            RowConstraints rowConstraints = new RowConstraints(25);
            pane.getRowConstraints().add(rowConstraints);
        }

        for (int y = 0; y < map.getHeight(); y++)
            for (int x = 0; x < map.getWidth(); x++) {
                if (map.objectAt(new Vector2d(x, y)) != null) {
                    GuiElementBox Box = new GuiElementBox(25,imgAnimal);;
                    if (map.objectAt(new Vector2d(x, y)) instanceof Grass){
                        Box = new GuiElementBox(25,imgGrass);
                    }

                    VBox box = null;
                    try {
                        box = Box.MakeBox();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    GridPane.setConstraints(box, x, y);
                    GridPane.setHalignment(box, HPos.CENTER);
                    pane.add(box, x, y);
                }

            }
    }

    public void mapPaused(IWorldMap map, Simulator engine, GridPane pane, Label trackedGenome,Label trackedOffspring,Label trackedDescendants,Label deathDay,Button thisDominantGenome, Button thisToFile, Boolean show){
        pane.setGridLinesVisible(false);
        pane.getColumnConstraints().clear();
        pane.getRowConstraints().clear();
        pane.getChildren().clear();
        pane.setGridLinesVisible(true);

        thisDominantGenome.setVisible(true);
        thisToFile.setVisible(true);

        pane.setPadding(new Insets(10, 10, 10, 10));


        for (int i = 0; i < map.getWidth(); i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints(25); // width in pixels
            pane.getColumnConstraints().add(columnConstraints);
        }

        for (int i = 0; i < map.getHeight(); i++) {
            RowConstraints rowConstraints = new RowConstraints(25);
            pane.getRowConstraints().add(rowConstraints);
        }

        for (int y = 0; y < map.getHeight(); y++)
            for (int x = 0; x < map.getWidth(); x++) {
                if (map.objectAt(new Vector2d(x, y)) != null) {
                    GuiElementBox Box = new GuiElementBox(25,imgAnimal);
                    if (map.objectAt(new Vector2d(x, y)) instanceof Grass){
                        Box = new GuiElementBox(25, imgGrass);
                    }

                    if (map.objectAt(new Vector2d(x,y)) instanceof Animal) {
                        if (engine.animalsWithDominantGenome().contains(((Animal) map.objectAt(new Vector2d(x, y)))) && show)
                            Box = new GuiElementBox(60, imgDominantAnimal);
                    }

                    VBox box = null;
                    try {
                        box = Box.MakeBox();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    GridPane.setConstraints(box, x, y);
                    GridPane.setHalignment(box, HPos.CENTER);
                    pane.add(box, x, y);
                    final int posX = x;
                    final int posY = y;

                    box.setOnMouseEntered((event) -> {
                        if (map.objectAt(new Vector2d(posX,posY)) instanceof Animal) {
                            if (!engine.tracked) {
                                trackedOffspring.setText("");
                                trackedDescendants.setText("");
                                deathDay.setText("");
                                trackedGenome.setText("Genome:" + ((Animal) map.objectAt(new Vector2d(posX,posY))).getGenotype().toString().replace(",", "").replace(" ",""));
                            }
                        }
                    });

                    box.setOnMouseClicked((event) -> {
                        if (map.objectAt(new Vector2d(posX,posY)) instanceof Animal) {
                            engine.setTrackedAnimal((Animal) map.objectAt(new Vector2d(posX,posY)));
                            engine.getTrackedAnimal().changeChildren();
                            engine.setTrackingVal(true);
                        }
                    });
                }
            }

    }

    public String toCSV(String[] output) {
        return Stream.of(output).collect(Collectors.joining(";"));
    }

    public void exportToFile(int day,ArrayList<String[]> thisOutput,int[] thisOutputSum,String side) throws IOException {
        File csvOutputFile = new File("OUTPUTS/"+ side + "engine_output_on_day_" + String.valueOf(day) + ".csv");
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            thisOutput.stream()
                    .map(this::toCSV)
                    .forEach(pw::println);
            pw.println();
            pw.print(thisOutputSum[0] / thisOutput.size());pw.print(";");
            pw.print(thisOutputSum[1] / thisOutput.size());pw.print(";");
            pw.print(thisOutputSum[2] / thisOutput.size());pw.print(";");
            pw.print(thisOutputSum[3] / thisOutput.size());pw.print(";");
            pw.print(thisOutputSum[4] / thisOutput.size());pw.print(";");

        }
        assertTrue(csvOutputFile.exists());
    }


    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;

        ReadFile f = new ReadFile("src/main/resources/parametry.txt");
        HashMap<String, String> parametersFromFile = f.getParametersFromFile();

        Label widthLabel = new Label("Width:");
        String width0 = parametersFromFile.get("WIDTH");
        Label widthTxtField = new Label(width0);

        Label heightLabel = new Label("Height:");
        String height0 = parametersFromFile.get("HEIGHT");
        Label heightTxtField = new Label(height0);

        Label startEnergyLabel = new Label("Start energy:");
        String startEnergy0 = parametersFromFile.get("START_ENERGY");
        Label startEnergyTxtField = new Label(startEnergy0);

        Label moveEnergyLabel = new Label("Move energy:");
        String moveEnergy0 = parametersFromFile.get("MOVE_ENERGY");
        Label moveEnergyTxtField = new Label(moveEnergy0);

        Label plantEnergyLabel = new Label("Plant energy:");
        String plantEnergy0 = parametersFromFile.get("PLANT_ENERGY");
        Label plantEnergyTxtField = new Label(plantEnergy0);

        Label initialNumOfAnimalsLabel = new Label("Initial number of animals:");
        String initialNumOfAnimals0 = parametersFromFile.get("START_ANIMALS");
        Label initialNumOfAnimalsField = new Label(initialNumOfAnimals0);

        Label mapVersionLabel = new Label("Chosen game version:");
        String mapVersion0 = parametersFromFile.get("MAP_VERSION");
        Label mapVersionField = new Label(mapVersion0);


        VBox Labels = new VBox(widthLabel, startEnergyLabel, plantEnergyLabel,
                            heightLabel, moveEnergyLabel, initialNumOfAnimalsLabel);
        Labels.setSpacing(10);
        Labels.setPadding(new Insets(10, 50, 50, 50));
        VBox Fields = new VBox(widthTxtField, startEnergyTxtField, plantEnergyTxtField,
                                heightTxtField, moveEnergyTxtField, initialNumOfAnimalsField);
        Fields.setSpacing(10);
        Fields.setPadding(new Insets(10, 50, 50, 50));

        HBox startBox = new HBox(Labels, Fields);
        startBox.setAlignment(Pos.CENTER);
        Button startstop = new Button("Start/Stop");
        Button startstop2 = new Button("Start/Stop");

        Button button = new Button("Start game");
        button.setPrefWidth(200);
        button.setOnAction(event -> {
            int width = Integer.parseInt(widthTxtField.getText());
            int height = Integer.parseInt(heightTxtField.getText());
            int startEnergy = Integer.parseInt(startEnergyTxtField.getText());
            int moveEnergy = Integer.parseInt(moveEnergyTxtField.getText());
            int plantEnergy = Integer.parseInt(plantEnergyTxtField.getText());
            int initAnimalsNumber = Integer.parseInt(initialNumOfAnimalsField.getText());

            IWorldMap sphereworld = new SphereWorld(width, height);
            IWorldMap hellmap = new HellMap(width, height, 0);

            Label trackedGenome = new Label();
            Label trackedOffspring = new Label();
            Label trackedDescendants = new Label();
            Label deathDay = new Label();
            VBox tracking1 = new VBox(trackedGenome,trackedOffspring,trackedDescendants,deathDay);

            Label trackedGenome2 = new Label();
            Label trackedOffspring2 = new Label();
            Label trackedDescendants2 = new Label();
            Label deathDay2 = new Label();
            VBox tracking2 = new VBox(trackedGenome2,trackedOffspring2,trackedDescendants2,deathDay2);

            dominantGenome.setVisible(false);
            toFile.setVisible(false);
            dominantGenome2.setVisible(false);
            toFile2.setVisible(false);
            VBox buttons = new VBox(dominantGenome,toFile,dominantGenome2,toFile2);
            buttons.setAlignment(Pos.CENTER);
            buttons.setSpacing(5);

            Separator sep = new Separator(Orientation.HORIZONTAL);
            Separator sep1 = new Separator(Orientation.HORIZONTAL);
            sep1.setPrefHeight(10);
            VBox tracking = new VBox(tracking1,sep,tracking2,sep1,buttons);


            IEngine earthSimulator = new Simulator(sphereworld, initAnimalsNumber, startEnergy,
                    3, 8, -2, -5, 7, 10,
                    this,sphereWorld,  moveEnergy, plantEnergy,
                    daysCount,genotype,
                    trackedGenome,trackedOffspring,trackedDescendants,deathDay,
                    output,outputSum);

            IEngine hellSimulator = new Simulator(hellmap, initAnimalsNumber, startEnergy,
                    3, 8, -2, -5, 7, 10,
                    this,hellMap,  moveEnergy, plantEnergy,
                    daysCount2,genotype2,
                    trackedGenome2,trackedOffspring2,trackedDescendants2,deathDay2,
                    output2,outputSum2);

//            IWorldMap gameMap;
//            GridPane gameGridPane;
//            IEngine gameSimulator;
//            if(mapVersionField.toString()=="EARTH") {
//                gameGridPane = sphereWorld;
//                gameMap = sphereworld;
//                gameSimulator = earthSimulator;
//            }
//            else if(mapVersionField.toString()=="HELL"){
//                gameGridPane = hellMap;
//                gameMap = hellmap;
//                gameSimulator = earthSimulator;
//            }


            mapVisual(sphereworld,sphereWorld,(Simulator) earthSimulator);
//            mapVisual(hellmap,hellMap,(Simulator) hellSimulator);

            Thread earthSimulatorThread = new Thread((Runnable) earthSimulator);
            earthSimulatorThread.start();
            startstop.setOnAction((event2) -> {
                earthSimulator.switchPausing();
                if (((Simulator) earthSimulator).pausing) {
                    this.mapPaused(sphereworld,(Simulator) earthSimulator,sphereWorld,trackedGenome,trackedOffspring,trackedDescendants,deathDay,dominantGenome,toFile,false);
                    toFile.setOnAction((event3) -> {
                        try {
                            exportToFile(((Simulator) earthSimulator).days,output,outputSum,"left");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    dominantGenome.setOnAction((event3) -> {
                        this.mapPaused(sphereworld,(Simulator) earthSimulator,sphereWorld,trackedGenome,trackedOffspring,trackedDescendants,deathDay,dominantGenome,toFile, true);
                    });
                }
                else {
                    dominantGenome.setVisible(false);
                    toFile.setVisible(false);
                }
            });


//            Thread hellSimulatorThread = new Thread((Runnable) hellSimulator);
//            hellSimulatorThread.start();
//            startstop2.setOnAction((event2) -> {
//                hellSimulator.switchPausing();
//                if (((Simulator) hellSimulator).pausing) {
//                    this.mapPaused(hellmap,(Simulator) hellSimulator,hellMap,trackedGenome2,trackedOffspring2,trackedDescendants2,deathDay2,dominantGenome2,toFile2,false);
//                    toFile2.setOnAction((event3) -> {
//                        try {
//                            exportToFile(((Simulator) hellSimulator).days,output2,outputSum2,"right");
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    });
//                    dominantGenome2.setOnAction((event3) -> {
//                        this.mapPaused(hellmap,(Simulator) hellSimulator,hellMap,trackedGenome2,trackedOffspring2,trackedDescendants2,deathDay2,dominantGenome2,toFile2, true);
//                    });
//                }
//                else {
//                    dominantGenome2.setVisible(false);
//                    toFile2.setVisible(false);
//                }
//            });


            HBox statBox = new HBox(daysCount,genotype);
            statBox.setSpacing(10);
//            HBox statBox2 = new HBox(daysCount2,genotype2);
//            statBox2.setSpacing(10);
            sphereWorld.setAlignment(Pos.CENTER);
//            hellMap.setAlignment(Pos.CENTER);
            VBox leftMap = new VBox(sphereWorld,startstop,statBox);
//            VBox rightMap = new VBox(hellMap,startstop2,statBox2);
            leftMap.setAlignment(Pos.TOP_CENTER);
//            rightMap.setAlignment(Pos.TOP_CENTER);

            leftMap.setPrefWidth(sphereworld.getWidth()*30);
//            rightMap.setPrefWidth(hellmap.getWidth()*30);
            leftMap.setMaxWidth(sphereworld.getWidth()*25 + 40);
//            rightMap.setMaxWidth(sphereworld.getWidth()*25 + 40);
            leftMap.setMinWidth(375);
//            rightMap.setMinWidth(375);

            HBox box = new HBox(leftMap, tracking);
            box.setAlignment(Pos.CENTER);
            animation = new Scene(box, Math.max(sphereworld.getWidth()*40+600,1100), hellmap.getHeight()*20+500);

            window.setScene(animation);
        });


        VBox inputsLayout = new VBox(startBox,  button);
        inputsLayout.setAlignment(Pos.CENTER);
        inputs = new Scene(inputsLayout, 650, 800);

        primaryStage.setTitle("Game of Life");

        primaryStage.setScene(inputs);
        window.show();

    }

}

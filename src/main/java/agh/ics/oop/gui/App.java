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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.testng.Assert.assertTrue;

public class App extends Application{
    Stage window;
    Stage title;
    Scene inputs, animation;

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


    GridPane finalSimulatorWorld = new GridPane();

    Label daysCount = new Label();
    Label genotype = new Label();
    Label grassCount = new Label();
    Label freeFieldsCount = new Label();
    Label avgLifeTime = new Label();
    Label avgEnergyCount = new Label();

    Label csvInfo = new Label("CSV will be created in the CSV folder after closing the simulation window");
    Button dominantGenome = new Button("Show animals with dominant genome");
    Button toFile = new Button("Generate CSV file of daily stats");
    ArrayList<String[]> output = new ArrayList<>();
    int[] outputSum = {0,0,0,0,0,0};
    protected final static int SIZE = 25;

    public void outputUpdate(IWorldMap map, Simulator engine,ArrayList<String[]> output,int[] outputSum){
        String[] dailyOutput = {String.valueOf(engine.days),
                String.valueOf(engine.numOfAnimals()),String.valueOf(map.getNumOfGrasses()),
                String.valueOf(engine.avgEnergy()),String.valueOf(engine.getAvgLifeSpan()),
                String.valueOf(engine.freeFieldsNum()), engine.dominantGenome()};

        outputSum[1]=outputSum[1]+engine.numOfAnimals();
        outputSum[2]=outputSum[2]+map.getNumOfGrasses();
        outputSum[3]=outputSum[3]+engine.avgEnergy();
        outputSum[4]=outputSum[4]+engine.getAvgLifeSpan();
        outputSum[5]=outputSum[5]+engine.freeFieldsNum();

        output.add(dailyOutput);
    }

    public void statisticsVisual(Simulator engine,IWorldMap map, Label daysCount,Label genotype, Label grassCount,
                                 Label avgEnergyCount, Label avgLifeTime, Label freeFieldsCount){
        daysCount.setText("Day: " + engine.days);
        genotype.setText("Dominant genotype: " + engine.dominantGenome());
        freeFieldsCount.setText("Free fields: " + engine.freeFieldsNum());
        grassCount.setText("Grass: " + map.getNumOfGrasses());
        avgEnergyCount.setText("Average energy: " + engine.avgEnergy());
        avgLifeTime.setText("Average life in days: " + engine.getAvgLifeSpan());
    }

    public void trackedAnimalVisual(Animal animal,Simulator engine,Label trackedGenome,
                                    Label trackedCurrentGen, Label trackedOffspring, Label trackedDescendants,
                                    Label trackedEnergy, Label trackedGrass, Label deathDay){
        if (animal.getEnergy() <= 0){
            deathDay.setText("Dead on: " + engine.days + " day");
            engine.setTrackingVal(false);
            engine.setTrackedAnimal(null);
        }
        else{
            trackedGenome.setText("Genome:" + (animal.getGenotype().toString().replace(",",
                    "").replace(" ","")));
            trackedCurrentGen.setText("Current Genotype: " + animal.getGenotype().get(animal.getCurrentGene()));
            trackedOffspring.setText("Children: " + animal.getChildren());
            trackedEnergy.setText("Energy: " + animal.getEnergy());
            trackedGrass.setText("Eaten grass: " + animal.getEatenGrass());
            trackedDescendants.setText("Days: " + animal.howOld());
            deathDay.setText("Dead on: not yet");
        }

    }

    public void mapVisual(IWorldMap map, GridPane pane) {
        pane.setGridLinesVisible(false);
        pane.getColumnConstraints().clear();
        pane.getRowConstraints().clear();
        pane.getChildren().clear();
        pane.setGridLinesVisible(true);

        pane.setPadding(new Insets(10, 10, 10, 10));

        for (int i = 0; i < map.getWidth(); i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints(SIZE);
            pane.getColumnConstraints().add(columnConstraints);
        }

        for (int i = 0; i < map.getHeight(); i++) {
            RowConstraints rowConstraints = new RowConstraints(SIZE);
            pane.getRowConstraints().add(rowConstraints);
        }

        for (int y = 0; y < map.getHeight(); y++)
            for (int x = 0; x < map.getWidth(); x++) {
                if (map.objectAt(new Vector2d(x, y)) != null) {
                    GuiElementBox Box = new GuiElementBox(SIZE,imgAnimal);
                    if (map.objectAt(new Vector2d(x, y)) instanceof Grass){
                        Box = new GuiElementBox(SIZE,imgGrass);
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

    public void mapPaused(IWorldMap map, Simulator engine, GridPane pane,Button thisDominantGenome,
                          Button thisToFile, Label csvInfo, Boolean show){
        pane.setGridLinesVisible(false);
        pane.getColumnConstraints().clear();
        pane.getRowConstraints().clear();
        pane.getChildren().clear();
        pane.setGridLinesVisible(true);

        thisDominantGenome.setVisible(true);
        thisToFile.setVisible(true);
        csvInfo.setVisible(true);

        pane.setPadding(new Insets(10, 10, 10, 10));


        for (int i = 0; i < map.getWidth(); i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints(SIZE);
            pane.getColumnConstraints().add(columnConstraints);
        }

        for (int i = 0; i < map.getHeight(); i++) {
            RowConstraints rowConstraints = new RowConstraints(SIZE);
            pane.getRowConstraints().add(rowConstraints);
        }

        for (int y = 0; y < map.getHeight(); y++)
            for (int x = 0; x < map.getWidth(); x++) {
                if (map.objectAt(new Vector2d(x, y)) != null) {
                    GuiElementBox Box = new GuiElementBox(SIZE,imgAnimal);
                    if (map.objectAt(new Vector2d(x, y)) instanceof Grass){
                        Box = new GuiElementBox(SIZE, imgGrass);
                    }

                    if (map.objectAt(new Vector2d(x,y)) instanceof Animal) {
                        if (engine.animalsWithDominantGenome().contains(((Animal) map.objectAt(new Vector2d(x, y))))
                                && show)
                            Box = new GuiElementBox(45, imgDominantAnimal);
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

                    assert box != null;
                    box.setOnMouseClicked((event) -> {
                        if (map.objectAt(new Vector2d(posX,posY)) instanceof Animal) {
                            engine.setTrackedAnimal((Animal) map.objectAt(new Vector2d(posX,posY)));
                            engine.getTrackedAnimal().getChildren();
                            engine.setTrackingVal(true);
                        }
                    });
                }
            }
    }

    public String toCSV(String[] output) {
        return Stream.of(output).collect(Collectors.joining(";"));
    }

    public void exportToFile(int day, ArrayList<String[]> thisOutput) throws IOException {
        File csvOutputFile = new File("CSV/darwin_game_on_day_" + String.valueOf(day) + ".csv");

        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            String[] header = {"Day Number","Animal count", "Plants count",
                    "Average energy", "Average Lifespan", "Free fields count", "Dominant genotype"};
            pw.println(toCSV(header));
            thisOutput.stream()
                    .map(this::toCSV)
                    .forEach(pw::println);
        }
        assertTrue(csvOutputFile.exists());
    }

    public void start(Stage primaryStage) {

        window = primaryStage;
        title = primaryStage;

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter("Txt files (*.txt)",
                "*.txt");
        fileChooser.getExtensionFilters().add(extentionFilter);

        fileChooser.setInitialDirectory(new File("src/main/resources"));

        Label label = new Label("File:");
        TextField tf= new TextField("Choose file");
        Button btn = new Button("Choose file");

        HBox root = new HBox();
        root.setSpacing(20);
        root.getChildren().addAll(label,tf,btn);

        btn.setOnAction(event->
        {
            fileChooser.setTitle("Open File");
            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            ReadFile f = new ReadFile(selectedFile);
            HashMap<String, String> parametersFromFile = f.getParametersFromFile();

            Label widthLabel = new Label("Width:");
            String widthValue = parametersFromFile.get("WIDTH");
            Label widthInput = new Label(widthValue);

            Label heightLabel = new Label("Height:");
            String heightValue = parametersFromFile.get("HEIGHT");
            Label heightInput = new Label(heightValue);

            Label startEnergyLabel = new Label("Start energy:");
            String startEnergyValue = parametersFromFile.get("START_ENERGY");
            Label startEnergyInput = new Label(startEnergyValue);

            Label moveEnergyLabel = new Label("Move energy:");
            String moveEnergyValue = parametersFromFile.get("MOVE_ENERGY");
            Label moveEnergyInput = new Label(moveEnergyValue);

            Label plantEnergyLabel = new Label("Plant energy:");
            String plantEnergyValue = parametersFromFile.get("PLANT_ENERGY");
            Label plantEnergyInput = new Label(plantEnergyValue);

            Label initialNumOfAnimalsLabel = new Label("Initial number of animals:");
            String initialNumOfAnimalsValue = parametersFromFile.get("START_ANIMALS");
            Label initialNumOfAnimalsInput = new Label(initialNumOfAnimalsValue);

            Label mapVersionLabel = new Label("Chosen game version:");
            String mapVersionValue = parametersFromFile.get("MAP_VERSION");
            Label mapVersionInput = new Label(mapVersionValue);


            Label geneLengthLabel = new Label("Length of Genotype:");
            String geneLength0 = parametersFromFile.get("GENE_LENGTH");
            Label geneLengthField = new Label(geneLength0 );

            Label dailyPlantLabel = new Label("Daily growing plants:");
            String dailyPlant0 = parametersFromFile.get("DAILY_GRASS");
            Label dailyPlantField = new Label(dailyPlant0);

            Label reproductionLossLabel = new Label("Reproduction energy:");
            String reproductionLoss0 = parametersFromFile.get("REPRODUCT_LOSS");
            Label reproductionLossField = new Label(reproductionLoss0);

            Label maxMutateLabel = new Label("Max number of mutation:");
            String maxMutate0 = parametersFromFile.get("MAX_MUTATE");
            Label maxMutateField = new Label(maxMutate0);

            Label startGrassLabel = new Label("Grass on the start:");
            String startGrass0 = parametersFromFile.get("START_GRASS");
            Label startGrassField = new Label(startGrass0);

            Label teleEnergyLabel = new Label("Teleport energy (only for HELL map):");
            String teleEnergy0 = parametersFromFile.get("TELE_ENERGY");
            Label teleEnergyField = new Label(teleEnergy0);

            VBox Labels = new VBox(widthLabel, startEnergyLabel, plantEnergyLabel,heightLabel, moveEnergyLabel,
                    initialNumOfAnimalsLabel, mapVersionLabel, geneLengthLabel, dailyPlantLabel, reproductionLossLabel,
                    maxMutateLabel, startGrassLabel, teleEnergyLabel);
            Labels.setSpacing(10);
            Labels.setPadding(new Insets(10, 50, 50, 50));
            VBox Fields = new VBox(widthInput, startEnergyInput, plantEnergyInput,
                    heightInput, moveEnergyInput, initialNumOfAnimalsInput, mapVersionInput,geneLengthField,
                    dailyPlantField, reproductionLossField, maxMutateField, startGrassField, teleEnergyField);
            Fields.setSpacing(10);
            Fields.setPadding(new Insets(10, 50, 50, 50));

            Label titleStart = new Label("Chosen parametres:");
            HBox parametreBox = new HBox(titleStart);
            parametreBox.setAlignment(Pos.CENTER);
            HBox startBox = new HBox(Labels, Fields);
            startBox.setAlignment(Pos.CENTER);
            Button startstop = new Button("Start/Stop");

            Button button = new Button("Start simulation");
            button.setPrefWidth(200);
            button.setOnAction(event2 -> {
                int width = Integer.parseInt(widthInput.getText());
                int height = Integer.parseInt(heightInput.getText());
                int startEnergy = Integer.parseInt(startEnergyInput.getText());
                int moveEnergy = Integer.parseInt(moveEnergyInput.getText());
                int plantEnergy = Integer.parseInt(plantEnergyInput.getText());
                int initAnimalsNumber = Integer.parseInt(initialNumOfAnimalsInput.getText());
                String mapVersion = mapVersionInput.getText();
                int geneLength = Integer.parseInt(geneLengthField.getText());
                int dailyPlant = Integer.parseInt(dailyPlantField.getText());
                int reproductionLoss = Integer.parseInt(reproductionLossField.getText());
                int maxMutate = Integer.parseInt(maxMutateField.getText());
                int startGrass = Integer.parseInt(startGrassField.getText());
                int teleEnergy = Integer.parseInt(teleEnergyField.getText());

                IWorldMap finalworld;
                if (Objects.equals(mapVersion, "EARTH"))
                    finalworld = new SphereWorld(width, height, teleEnergy);
                else finalworld = new HellMap(width, height, 0);

                Label trackedGenome = new Label();
                Label trackedCurrentGen = new Label();
                Label trackedOffspring = new Label();
                Label trackedDays = new Label();
                Label trackedEnergy = new Label();
                Label trackedGrass = new Label();
                Label deathDay = new Label();
                VBox tracking1 = new VBox(trackedGenome,trackedCurrentGen, trackedOffspring,trackedDays,trackedEnergy,
                        trackedGrass, deathDay);


                dominantGenome.setVisible(false);
                toFile.setVisible(false);
                csvInfo.setVisible(false);
                Region spacer0 = new Region();
                spacer0.setPrefHeight(20);

                VBox buttons = new VBox( dominantGenome, toFile, spacer0, csvInfo);
                buttons.setAlignment(Pos.CENTER);
                buttons.setSpacing(5);

                Simulator finalSimulator = new Simulator(finalworld, initAnimalsNumber, startEnergy,
                        dailyPlant, geneLength, moveEnergy, reproductionLoss, maxMutate, startGrass,
                        this,finalSimulatorWorld,  moveEnergy, plantEnergy,
                        daysCount,genotype, grassCount, avgEnergyCount, avgLifeTime, freeFieldsCount,
                        trackedGenome,trackedCurrentGen, trackedOffspring,trackedDays,trackedEnergy, trackedGrass,
                        deathDay, output,outputSum);

                mapVisual(finalworld,finalSimulatorWorld);

                Thread earthSimulatorThread = new Thread(finalSimulator);
                earthSimulatorThread.start();
                startstop.setOnAction((event3) -> {
                    finalSimulator.switchPausing();
                    if (finalSimulator.pausing) {
                        this.mapPaused(finalworld, finalSimulator,finalSimulatorWorld, dominantGenome,
                                toFile, csvInfo,false);
                        toFile.setOnAction((event4) -> {
                            try {
                                exportToFile(finalSimulator.days,output);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        dominantGenome.setOnAction((event4) -> {
                            this.mapPaused(finalworld, finalSimulator,finalSimulatorWorld, dominantGenome,
                                    toFile, csvInfo,true);
                        });
                    }
                    else {
                        dominantGenome.setVisible(false);
                        toFile.setVisible(false);
                        csvInfo.setVisible(false);
                    }
                });


                HBox statBox1 = new HBox(daysCount,genotype, freeFieldsCount);
                HBox statBox2 = new HBox(grassCount,avgEnergyCount,avgLifeTime);
                statBox1.setSpacing(15);
                statBox2.setSpacing(15);
                finalSimulatorWorld.setAlignment(Pos.CENTER);
                VBox darwinMap = new VBox(finalSimulatorWorld,startstop);
                darwinMap.setAlignment(Pos.TOP_CENTER);

                Region spacer = new Region();
                spacer.setPrefHeight(20);
                Region spacer1 = new Region();
                spacer1.setPrefHeight(20);
                Region spacer2 = new Region();
                spacer2.setPrefHeight(20);
                Region spacer3 = new Region();
                spacer3.setPrefHeight(20);
                Region spacer4 = new Region();
                spacer4.setPrefHeight(20);
                Region spacer5 = new Region();
                spacer5.setPrefHeight(20);
                Region spacer6 = new Region();
                spacer6.setPrefHeight(20);
                Region spacer7 = new Region();
                spacer7.setPrefHeight(20);

                Separator sep = new Separator(Orientation.HORIZONTAL);
                Separator sep1 = new Separator(Orientation.HORIZONTAL);
                Label trackingAnimalLabel = new Label("Tracked Animal Info");
                Font font = Font.font("Verdana", FontWeight.BOLD, 15);
                trackingAnimalLabel.setFont(font);
                Label statsLabel = new Label("Simulation statistics");
                statsLabel.setFont(font);

                VBox tracking = new VBox(spacer3, statsLabel, spacer5, statBox1, spacer2, statBox2, spacer6,
                                        sep,spacer, trackingAnimalLabel, spacer1, tracking1, spacer7, sep1,
                                     spacer4, buttons);


                darwinMap.setPrefWidth(finalworld.getWidth()*30);
                darwinMap.setMaxWidth(finalworld.getWidth()*25 + 40);
                darwinMap.setMinWidth(375);

                HBox box = new HBox(darwinMap, tracking);
                box.setAlignment(Pos.CENTER);
                animation = new Scene(box, Math.max(finalworld.getWidth()*40+100,1100),
                        finalworld.getHeight()*20+200);

                window.setScene(animation);
        });


            VBox inputsLayout = new VBox(parametreBox, startBox,  button);
            inputsLayout.setAlignment(Pos.CENTER);
            inputs = new Scene(inputsLayout, 550, 550);

            title.setScene(inputs);
            window.show();

        });

        VBox inputsLayout = new VBox(root);
        inputsLayout.setAlignment(Pos.CENTER);
        inputs = new Scene(inputsLayout, 325, 125);

        primaryStage.setTitle("Darwin Simulator");

        primaryStage.setScene(inputs);
        window.show();

    }

}

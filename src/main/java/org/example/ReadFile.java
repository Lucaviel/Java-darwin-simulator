package org.example;


import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ReadFile {

    public static HashMap<String, String> getParametersFromFile() {

        File f = new File("./parametry.txt");
        Scanner read = null;
        try {
            read = new Scanner(f);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        HashMap<String, String> Parameters = new HashMap<>();

        while (read.hasNext()) {
            String line = read.nextLine();

            java.util.List<String> read_line = List.of(line.split("="));
            String ParameterName = read_line.get(0);
            String ParameterValue = read_line.get(1);

            Parameters.put(ParameterName, ParameterValue);
        }
        return Parameters;
    }

}

package org.example;


import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class ReadFile {
    public HashMap<String, String> getParameterHashMap() {

        File f = new File("C:\\Users\\marty\\Java\\javaProject\\teksty.txt");
        Scanner read = null;
        try {
            read = new Scanner(f);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        HashMap<String, String> Parameters = new HashMap<>();

        while (read.hasNext()) {
            String line = read.nextLine();

//            List<String> read_line = List.of(line.split("="));
//            String ParameterName = read_line.get(0);
//            String ParameterValue = read_line.get(1);

//            Parameters.put(ParameterName, ParameterValue);
        }

        return Parameters;
    }
}

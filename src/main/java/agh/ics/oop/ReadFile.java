package agh.ics.oop;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ReadFile {

    private final File f;

    public ReadFile(String pathname) {
        this.f = new File(pathname);
    }

    public HashMap<String, String> getParametersFromFile() {

        Scanner read;
        try {
            read = new Scanner(this.f);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        HashMap<String, String> Parameters = new HashMap<>();

        while (read.hasNext()) {
            String line = read.nextLine();

            List<String> read_line = List.of(line.split("="));
            String ParameterName = read_line.get(0);
            String ParameterValue = read_line.get(1);

            Parameters.put(ParameterName, ParameterValue);
        }
        return Parameters;
    }

}

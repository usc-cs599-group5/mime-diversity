package csci599;

import java.io.*;
import java.util.*;
import java.util.function.*;
import static java.util.stream.Collectors.*;

public class FHT {
    public static void analyze(String[] fileNames) {
        // create sparse matrix
        final int H = 16;
        int[][] matrix = new int[H][256];
        int numFiles = 0; // don't use fileNames.length in case can't open some files
        for (String fileName : fileNames) {
            try (FileInputStream in = new FileInputStream(fileName)) {
                numFiles++;
                for (int j = 0; j < H; j++) {
                    int b = in.read();
                    if (b < 0) break;
                    matrix[j][b]++;
                }
            } catch (IOException ex) {
                System.err.println("Error reading file: " + fileName);
            }
        }
        if (numFiles == 0) {
            System.out.println("Must specify files.");
            return;
        }
        // helper function to convert matrix row into string
        final double finalNumFiles = numFiles;
        Function<int[], String> rowString = row -> Arrays.stream(row).mapToObj(c -> "" + c / finalNumFiles).collect(joining(","));
        // write csv file
        try (Writer out = new OutputStreamWriter(new FileOutputStream("fht.csv"), "UTF-8")) {
            for (int[] row : matrix) {
                out.write(rowString.apply(row) + "\n");
            }
        } catch (IOException ex) {
            System.err.println("Error writing fht.csv");
        }
        // write json file
        try (Writer out = new OutputStreamWriter(new FileOutputStream("fht.json"), "UTF-8")) {
            out.write("[\n");
            for (int[] row : matrix) {
                out.write("    [" + rowString.apply(row) + "],\n");
            }
            out.write("]\n");
        } catch (IOException ex) {
            System.err.println("Error writing fht.json");
        }
    }
}

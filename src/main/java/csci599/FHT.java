// TODO:
// I'm using wrong algorithm, should use http://www.computer.org/csdl/proceedings/hicss/2003/1874/09/187490332a.pdf
// sort output by content type

package csci599;

import java.io.*;
import java.util.*;
import java.util.function.*;
import static java.util.stream.Collectors.*;

public class FHT {
    private static final int H = 16;
    private static int[][] matrix;
    private static int numFiles = 0; // don't use fileNames.length in case can't open some files

    public static void analyze(File folder, List<String> contentTypes) {
        // create sparse matrix
        matrix = new int[H][256];
        numFiles = 0;
        FileTypeFilter.forEach(folder, contentTypes, (file, contentType) -> {
            try (FileInputStream in = new FileInputStream(file)) {
                numFiles++;
                for (int i = 0; i < H; i++) {
                    int b = in.read();
                    if (b >= 0) {
                        matrix[i][b]++;
                    } else {
                        // end of file, subtract 1 from rows
                        for (int j = 0; j < 256; j++) {
                            matrix[i][j]--;
                        }
                    }
                }
            } catch (IOException ex) {
                System.err.println("Error reading file: " + file.getPath());
            }
        });
        if (numFiles == 0) {
            System.out.println("Folder is empty.");
            return;
        }
        // helper function to convert matrix row into string
        // dividing by numFiles here is equivalent to fingerprint formula in II.3.2 of paper
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

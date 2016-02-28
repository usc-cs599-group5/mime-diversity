package csci599;

import java.io.*;
import java.util.*;
import java.util.function.*;
import static java.util.stream.Collectors.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FHT {
    private static final int H = 16;

    private static class Fingerprint {
        public int[][] matrix;
        public int numFiles = 0;

        public Fingerprint() {
            matrix = new int[H][256];
        }

        public void addFile(File file) {
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
        }
    }

    public static void analyze(File folder, List<String> contentTypes) {
        // initialize fingerprints
        Map<String, Fingerprint> fingerprints = new HashMap<>();
        for (String contentType : contentTypes) {
            fingerprints.put(contentType, new Fingerprint());
        }
        // create sparse matrices
        FileTypeFilter.forEach(folder, contentTypes, (file, contentType) -> {
            fingerprints.get(contentType).addFile(file);
        });
        // write json file
        Map<String, double[][]> json = new HashMap<>();
        for (Map.Entry<String, Fingerprint> entry : fingerprints.entrySet()) {
            Fingerprint fingerprint = entry.getValue();
            if (fingerprint.numFiles == 0) {
                System.out.println("Warning: No files found with MIME type " + entry.getKey());
            } else {
                double[][] matrix = new double[H][256];
                for (int i = 0; i < H; i++) {
                    for (int j = 0; j < 256; j++) {
                        // dividing by numFiles here is equivalent to fingerprint formula in II.3.2 of paper
                        matrix[i][j] = fingerprint.matrix[i][j] / (double)fingerprint.numFiles;
                    }
                }
                json.put(entry.getKey(), matrix);
            }
        }
        try {
            new ObjectMapper().writeValue(new File("fht.json"), json);
        } catch (IOException ex) {
            System.err.println("Error writing fht.json");
        }
    }
}

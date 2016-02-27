package csci599;

import java.io.*;
import java.util.*;
import java.util.function.*;
import static java.util.stream.Collectors.*;

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
        final Map<String, Fingerprint> fingerprints = new HashMap<>();
        for (String contentType : contentTypes) {
            fingerprints.put(contentType, new Fingerprint());
        }
        // create sparse matrices
        FileTypeFilter.forEach(folder, contentTypes, (file, contentType) -> {
            fingerprints.get(contentType).addFile(file);
        });
        // write json file
        // Andrew wanted to use Clojure but he was outvoted, so he gets to use Java streams instead :)
        try (Writer out = new OutputStreamWriter(new FileOutputStream("fht.json"), "UTF-8")) {
            out.write("{\n" + fingerprints.entrySet().stream()
                .filter(entry -> {
                    if (entry.getValue().numFiles == 0) {
                        System.out.println("Warning: No files found with MIME type " + entry.getKey());
                        return false;
                    }
                    return true;
                })
                .map(entry -> "    \"" + entry.getKey() + "\": [\n" + Arrays.stream(entry.getValue().matrix)
                    // dividing by numFiles here is equivalent to fingerprint formula in II.3.2 of paper
                    .map(row -> "        [" + Arrays.stream(row).mapToObj(c -> "" + c / (double)entry.getValue().numFiles)
                                                                .collect(joining(",")) + "]")
                    .collect(joining(",\n")) + "\n    ]")
                .collect(joining(",\n")) + "\n}\n");
        } catch (IOException ex) {
            System.err.println("Error writing fht.json");
        }
    }
}

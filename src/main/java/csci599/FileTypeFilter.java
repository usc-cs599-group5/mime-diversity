package csci599;

import java.io.*;
import java.util.*;
import java.util.function.*;
import static java.util.stream.Collectors.*;
import org.apache.tika.Tika;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FileTypeFilter {
    private static Tika tika = new Tika();

    public static List<String> getMIMETypes(final File sortFolder) {
        return Arrays.stream(sortFolder.listFiles())
                     .map(file -> file.getName().replace(';', '/'))
                     .collect(toList());
    }

    public static void forEach(final File sortFolder, BiConsumer<File, String> callback) {
        int line = 0, totalLines = 0;
        // count total # files to process
        for (File file : sortFolder.listFiles()) {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    scanner.nextLine();
                    totalLines++;
                }
            } catch (Exception ex) {
                System.out.println("Error counting lines in " + file.getPath());
            }
        }
        // call callback on each file
        for (File file : sortFolder.listFiles()) {
            String mimeType = file.getName().replace(';', '/');
            System.out.println(mimeType);
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    callback.accept(new File(scanner.nextLine()), mimeType);
                    line++;
                    System.out.print(100 * line / totalLines + "%\r");
                }
            } catch (Exception ex) {
                System.err.println("Error iterating over files of MIME type " + mimeType);
            }
        }
    }

    public static void sort(final File folder, final List<String> contentTypes) {
        // create file writers
        final Map<String, Writer> writers = contentTypes.stream().collect(toMap(Function.identity(), contentType -> {
            try {
                return new OutputStreamWriter(new FileOutputStream(contentType.replace('/', ';')), "UTF-8");
            } catch (Exception ex) {
                System.err.println("Error creating file.");
                System.exit(-1);
                return null;
            }
        }));
        // sort files by MIME type
        forEachInFolder(folder, file -> {
            try {
                String contentType = tika.detect(file);
                if (contentTypes.contains(contentType)) {
                    writers.get(contentType).write(file.getCanonicalPath() + '\n');
                }
            } catch (IOException ex) {
                System.err.println("Error sorting file: " + file.getPath());
            }
        });
        // close file writers
        for (Writer writer : writers.values()) {
            try {
                writer.close();
            } catch (IOException ex) {
                System.err.println("Error closing file.");
            }
        }
    }

    public static void diversityAnalysis(final File folder) {
        final Map<String, Integer> diversity = new HashMap<>();
        forEachInFolder(folder, file -> {
            String contentType;
            try {
                contentType = tika.detect(file);
            } catch (IOException ex) {
                System.err.println("Tika could not read file: " + file.getPath());
                return;
            }
            diversity.put(contentType, diversity.getOrDefault(contentType, 0) + 1);
        });
        try {
            new ObjectMapper().writeValue(new File("diversity.json"), diversity);
        } catch (IOException ex) {
            System.err.println("Error writing diversity.json");
        }
    }

    private static void forEachInFolder(final File folder, Consumer<File> callback) {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                forEachInFolder(file, callback);
            } else if (file.length() > 0) {
                callback.accept(file);
            }
        }
    }
}

package csci599;

import java.io.*;
import java.util.*;
import java.util.function.*;
import static java.util.stream.Collectors.*;
import org.apache.tika.Tika;

public class FileTypeFilter {
    private static Tika tika = new Tika();

    public static List<String> getMIMETypes(final File sortFolder) {
        return Arrays.stream(sortFolder.listFiles())
                     .map(file -> file.getName().replace(';', '/'))
                     .collect(toList());
    }

    public static void forEach(final File sortFolder, BiConsumer<File, String> callback) {
        int line = 0, totalLines = 0;
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
        for (File file : sortFolder.listFiles()) {
            String mimeType = file.getName().replace(';', '/');
            System.out.println(mimeType);
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    callback.accept(new File(scanner.nextLine()), mimeType);
                    line++;
                    System.out.print((int)(100.0 * line / totalLines) + "%\r");
                }
            } catch (Exception ex) {
                System.err.println("Error iterating over files of MIME type " + mimeType);
            }
        }
    }

    public static void sort(final File folder, final List<String> contentTypes) {
        final Map<String, Writer> writers = contentTypes.stream().collect(toMap(Function.identity(), contentType -> {
            try {
                return new OutputStreamWriter(new FileOutputStream(contentType.replace('/', ';')), "UTF-8");
            } catch (Exception ex) {
                System.err.println("Error creating file.");
                System.exit(-1);
                return null;
            }
        }));
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
        for (Writer writer : writers.values()) {
            try {
                writer.close();
            } catch (IOException ex) {
                System.err.println("Error closing file.");
            }
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

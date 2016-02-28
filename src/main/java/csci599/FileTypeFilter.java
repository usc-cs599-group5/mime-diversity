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
        for (File file : sortFolder.listFiles()) {
            String mimeType = file.getName().replace(';', '/');
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    callback.accept(new File(scanner.nextLine()), mimeType);
                }
            } catch (Exception ex) {
                System.err.println("Error iterating over files of MIME type " + mimeType);
            }
        }
    }

    private static void forEach(final File folder, final List<String> contentTypes, BiConsumer<File, String> callback) {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                forEach(file, contentTypes, callback);
            } else {
                String contentType;
                try {
                    contentType = tika.detect(file);
                } catch (IOException ex) {
                    System.err.println("Tika could not read file: " + file.getPath());
                    continue;
                }
                if (contentTypes.contains(contentType)) {
                    callback.accept(file, contentType);
                }
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
        forEach(folder, contentTypes, (file, contentType) -> {
            try {
                writers.get(contentType).write(file.getCanonicalPath() + '\n');
            } catch (IOException ex) {
                System.err.println("Error writing to file.");
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
}

package csci599;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.BiConsumer;
import org.apache.tika.Tika;

public class FileTypeFilter {
    private static Tika tika = new Tika();

    public static void forEach(final File folder, final List<String> contentTypes, BiConsumer<File, String> callback) {
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
}

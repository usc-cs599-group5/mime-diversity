package csci599;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.BiConsumer;
import org.apache.tika.Tika;

public class FileTypeFilter {
    private static Tika tika = new Tika();

    public static void forEach(File folder, List<String> contentTypes, BiConsumer<File, String> callback) throws IOException {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                forEach(file, contentTypes, callback);
            } else {
                String contentType = tika.detect(file);
                if (contentTypes.contains(contentType)) {
                    callback.accept(file, contentType);
                }
            }
        }
    }
}

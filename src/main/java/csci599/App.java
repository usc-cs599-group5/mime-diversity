package csci599;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    public static void main(String[] args) {
        String usage = "Command line arguments:\n" +
            "sort <folder> <mime types>\n" +
            "    For each of the <mime types>, create a text file in the current directory listing files in <folder> of that MIME type.\n" +
            "bfa <sort folder>\n" +
            "    Perform byte frequency analysis using the file lists in <sort folder>.\n" +
            "bfc <sort folder>\n" +
            "    Perform byte frequency correlation using the file lists in <sort folder>.\n" +
            "fht <sort folder>\n" +
            "    Perform file header/trailer analysis using the file lists in <sort folder>.\n";
        if (args.length < 2) {
            System.out.print(usage);
            return;
        }
        switch (args[0]) {
            case "sort":
                FileTypeFilter.sort(new File(args[1]), Arrays.asList(Arrays.copyOfRange(args, 2, args.length)));
                break;
            case "bfa":
            {
                final File folder = new File(args[1]);
                BFA bf = new BFA(folder);
                bf.listFilesForFolder(folder);
                break;
            }
            case "bfc":
            {
                final File folder=new File(args[1]);
                BFCCrossCorrelation bfc5c = new BFCCrossCorrelation(folder);
            try {
                bfc5c.listFilesForFolder(folder);
            } catch (IOException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
                break;
            }
            case "fht":
                FHT.analyze(new File(args[1]));
                break;
            default:
                System.out.print(usage);
        }
    }
}

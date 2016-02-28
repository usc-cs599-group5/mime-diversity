package csci599;

import java.io.File;
import java.util.*;

public class App {
    public static void main(String[] args) {
        String usage = "Command line arguments:\n" +
            "bfa <folder>\n" +
            "    Perform byte frequency analysis on files in <folder>.\n" +
            "bfc <folder>\n" +
            "    Perform byte frequency correlation on files in <folder>.\n" +
            "fht <folder> <mime types>\n" +
            "    Perform file header/trailer analysis on files in <folder> of specified <mime types>.\n";
        if (args.length < 2) {
            System.out.print(usage);
            return;
        }
        switch (args[0]) {
            case "bfa":
            {
                BFA bf = new BFA();
                final File folder = new File(args[1]);
                bf.listFilesForFolder(folder);
                break;
            }
                
            case "bfc":
            {
                BFDCorrelation bfc5a = new BFDCorrelation();
                final File folder = new File(args[1]);
                try {
                    bfc5a.listFilesForFolder(folder);
                } catch (Exception ex) {
                    System.err.println("Error in BFC:");
                    ex.printStackTrace();
                }
                break;
            }
            case "fht":
                FHT.analyze(new File(args[1]), Arrays.asList(Arrays.copyOfRange(args, 2, args.length)));
                break;
            default:
                System.out.print(usage);
        }
    }
}

package csci599;

import java.io.File;
import java.util.*;

public class App {
    public static void main(String[] args) {
        String usage = "Pass bfa to perform byte frequency analysis, bfc to perform byte frequency correlation, or fht to perform file/header trailer analysis.";
        if (args.length < 1) {
            System.out.println(usage);
            return;
        }
        switch (args[0]) {
            case "bfa":
            {
                System.out.println("TODO: bfa");
                BFA bf = new BFA();
                //bf.freqAnalysis();
                final File folder = new File("C:\\Users\\Public\\Pictures\\Sample Pictures\\");
                bf.listFilesForFolder(folder);
                break;
            }
                
            case "bfc":
                System.out.println("TODO: bfc");
                break;
            case "fht":
                FHT.analyze(Arrays.copyOfRange(args, 1, args.length));
                break;
            default:
                System.out.println(usage);
        }
    }
}

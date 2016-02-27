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
                BFA bf = new BFA();
                final File folder = new File("D:\\Big_Data_Dumps\\temp");
                bf.listFilesForFolder(folder);
                break;
            }
                
            case "bfc":
                System.out.println("TODO: bfc");
                break;
            case "fht":
                if (args.length < 2) {
                    System.out.println("Pass in folder then list of content types");
                    return;
                }
                FHT.analyze(new File(args[1]), Arrays.asList(Arrays.copyOfRange(args, 2, args.length)));
                break;
            default:
                System.out.println(usage);
        }
    }
}

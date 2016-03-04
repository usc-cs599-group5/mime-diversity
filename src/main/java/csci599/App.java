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
            "    Perform byte frequency analysis using the file lists in <sort folder>, saving the output to bfa.json.\n" +
            "bfd <sort folder>\n" +
            "    Perform byte frequency distribution correlation using bfa.json and the file lists in <sort folder>, saving the output to bfd-I-a.json, bfd-I-b.json, and bfd-I-c.json.\n" +
            "bfc <sort folder>\n" +
            "    Perform byte frequency cross correlation using the file lists in <sort folder>, saving the output to bfc.json.\n" +
            "fht <sort folder> [H=16]\n" +
            "    Perform file header/trailer analysis on first H bytes per file using the file lists in <sort folder>, saving the output to fht.json.\n" +
            "diversity <folder>\n" +
            "    Perform MIME diversity analysis on <folder>, saving the output to diversity.json.\n";
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
            case "bfaDetect":
            {
                final File folder = new File(args[1]);
                BFA bf = new BFA(folder);
                bf.listFilesForFolder1(folder);
                break;
            }
            case "bfd":
                new BFDCorrelationAnalysis().listFilesForFolder(new File(args[1]));
                break;
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
                if (args.length >= 3) {
                    FHT.H = Integer.parseInt(args[2]);
                }
                FHT.analyze(new File(args[1]));
                break;
            case "diversity":
                FileTypeFilter.diversityAnalysis(new File(args[1]));
                break;
            case "separatejson":
                try {
                    separateBFDJson sjson=new separateBFDJson(args[1]);
                } catch (IOException ex) {
                    System.err.println("IOException");
                }
                break;
            default:
                System.out.print(usage);
        }
    }
}

package csci599;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BFCCrossCorrelation
{
    static int noOfMIMETypes;
    static int charSetSize;
    static ArrayList<String> MIMETypes;
    static HashMap<String,ArrayList<ArrayList<Double>>> CorrMatrix;
    
    BFCCrossCorrelation()
    {
        noOfMIMETypes=15;
        charSetSize=256;
        MIMETypes=new ArrayList<String>();
        MIMETypes.add("image/jpg");
        MIMETypes.add("image/png");
        MIMETypes.add("text/plain");
        CorrMatrix=new HashMap<String,ArrayList<ArrayList<Double>>>();
    }
    
    public void listFilesForFolder(final File folder) throws IOException{
        FileTypeFilter.forEach(folder,MIMETypes, (file, MIMEType) -> {
            ArrayList<Double> BFD=new ArrayList<Double>();
            try {
                BFD=frequencyDist(file);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BFCCrossCorrelation.class.getName()).log(Level.SEVERE, null, ex);
            }
            ArrayList<ArrayList<Double>> matrix=new ArrayList<ArrayList<Double>>();
            //matrix=generateCorrMatrix(file);
        });        
    }
    
    public ArrayList<Double> frequencyDist(File file) throws FileNotFoundException
    {
        ArrayList<Double> BFD=new ArrayList<Double>();
        /*for(int i=0;i<charSetSize;i++)
            BFD.add(0.0);
        FileInputStream in = new FileInputStream(file);
        int c=0;
        while((c=in.read())!=-1)
        {
            double i=BFD.get(c);
            BFD.remove(c);
            //BFD.
        }*/
        return BFD;
    }
}

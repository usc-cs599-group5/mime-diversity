package csci599;

import com.fasterxml.jackson.databind.ObjectMapper;
import static csci599.BFCCrossCorrelation.charSetSize;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BFDCorrelationAnalysis
{
    //int noOfMIMETypes;
    int charSetSize;
    //List<String> MIMETypes;
    HashMap<String,double[]> bfd = new HashMap<>();
    HashMap<String,double[]> cs = new HashMap<>();
    HashMap<String, ArrayList<String>> listOfFiles = new HashMap<>();
    HashMap<String, double[]> newFileBFD = new HashMap<>();
    HashMap<String, int[]> HighLowC = new HashMap<>();
    
    BFDCorrelationAnalysis()
    {
        charSetSize=256;
        Map<String, BFAFingerprint> json = null;
        try {
            json = JSONGenerator.readJSON("E:\\Sem 2\\CSCI 599\\tika-test\\BFA\\bfa75.json");
            //System.out.println(json);
        } catch (IOException ex) {
            System.out.println("Error reading bfa.json. Make sure it exists.");
        }
        for(Object MIMEType:json.keySet())
        {
            double[] bfdA=new double[json.get(MIMEType).BFD.size()];
            ArrayList<Double> bfdAL=new ArrayList<Double>(json.get(MIMEType).BFD);
            ArrayList<String> fileNames=new ArrayList<String>();
            for(int i=0;i<bfdA.length;i++)
            {
                bfdA[i]=bfdAL.get(i);
            }
            bfd.put((String) MIMEType, bfdA);
            listOfFiles.put((String) MIMEType, fileNames);
        }
    }
   
    
    public void listFilesForFolder(final File folder) {
        FileTypeFilter.forEach(folder, (file, MIMEType) -> {
            double[] BFD=new double[charSetSize];
            try {
                ArrayList<String> fileName=listOfFiles.get(MIMEType);
                fileName.add(file.getName());
                listOfFiles.replace(MIMEType, fileName);
                //calculate BFD of the input file
                BFD=frequencyDist(BFD,file);
                BFD=normalize(BFD);
                BFD=compand(BFD);
                //for(int i=0;i<BFD.length;i++)
                //    System.out.println(BFD[i]);
                //System.out.println("");
                newFileBFD.put(file.getName(), BFD);
                double[] CS=new double[charSetSize];
                for(int i=0;i<CS.length;i++)
                    CS[i]=BFD[i];
                CS=calculateCS(CS,MIMEType);
                int[] HighLow=new int[4];
                HighLow=findHL(HighLow,CS);
                HighLowC.put(file.getName(), HighLow);
            } catch (IOException ex) {
                System.err.println("Error reading file: " + file.getPath());
            }
        });
        try {
            new ObjectMapper().writeValue(new File("bfd-I-a.json"), listOfFiles);
            new ObjectMapper().writeValue(new File("bfd-I-b.json"), newFileBFD);
            new ObjectMapper().writeValue(new File("bfd-I-c.json"), HighLowC);
            System.out.println("bfd-I-a.json, bfd-I-b.json, bfd-I-c.json created");
        } catch (IOException ex) {
            System.err.println("Error writing bfc.json");
        }
    }
    
    public double[] frequencyDist(double[] BFD,File file) throws IOException,FileNotFoundException
    {
        for(int i=0;i<BFD.length;i++)
            BFD[i]=0.0;
        try (FileInputStream in = new FileInputStream(file)) {
            int c=0;
            while((c=in.read())!=-1)
                BFD[c]++;
        }
        return BFD;
    }
    
    public double[] normalize(double[] BFD)
    {
        double max=findMax(BFD);
        for(int i=0;i<BFD.length;i++)
        {
            BFD[i]=BFD[i]/max;
        }
        return BFD;
    }
    
    public double findMax(double[] BFD)
    {
        double max=BFD[0];
        for(int i=1;i<BFD.length;i++)
        {
            if(max<BFD[i])
                max=BFD[i];
        }
        return max;
    }
    
    public double[] compand(double[] BFD)
    {
        for(int i=0;i<BFD.length;i++)
            BFD[i]=Math.pow(BFD[i], (1/1.5));
        return BFD;
    }
    
    public double[] calculateCS(double[] BFD,String MIMEType)
    {
        double[] FP=bfd.get(MIMEType);
        for(int i=0;i<BFD.length;i++)
        {
            BFD[i]=BFD[i]-FP[i];
            BFD[i]=CorrelationStrength(BFD[i]);
        }
        return BFD;
    }
    
    public double CorrelationStrength(double correlationFactor)
    {
        //this is linear formula for correlation
        //return 1-Math.abs(correlationFactor);
        //have to use bell curve formula
        double sigma=0.0375;
        return Math.pow(Math.E, (((-1)*Math.pow(correlationFactor, 2))/(2*Math.pow(sigma, 2))));
    }
    
    public int[] findHL(int[] HL,double[] CS)
    {
        double max1Val=CS[0];
        int max1=0;
        double max2Val=CS[1];
        int max2=1;
        if(max2Val>max1Val)
        {
            double temp1=max2Val;
            max2Val=max1Val;
            max1Val=temp1;
            int temp2=max2;
            max2=max1;
            max1=temp2;
        }
        double min1Val=CS[0];
        int min1=0;
        double min2Val=CS[1];
        int min2=1;
        if(min1Val>min2Val)
        {
            double temp1=min1Val;
            min1Val=min2Val;
            min2Val=temp1;
            int temp2=min2;
            min2=min1;
            min1=temp2;
        }
        for(int i=2;i<CS.length;i++)
        {
            if(CS[i]>max2Val)
            {
                if(CS[i]>max1Val)
                {
                    max2Val=max1Val;
                    max2=max1;
                    max1Val=CS[i];
                    max1=i;
                }
                else
                {
                    max2Val=CS[i];
                    max2=i;
                }
            }
            if(CS[i]<min2Val)
            {
                if(CS[i]<min1Val)
                {
                    min2Val=min1Val;
                    min2=min1;
                    min1Val=CS[i];
                    min1=i;
                }
                else
                {
                    min2Val=CS[i];
                    min2=i;
                }
            }
        }
        HL[0]=max1;
        HL[1]=max2;
        HL[2]=min1;
        HL[3]=min2;
        return HL;
    }
}
